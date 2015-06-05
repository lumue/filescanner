package io.github.lumue.filescanner.path;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilesystemScanTask implements Runnable {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(FilesystemScanTask.class);

	private final Path path;

	private final PathEventCallback pathEventCallback;

	public FilesystemScanTask(String path,
			PathEventCallback pathEventCallback) {
		super();
		this.path = Paths.get(path);
		this.pathEventCallback = pathEventCallback;
	}


	@Override
	public void run() {
		{

			try {
				Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
						     @Override
						     public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						          if(!attrs.isDirectory()){
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
				throw new RuntimeException(e);
			}

			LOGGER.info(" finished scanning " + path);
		}


	}

}
