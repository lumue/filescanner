package io.github.lumue.filescanner.integration;

import org.springframework.integration.core.MessageSource;
import org.springframework.integration.endpoint.AbstractMessageSource;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FluxMessageSource<T> extends AbstractMessageSource<T> {
	
	private final Flux<T> flux;
	
	private final BlockingQueue<T> itemQueue=new ArrayBlockingQueue<T>(10);
	
	public FluxMessageSource(Flux<T> all) {
		this.flux=all;
		this.flux.subscribe(itemQueue::add);
	}
	
	
	
	@Override
	protected Object doReceive() {
		return itemQueue.poll();
	}
	
	@Override
	public String getComponentType() {
		return "inbound-channel-adapter";
	}
}
