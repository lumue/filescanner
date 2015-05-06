package io.github.lumue.filescanner.file;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PathWatcher implements Runnable {

	@FunctionalInterface
	public interface PathWatcherCallback {
		public void onPathEvent(Path path);
	}

	private final static Logger LOGGER = LoggerFactory
			.getLogger(PathWatcher.class);

	private final WatchService watchService;

	private final AtomicBoolean running = new AtomicBoolean(false);

	private PathWatcherCallback callback;

	@Autowired
	public PathWatcher(PathWatcherCallback callback) throws IOException {
		super();
		this.watchService = FileSystems.getDefault().newWatchService();
		this.callback = callback;

	}

	@Override
	public void run() {

		LOGGER.info("running PathWatcher");

		while (running.get()) {

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

		try {
			this.watchService.close();
		} catch (IOException e) {
			LOGGER.error("exception closing WatchService", e);
		}
	}

	public synchronized void stop() {
		LOGGER.info("stopping PathWatcher");
		running.compareAndSet(true, false);

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
		AsynchronousRecursiveDirectoryStream directoryStream = null;

		try {

			directoryStream = new AsynchronousRecursiveDirectoryStream(
					rootPath);

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
}
