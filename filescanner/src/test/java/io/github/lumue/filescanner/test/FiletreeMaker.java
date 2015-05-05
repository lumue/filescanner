package io.github.lumue.filescanner.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * utility class for creating a file tree with testfiles
 *
 * @author lm
 *
 */
public class FiletreeMaker {

	/**
	 * depth of filetree
	 */
	private final short depth;

	/**
	 * amount of subdirs per directory level
	 */
	private final short breadth;

	/**
	 * number of testfiles in each directory
	 */
	private final short testfilesPerDirectory;

	public FiletreeMaker(short depth, short breadth,
			short testfilesPerDirectory) {
		super();
		this.depth = depth;
		this.breadth = breadth;
		this.testfilesPerDirectory = testfilesPerDirectory;
	}

	public void writeTree(String rootPath) throws IOException {
		writeSubtree(rootPath, 0);
	}

	public void deleteTree(String rootPath) throws IOException {
		Path directory = Paths.get(rootPath);
		Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc)
					throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}

		});
	}

	private void writeSubtree(String rootPath, int level) throws IOException {

		Path dir = Paths.get(rootPath);
		Files.createDirectory(dir);

		for (int j = 1; j <= testfilesPerDirectory; j++) {
			String filePath = rootPath + File.separator + "f" + j;
			Files.createFile(Paths.get(filePath));
		}

		if (level > depth) {
			return;
		}
		for (int i = 1; i <= breadth; i++) {
			String dirPath = rootPath + File.separator + "d" + i;
			writeSubtree(dirPath, level + 1);
		}
	}
}
