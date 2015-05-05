package io.github.lumue.filescanner.scan;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Function;
/**
 * @author lm
 */
class AsynchronousRecursiveDirectoryStream implements DirectoryStream<Path> {

	static class FunctionVisitor extends SimpleFileVisitor<Path> {

		Function<Path, FileVisitResult> pathFunction;

		public FunctionVisitor(Function<Path, FileVisitResult> pathFunction) {
			this.pathFunction = pathFunction;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
				throws IOException {
			return pathFunction.apply(file);
		}
	}

	static class FilterBuilder {

		private FilterBuilder() {
		}

		public static DirectoryStream.Filter<Path> buildGlobFilter(
				String pattern) {
			final PathMatcher pathMatcher = getPathMatcher("glob:" + pattern);
			return new DirectoryStream.Filter<Path>() {
				@Override
				public boolean accept(Path entry) throws IOException {
					return pathMatcher.matches(entry);
				}
			};
		}

		public static DirectoryStream.Filter<Path> buildRegexFilter(
				String pattern) {
			final PathMatcher pathMatcher = getPathMatcher("regex:" + pattern);
			return new DirectoryStream.Filter<Path>() {
				@Override
				public boolean accept(Path entry) throws IOException {
					return pathMatcher.matches(entry);
				}
			};
		}

		private static PathMatcher getPathMatcher(String pattern) {
			return FileSystems.getDefault().getPathMatcher(pattern);
		}

	}

	private LinkedBlockingQueue<Path> pathsBlockingQueue = new LinkedBlockingQueue<>();
	private boolean closed = false;
	private FutureTask<Void> pathTask;
	private final Path startPath;
	private Filter<Path> filter;

	public AsynchronousRecursiveDirectoryStream(Path startPath, String pattern)
			throws IOException {
		this.filter = FilterBuilder.buildGlobFilter(Objects
				.requireNonNull(pattern));
		this.startPath = Objects.requireNonNull(startPath);
	}

	@Override
	public Iterator<Path> iterator() {
		confirmNotClosed();
		findFiles(startPath, filter);
		return new Iterator<Path>() {
			Path next;

			@Override
			public boolean hasNext() {
				return !(pathsBlockingQueue.isEmpty() && pathTask.isDone());
			}

			@Override
			public Path next() {
				try {
					next = pathsBlockingQueue.poll();
					while (!pathTask.isDone() && next == null) {
						next = pathsBlockingQueue
								.poll(5, TimeUnit.MILLISECONDS);
					}
					return next;
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				return next;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Removal not supported");
			}
		};
	}

	private void findFiles(final Path startPath, final Filter<Path> filter) {
		pathTask = new FutureTask<>(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				Files.walkFileTree(startPath, new FunctionVisitor(
						getFunction(filter)));
				return null;
			}
		});
		start(pathTask);
	}

	private Function<Path, FileVisitResult> getFunction(
			final Filter<Path> filter)  {
		return new Function<Path, FileVisitResult>() {
			@Override
			public FileVisitResult apply(Path input) {
				try {
					if (filter.accept(input.getFileName())) {
						pathsBlockingQueue.offer(input);
					}
				} catch (IOException e) {
					throw new RuntimeException(
							e);
				}
				return pathTask.isCancelled() ? FileVisitResult.TERMINATE
						: FileVisitResult.CONTINUE;
			}
		};
	}

	@Override
	public void close() throws IOException {
		if (pathTask != null) {
			pathTask.cancel(true);
		}
		pathsBlockingQueue.clear();
		pathsBlockingQueue = null;
		pathTask = null;
		filter = null;
		closed = true;
	}

	private void start(FutureTask<Void> futureTask) {
		new Thread(futureTask).start();
	}

	private void confirmNotClosed() {
		if (closed) {
			throw new IllegalStateException(
					"DirectoryStream has already been closed");
		}
	}

}
