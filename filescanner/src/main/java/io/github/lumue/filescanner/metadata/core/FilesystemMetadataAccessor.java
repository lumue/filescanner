package io.github.lumue.filescanner.metadata.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FilesystemMetadataAccessor implements MetadataAccessor {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(FilesystemMetadataAccessor.class);

	private final BasicFileAttributes attrs;

	private final Path path;

	private final AtomicReference<String> hash = new AtomicReference<>(null);


	public FilesystemMetadataAccessor(Path path) throws IOException {
		super();
		attrs = Files.readAttributes(path, BasicFileAttributes.class);
		this.path = path;
	}


	@Override
	public String getMimeType() throws IOException {
		return Files.probeContentType(path);
	}

	@Override
	public Long getSize() {

		return attrs.size();
	}


	@Override
	public LocalDateTime getCreationTime() {
		return LocalDateTime.ofInstant(attrs.creationTime().toInstant(), TimeZone.getDefault().toZoneId());
	}

	@Override
	public LocalDateTime getLastAccessTime() {
		return LocalDateTime.ofInstant(attrs.lastAccessTime().toInstant(), TimeZone.getDefault().toZoneId());
	}

	@Override
	public LocalDateTime getModificationTime() {
		return LocalDateTime.ofInstant(attrs.lastModifiedTime().toInstant(), TimeZone.getDefault().toZoneId());
	}

	@Override
	public String getUrl() {
		return path.toUri().toString();
	}

	@Override
	public String getType() throws IOException {
		return fromMimeType(getMimeType());
	}

	@Override
	public String getHash() {
		hash.compareAndSet(null, calculateHash());
		return hash.get();
	}

	private String calculateHash() {

		LOGGER.debug("calculating hash for " + this.path);

		FileInputStream inputStream = null;
		FileChannel channel = null;
		byte[] hashValue;
		String hashString = null;
		MessageDigest md;
		try {

			md = MessageDigest.getInstance("MD5");
			inputStream = new FileInputStream(this.path.toFile());
			channel = inputStream.getChannel();
			ByteBuffer buff = ByteBuffer.allocate(4096);

			while (channel.read(buff) != -1) {
				buff.flip();
				md.update(buff);
				buff.clear();
			}

			hashValue = md.digest();
			hashString = new String(hashValue);

			LOGGER.debug("calculated hash " + hashString + " for " + this.path);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			try {
				if (channel != null) {
					channel.close();
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						LOGGER.error(e.getMessage(), e);
					}
				}
			}
		}
		return hashString;

	}

	private static String fromMimeType(String mimeType) {
		if (mimeType == null) {
			return "GENERIC";
		}

		if (mimeType.startsWith("video")) {
			return "VIDEO";
		}
		if (mimeType.startsWith("audio")) {
			return "AUDIO";
		}
		if (mimeType.startsWith("image")) {
			return "IMAGE";
		}
		if (mimeType.startsWith("text") || mimeType.startsWith("application")) {
			return "DOCUMENT";
		}

		return "GENERIC";
	}

	@Override
	public String getName() {
		return this.path.getFileName().toString();
	}

	@Override
	public <T> T getProperty(String key) {
		return null;
	}

	@Override
	public Collection<String> getPropertyKeys() {
		return Collections.emptyList();
	}

	public Path getPath(){
		return this.path;
	}
}
