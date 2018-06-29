package io.github.lumue.filescanner.integration;

import io.github.lumue.filescanner.metadata.location.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;

public class InboundLocationAdapter<outChannel> {

	private final LocationService locationService;
	
	private final TaskExecutor executor;
	
	private final MessageChannel outChannel;
	
	@Autowired
	public InboundLocationAdapter(LocationService locationService, TaskExecutor executor, MessageChannel outChannel) {
		this.locationService = Objects.requireNonNull(locationService);
		this.executor = Objects.requireNonNull(executor);
		this.outChannel = Objects.requireNonNull(outChannel);
	}
	
	@Autowired
	public InboundLocationAdapter(LocationService locationService, MessageChannel outChannel) {
		this(locationService, new SyncTaskExecutor(), outChannel);
	}
	
	public void scanAll(){
		locationService.findAll()
				.publishOn(Schedulers.fromExecutor(executor))
				.map(l->MessageBuilder.withPayload(l).build())
				.subscribe(m->outChannel.send(m));
	}
	
}
