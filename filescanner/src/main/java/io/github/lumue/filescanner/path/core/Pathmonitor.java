package io.github.lumue.filescanner.path.core;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import reactor.core.Reactor;
import reactor.event.Event;

@Component
public class Pathmonitor {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(Pathmonitor.class);

	private final ThreadPoolTaskExecutor taskExecutor;

	private final Reactor reactor;

	private final ConcurrentMap<String,FilesystemMonitorTask> runningJobMap=new ConcurrentHashMap<>();

	@Autowired
	public Pathmonitor(Reactor reactor,
					   @Qualifier("filesystemSessionTaskRunner") ThreadPoolTaskExecutor taskExecutor, Reactor reactor1)
			throws IOException {
		super();
		this.taskExecutor = taskExecutor;
		this.reactor = reactor1;
	}

	/**
	 * start monitoring
	 */
	public synchronized FilesystemMonitorTask newMonitor(Path path) throws IOException {
		if(runningJobMap.containsKey(path.toString())
			&& runningJobMap.get(path.toString()).isRunning()){
			LOGGER.warn("already running a monitoring job on "+path+" aborted");
		}

		LOGGER.info("starting pathmonitor for "+path);
		FilesystemMonitorTask pathWatcher= new FilesystemMonitorTask(
				path,
				(file) -> {
			reactor.notify("files", Event.wrap(file));
		});
		taskExecutor.submit(pathWatcher);
		runningJobMap.putIfAbsent(path.toString(),pathWatcher);
		return pathWatcher;
	};

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
