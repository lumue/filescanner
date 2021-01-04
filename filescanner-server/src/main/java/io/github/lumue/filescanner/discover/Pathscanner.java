package io.github.lumue.filescanner.discover;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;


@Component
public class Pathscanner {
	
	private final ThreadPoolTaskExecutor taskExecutor;
	
	private final static Logger LOGGER = LoggerFactory.getLogger(Pathscanner.class);
	
	private final MessageChannel inboundFileChannel;
	
	private final ConcurrentMap<String, ListenableFuture<?>> runningTasks = new ConcurrentHashMap<>();
	
	@Autowired
	public Pathscanner(
			@Qualifier("filesystemSessionTaskRunner") ThreadPoolTaskExecutor taskExecutor,
			@Qualifier("inboundFileChannel") MessageChannel inboundFileChannel) {
		
		super();
		this.taskExecutor = taskExecutor;
		this.inboundFileChannel = inboundFileChannel;
	}
	
	public ListenableFuture<?> startScan(final String path) {
		final FileHandler pathEventCallback = (file) -> inboundFileChannel.send(MessageBuilder.withPayload(file).build());
		final Consumer<Throwable> errorHandler=null;
		final Consumer<Object> successHandler=null;
		return startScanInternal(path, pathEventCallback, errorHandler, successHandler);
		
	}
	
	private ListenableFuture<?> startScanInternal(String path, FileHandler pathEventCallback, Consumer<Throwable> errorHandler, Consumer<Object> successHandler) {
		LOGGER.info(" start scanning " + path);
		return runningTasks.computeIfAbsent(path, (p) -> {
					final FilesystemScanTask task = new FilesystemScanTask(
							p,
							pathEventCallback
					);
					final ListenableFuture<?> future = taskExecutor.submitListenable(task);
					future.addCallback(
							(SuccessCallback<Object>) result -> {
									runningTasks.remove(p);
									Optional.ofNullable(successHandler).ifPresent(sh->sh.accept(result));
								},
							ex -> {
								LOGGER.error("error scanning path "+p,ex);
								runningTasks.remove(p);
								Optional.ofNullable(errorHandler).ifPresent(eh->eh.accept(ex));
							}
					);
					return future;
				}
		);
	}
	
	public Flux<File> scan(final String path) {
		
		return Flux.<File>create(fluxSinkConsumer->startScanInternal(
				path,
				fluxSinkConsumer::next,
				fluxSinkConsumer::error,
				o -> fluxSinkConsumer.complete()
		));
	}
	
	public Boolean isScanning(String path) {
		return Optional.ofNullable(runningTasks.get(path))
				.map(lf->!(lf.isCancelled()||lf.isDone()))
				.orElse(false);
	}
	
	public Mono<Void> stopScan(String path) {
		runningTasks.computeIfPresent(path,(p,v)->{
			v.cancel(true);
			return null;
		});
		return Mono.empty();
	}
}
