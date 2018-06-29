package io.github.lumue.filescanner.integration;

import java.nio.file.Path;
import java.util.function.Consumer;

import io.github.lumue.filescanner.metadata.location.LocationRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class LocationRecorderServiceActivator implements Consumer<Message<Path>> {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(LocationRecorder.class);

	private final AsyncTaskExecutor taskExecutor;

	private final LocationRecorder pathMetadataRecorder;



	@Autowired
	public LocationRecorderServiceActivator(
			@Qualifier("metadataRecorderTaskRunner") AsyncTaskExecutor taskExecutor,
			LocationRecorder recorder) {
		super();
		this.taskExecutor = taskExecutor;
		this.pathMetadataRecorder = recorder;
	}

	@Override
	public void accept(Message<Path> pathEvent) {
		LOGGER.debug("consuming Event " + pathEvent);
		taskExecutor.submit(() -> {
			pathMetadataRecorder.recordMetadata(pathEvent.getPayload());
		} );
		LOGGER.debug("consumed Event " + pathEvent);
	}

}
