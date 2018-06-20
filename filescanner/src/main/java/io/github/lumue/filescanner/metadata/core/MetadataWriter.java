package io.github.lumue.filescanner.metadata.core;

/**
 * Created by lm on 04.11.16.
 */
@FunctionalInterface
public interface MetadataWriter {
		void writeMetadata(DocumentMetadata documentMetadata);
}
