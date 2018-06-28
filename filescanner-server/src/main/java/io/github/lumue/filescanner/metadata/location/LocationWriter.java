package io.github.lumue.filescanner.metadata.location;

/**
 * Created by lm on 04.11.16.
 */
@FunctionalInterface
public interface LocationWriter {
		void writeMetadata(Location location);
}
