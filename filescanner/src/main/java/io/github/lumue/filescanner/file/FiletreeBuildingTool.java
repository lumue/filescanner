package io.github.lumue.filescanner.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * utility class for creating a file tree with testfiles
 *
 * @author lm
 *
 */
public class FiletreeBuildingTool {

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

	private final Set<String> creatednodes = new ConcurrentSkipListSet<String>();

	public FiletreeBuildingTool(short depth, short breadth,
			short testfilesPerDirectory) {
		super();
		this.depth = depth;
		this.breadth = breadth;
		this.testfilesPerDirectory = testfilesPerDirectory;
	}

	public void writeTree(String rootPath) throws IOException {
		creatednodes.clear();
		writeSubtree(rootPath, 0);
	}

	public void deleteTree(String rootPath) throws IOException {

		Path directory = Paths.get(rootPath);

		if (!directory.toFile().exists()) {
			return;
		}

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

		createDirectory(rootPath);

		for (int j = 1; j <= testfilesPerDirectory; j++) {
			String filePath = rootPath + File.separator + "f" + j;
			createFile(filePath);
		}

		if (level > depth) {
			return;
		}
		for (int i = 1; i <= breadth; i++) {
			String dirPath = rootPath + File.separator + "d" + i;
			writeSubtree(dirPath, level + 1);
		}
	}

	private void createFile(String filePath) throws IOException {
		Files.createFile(Paths.get(filePath));
		this.creatednodes.add(filePath);
	}

	private void createDirectory(String dirPath) throws IOException {
		Files.createDirectory(Paths.get(dirPath));
		this.creatednodes.add(dirPath);
	}

	public Set<String> getCreatednodes() {
		return Collections.unmodifiableSet(creatednodes);
	}

}
