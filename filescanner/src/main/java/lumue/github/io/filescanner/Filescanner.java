package lumue.github.io.filescanner;


import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import reactor.core.Reactor;
import reactor.event.Event;

@Component
public class Filescanner {

	private final TaskExecutor taskExecutor;

	private final Reactor reactor;

	private final static Logger LOGGER = LoggerFactory.getLogger(Filescanner.class);

	@Autowired
	public Filescanner(
			TaskExecutor taskExecutor,
			Reactor reactor) {

		super();
		this.taskExecutor = taskExecutor;
		this.reactor = reactor;

	}

	public void startScan(final String path) {

		LOGGER.info(" start scanning " + path);

		taskExecutor.execute(() -> {

			try {

				DirectoryStream<Path> directoryStream = new AsynchronousRecursiveDirectoryStream(Paths.get(path), "*");
				directoryStream.forEach(file -> {

					if (file != null) {
						LOGGER.debug("file discovered " + file.toString());
						reactor.notify("files", Event.wrap(file));
					} else {
						LOGGER.warn("discovered null");
					}

				});
				directoryStream.close();
				LOGGER.info(" finished scanning " + path);

			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		});

	}
}
