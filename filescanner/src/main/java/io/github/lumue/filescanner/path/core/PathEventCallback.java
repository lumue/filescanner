package io.github.lumue.filescanner.path.core;

import java.nio.file.Path;

@FunctionalInterface
public interface PathEventCallback {
	void onPathEvent(Path path);
}