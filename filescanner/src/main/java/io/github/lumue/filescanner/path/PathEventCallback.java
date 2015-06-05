package io.github.lumue.filescanner.path;

import java.nio.file.Path;

@FunctionalInterface
interface PathEventCallback {
	public void onPathEvent(Path path);
}