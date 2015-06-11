package io.github.lumue.filescanner.path.core;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import reactor.core.Reactor;
import reactor.event.Event;

@Component
public class Pathmonitor {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(Pathmonitor.class);

	private final Executor taskExecutor;

	private final FilesystemMonitorTask pathWatcher;


	@Autowired
	public Pathmonitor(Reactor reactor,
			@Qualifier("taskScheduler") Executor taskExecutor)
			throws IOException {
		super();
		this.taskExecutor = taskExecutor;
		this.pathWatcher = new FilesystemMonitorTask((file) -> {
			reactor.notify("files", Event.wrap(file));
		});
	}

	/**
	 * start monitoring
	 */
	public synchronized void startMonitor() {
		LOGGER.info("starting pathmonitor");
		taskExecutor.execute(pathWatcher);
	};

	/**
	 * stop monitoring
	 */
	public synchronized void stopMonitor() {
		LOGGER.info("stopping pathmonitor");
		pathWatcher.stop();
	};

	/**
	 * register filesystem tree
	 *
	 * @param paths
	 * @throws IOException
	 */
	public void registerTree(Path rootPath) throws IOException {
		LOGGER.info("registering " + rootPath
				+ " and subdirectories for montoring");
		pathWatcher.watchRecursive(rootPath);
		LOGGER.info(rootPath + " and subdirectories registered for montoring");
	}

	/**
	 * register path for montoring
	 *
	 * @param path
	 * @throws IOException
	 */
	public void registerPath(Path path) throws IOException {
		LOGGER.info("registering " + path + " for montoring");
		pathWatcher.watch(path);
		LOGGER.info(path + " registered for montoring");
	};
}
