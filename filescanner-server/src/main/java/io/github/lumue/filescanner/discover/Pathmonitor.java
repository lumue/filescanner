package io.github.lumue.filescanner.discover;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.SuccessCallback;


@Component
public class Pathmonitor {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(Pathmonitor.class);

	private final ThreadPoolTaskExecutor taskExecutor;

	private final ConcurrentMap<String,FilesystemMonitorTask> runningJobMap=new ConcurrentHashMap<>();
	
	private final MessageChannel inboundFileChannel;
	
	@Autowired
	public Pathmonitor(
			@Qualifier("filesystemSessionTaskRunner") ThreadPoolTaskExecutor taskExecutor,
			MessageChannel inboundFileChannel)
			throws IOException {
		super();
		this.taskExecutor = taskExecutor;
		this.inboundFileChannel = inboundFileChannel;
	}

	/**
	 * start monitoring
	 */
	public synchronized FilesystemMonitorTask newMonitor(final Path path) throws IOException {
		if(runningJobMap.containsKey(path.toString())
			&& runningJobMap.get(path.toString()).isRunning()){
			LOGGER.warn("already running a monitoring job on "+path+" aborted");
		}
		
		return runningJobMap.computeIfAbsent(path.toString(),p-> {
			LOGGER.info("starting pathmonitor for " + path);
			FilesystemMonitorTask pathWatcher = null;
			try {
				pathWatcher = new FilesystemMonitorTask(
						path,
						this::sendMessage);
			} catch (IOException e) {
				LOGGER.error("error installing watcher for path:"+path.toString());
				return null;
			}
			taskExecutor.submitListenable(pathWatcher).addCallback(
					(SuccessCallback<Object>) result -> runningJobMap.remove(path.toString()),
					ex -> runningJobMap.remove(path.toString()));
			return pathWatcher;
		});
		
		
	}
	
	private void sendMessage(Path file) {
		inboundFileChannel.send(MessageBuilder.withPayload(file.toFile()).build());
	}
	
	;

	/**
	 * start monitoring
	 */
	public synchronized void removeMonitor(Path path) throws IOException {
		if(!runningJobMap.containsKey(path.toString())){
			LOGGER.warn("no running  monitoring job on "+path+". aborted");
		}

		FilesystemMonitorTask filesystemMonitorTask = runningJobMap.get(path.toString());
		if(!filesystemMonitorTask.isRunning() || filesystemMonitorTask.isStopRequested()){
			LOGGER.warn("monitoring job on "+path+" already shutdown or stopping.");
			runningJobMap.remove(path.toString());
		}
		filesystemMonitorTask.stop();
		runningJobMap.remove(path.toString());

	};



}
