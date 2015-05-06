package io.github.lumue.filescanner.scan;

import java.io.IOException;
import java.nio.file.Path;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import io.github.lumue.filescanner.file.PathWatcher;
import reactor.core.Reactor;
import reactor.event.Event;

@Component
public class Pathmonitor {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(Pathmonitor.class);

	private final TaskExecutor taskExecutor;

	private final PathWatcher pathWatcher;

	@Autowired
	public Pathmonitor(Reactor reactor, TaskExecutor taskExecutor)
			throws IOException {
		super();
		this.taskExecutor = taskExecutor;
		this.pathWatcher = new PathWatcher((file) -> {
			reactor.notify("files", Event.wrap(file));
		});
	}

	/**
	 * start monitoring
	 */
	@PostConstruct
	public synchronized void startMonitor() {
		taskExecutor.execute(pathWatcher);
	};

	/**
	 * stop monitoring
	 */
	public synchronized void stopMonitor() {
		pathWatcher.stop();
	};

	/**
	 * register filesystem tree
	 *
	 * @param paths
	 * @throws IOException
	 */
	public void registerTree(Path... rootPaths) throws IOException {
		pathWatcher.watchRecursive(rootPaths);
	}

	/**
	 * register path for montoring
	 *
	 * @param path
	 * @throws IOException
	 */
	public void registerPath(Path... paths) throws IOException {
		pathWatcher.watch(paths);
	};
}
