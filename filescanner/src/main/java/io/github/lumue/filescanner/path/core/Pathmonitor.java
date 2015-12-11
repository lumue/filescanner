package io.github.lumue.filescanner.path.core;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ThreadPoolExecutor;

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

	private final ThreadPoolExecutor taskExecutor;

	private final Reactor reactor;

	@Autowired
	public Pathmonitor(Reactor reactor,
					   @Qualifier("taskScheduler") ThreadPoolExecutor taskExecutor, Reactor reactor1)
			throws IOException {
		super();
		this.taskExecutor = taskExecutor;
		this.reactor = reactor1;
	}

	/**
	 * start monitoring
	 */
	public synchronized FilesystemMonitorTask newMonitor(Path path) throws IOException {
		LOGGER.info("starting pathmonitor for "+path);
		FilesystemMonitorTask pathWatcher=new FilesystemMonitorTask(
				path,
				(file) -> {
			reactor.notify("files", Event.wrap(file));
		});
		taskExecutor.submit(pathWatcher);
		return pathWatcher;
	};



}
