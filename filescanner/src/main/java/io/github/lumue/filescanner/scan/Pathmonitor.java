package io.github.lumue.filescanner.scan;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import reactor.core.Reactor;
import reactor.event.Event;

@Component
public class Pathmonitor implements Runnable {

	private final Reactor reactor;

	private final static Logger LOGGER = LoggerFactory
			.getLogger(Pathmonitor.class);

	private final TaskExecutor taskExecutor;

	private final WatchService watchService;

	private boolean running;

	@Autowired
	public Pathmonitor(Reactor reactor, TaskExecutor taskExecutor,
			WatchService watchService) {
		super();
		this.reactor = reactor;
		this.taskExecutor = taskExecutor;
		this.watchService = watchService;
	}

	/**
	 * start monitoring
	 */
	@PostConstruct
	public synchronized void startMonitor() {
		this.running = true;
		taskExecutor.execute(this);
	};

	@Override
	public void run() {
		while (running) {

			// wait for key to be signaled
			WatchKey key;
			try {
				key = watchService.take();
			} catch (InterruptedException x) {
				return;
			}

			// proces key events
			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();

				if (kind == OVERFLOW) {
					continue;
				}

				@SuppressWarnings("unchecked")
				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				Path file = ev.context();
				LOGGER.info("file change detected " + file.toString());
				reactor.notify("files", Event.wrap(file));
			}

			key.reset();
		}
	}

	/**
	 * stop monitoring
	 */
	public synchronized void stopMonitor() {
		running = false;
	};

	/**
	 * register filesystem tree
	 *
	 * @param paths
	 * @throws IOException
	 */
	public void registerTree(Path... rootPaths) throws IOException {
		for (Path rootPath : rootPaths) {
			registerPath(listSubdirectories(rootPath));
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
	public void registerPath(Path... paths) throws IOException {
		for (Path path : paths) {
			path.register(watchService, ENTRY_CREATE, ENTRY_DELETE,
					ENTRY_MODIFY);
			LOGGER.info(path + " registered for change monitoring");
		}
	};
}
