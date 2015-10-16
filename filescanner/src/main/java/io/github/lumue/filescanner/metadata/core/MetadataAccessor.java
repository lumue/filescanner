package io.github.lumue.filescanner.metadata.core;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Created by lm on 16.10.15.
 */
public interface MetadataAccessor {
	String getMimeType() throws IOException;

	Long getSize();

	LocalDateTime getCreationTime();

	LocalDateTime getLastAccessTime();

	LocalDateTime getModificationTime();

	String getUrl();

	String getType() throws IOException;

	String getHash();

	String getName();

	String getProperty(String key);

	Collection<String> getPropertyKeys();
}
