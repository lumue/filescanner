package io.github.lumue.filescanner.discover;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import io.github.lumue.filescanner.util.FileNamingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilesystemScanTask implements Runnable {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(FilesystemScanTask.class);

	private final Path path;

	private final PathEventCallback pathEventCallback;

	private boolean running=false;

	public FilesystemScanTask(String path,
			PathEventCallback pathEventCallback) {
		super();
		this.path = Paths.get(path);
		this.pathEventCallback = pathEventCallback;
	}


	public boolean isRunning() {
		return running;
	}

	@Override
	public void run() {
		{
			running=true;
			try {
				Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
						     @Override
						     public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						          if(!attrs.isDirectory() && FileNamingUtils.isVideoFileExtension(file)){
							LOGGER.debug("file discovered " + file.toString());
						        	  pathEventCallback.onPathEvent(file);
						          }
						          return FileVisitResult.CONTINUE;
						      }

					@Override
					public FileVisitResult visitFileFailed(Path file,
							IOException exc) throws IOException {
						LOGGER.error("exception visiting " + file, exc);
						return FileVisitResult.CONTINUE;
					}
				});
			} catch (Exception e) {
				running=false;
				throw new RuntimeException(e);
			}
			running=false;
			LOGGER.info(" finished scanning " + path);
		}


	}

}
