package io.github.lumue.filescanner.discover;

import java.nio.file.Path;

@FunctionalInterface
public interface PathEventCallback {
	void onPathEvent(Path path);
}