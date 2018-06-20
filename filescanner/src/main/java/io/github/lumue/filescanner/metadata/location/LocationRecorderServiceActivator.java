package io.github.lumue.filescanner.metadata.location;

import java.nio.file.Path;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;
import reactor.bus.Event;
import reactor.bus.EventBus;

import static reactor.bus.selector.Selectors.$;

@Component
public class LocationRecorderServiceActivator implements Consumer<Event<Path>> {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(LocationRecorder.class);

	private final AsyncTaskExecutor taskExecutor;

	private final LocationRecorder pathMetadataRecorder;



	@Autowired
	public LocationRecorderServiceActivator(
			EventBus reactor,
			@Qualifier("metadataRecorderTaskRunner") AsyncTaskExecutor taskExecutor,
			LocationRecorder recorder) {
		super();
		this.taskExecutor = taskExecutor;
		this.pathMetadataRecorder = recorder;
		reactor.on($("files"), this);
	}

	@Override
	public void accept(Event<Path> pathEvent) {
		LOGGER.debug("consuming Event " + pathEvent);
		taskExecutor.submit(() -> {
			pathMetadataRecorder.recordMetadata(pathEvent.getData());
		} );
		LOGGER.debug("consumed Event " + pathEvent);
	}

}
