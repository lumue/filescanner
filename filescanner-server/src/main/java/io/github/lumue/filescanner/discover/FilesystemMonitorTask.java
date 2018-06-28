package io.github.lumue.filescanner.discover;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilesystemMonitorTask implements Runnable {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(FilesystemMonitorTask.class);

	private final WatchService watchService;

	private final AtomicBoolean running = new AtomicBoolean(false);

	private final AtomicBoolean stopRequested=new AtomicBoolean(false);

	private PathEventCallback callback;
	private final Path path;

	public FilesystemMonitorTask(Path path,PathEventCallback callback) throws IOException {
		super();
		this.watchService = FileSystems.getDefault().newWatchService();
		this.callback = callback;
		this.path = path;
	}

	@Override
	public void run() {

		LOGGER.info("running FilesystemMonitorTask");
		try{


		watchRecursive(this.path);

		while (!stopRequested.get()) {
			running.compareAndSet(false,true);
			// wait for key to be signaled
			WatchKey key;
			key = watchService.poll();
			if (null == key) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					LOGGER.error(
							"execution was interrupted externally. shutting down",
							e);
					stop();
				}
				continue;
			}

			// proces key events
			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();

				if (kind == OVERFLOW) {
					LOGGER.warn(
							"overflow event in WatchService. there may have been skipped events");
					continue;
				}

				@SuppressWarnings("unchecked")
				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				final Path file = ev.context();

				LOGGER.debug("file change detected " + file.toString());
				callback.onPathEvent(file);
			}

			key.reset();
		}
		}
		catch (IOException e){
			LOGGER.error("error running path monitor task",e);
		}
		finally {
			try {
				this.watchService.close();
				running.compareAndSet(true,false);
			} catch (IOException e) {
				LOGGER.error("exception closing WatchService", e);
			}
		}
	}

	public synchronized void stop() {
		LOGGER.info("stopping FilesystemMonitorTask");
		stopRequested.compareAndSet(false,true);
		while(running.get()){
			try {
				Thread.sleep(1000);
				LOGGER.info("waiting for FilesystemMonitorTask to stop");
			} catch (InterruptedException e) {
				LOGGER.error("interrupted stopping FilesystemMonitorTask "+this);
			}
		};
		stopRequested.compareAndSet(true,false);
	}

	/**
	 * register filesystem tree
	 *
	 * @param paths
	 * @throws IOException
	 */
	public void watchRecursive(Path... rootPaths) throws IOException {
		for (Path rootPath : rootPaths) {
			watch(listSubdirectories(rootPath));
		}
	}



	private Path[] listSubdirectories(Path rootPath) throws IOException {

		Collection<Path> directories = new ArrayList<Path>();

		DirectoryStream<Path> directoryStream = null;
		try {

			directoryStream = Files.newDirectoryStream(rootPath);

			directoryStream.forEach(file -> {
				if (file != null && file.toFile().isDirectory()) {
					directories.add(file);
				}
			} );

		} finally {
			if (directoryStream != null) {
				directoryStream.close();
			}
		}

		return directories.toArray(new Path[directories.size()]);
	}

	/**
	 * register path for montoring
	 *
	 * @param path
	 * @throws IOException
	 */
	public void watch(Path... paths) throws IOException {
		for (Path path : paths) {
			path.register(watchService, ENTRY_CREATE, ENTRY_DELETE,
					ENTRY_MODIFY);
			LOGGER.info(path + " registered for change monitoring");
		}
	};

	public boolean isRunning() {
		return running.get();
	}

	public boolean isStopRequested() {
		return stopRequested.get();
	}
}
