package io.github.lumue.filescanner.process.metadata;

import static reactor.event.selector.Selectors.$;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

import reactor.core.Reactor;
import reactor.event.Event;
import reactor.function.Consumer;

@Component
public class MetadataRecorderServiceActivator implements Consumer<Event<Path>> {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(MetadataRecorder.class);

	private final AsyncTaskExecutor taskExecutor;

	private final MetadataRecorder metadataRecorder;

	@Autowired
	public MetadataRecorderServiceActivator(
			Reactor reactor,
			@Qualifier("metadataRecorderTaskRunner") AsyncTaskExecutor taskExecutor,
			MetadataRecorder recorder) {
		super();
		this.taskExecutor = taskExecutor;
		this.metadataRecorder = recorder;
		reactor.on($("files"), this);
	}

	@Override
	public void accept(Event<Path> pathEvent) {
		LOGGER.debug("consuming Event " + pathEvent);
		taskExecutor.submit(() -> {
			metadataRecorder.recordMetadata(pathEvent.getData());
		} );
		LOGGER.debug("consumed Event " + pathEvent);
	}

}
