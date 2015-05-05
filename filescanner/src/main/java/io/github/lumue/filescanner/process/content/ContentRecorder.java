package io.github.lumue.filescanner.process.content;

import static reactor.event.selector.Selectors.$;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import reactor.core.Reactor;
import reactor.event.Event;
import reactor.function.Consumer;

@Component
public class ContentRecorder implements Consumer<Event<Path>> {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(ContentRecorder.class);

	@Autowired
	public ContentRecorder(Reactor reactor) {
		super();
		reactor.on($("files"), this);
	}

	@Override
	public void accept(Event<Path> pathEvent) {

		LOGGER.debug("consuming new Event " + pathEvent);


	}



}
