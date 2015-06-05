package io.github.lumue.filescanner.path;


import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import reactor.core.Reactor;
import reactor.event.Event;

@Component
public class Pathscanner {

	private final Executor taskExecutor;

	private final Reactor eventbus;

	private final static Logger LOGGER = LoggerFactory.getLogger(Pathscanner.class);

	@Autowired
	public Pathscanner(
			Executor taskExecutor,
			Reactor eventBus) {

		super();
		this.taskExecutor = taskExecutor;
		this.eventbus = eventBus;

	}

	public void startScan(final String path) {

		LOGGER.info(" start scanning " + path);

		taskExecutor.execute(new FilesystemScanTask(path, (file) -> {
			eventbus.notify("files", Event.wrap(file));
		} ));

	}
}
