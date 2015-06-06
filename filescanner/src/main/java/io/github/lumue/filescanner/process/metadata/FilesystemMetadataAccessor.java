package io.github.lumue.filescanner.process.metadata;

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
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

class FilesystemMetadataAccessor {

	private final BasicFileAttributes attrs;

	private final Path path;

	private final AtomicReference<String> hash = new AtomicReference<>(null);


	public FilesystemMetadataAccessor(Path path) throws IOException {
		super();
		attrs = Files.<BasicFileAttributes> readAttributes(path, BasicFileAttributes.class);
		this.path = path;
	}


	public String getMimeType() throws IOException {
		return Files.probeContentType(path);
	}

	public Long getSize() {

		return attrs.size();
	}


	public LocalDateTime getCreationTime() {
		return LocalDateTime.ofInstant(attrs.creationTime().toInstant(), TimeZone.getDefault().toZoneId());
	}

	public LocalDateTime getLastAccessTime() {
		return LocalDateTime.ofInstant(attrs.lastAccessTime().toInstant(), TimeZone.getDefault().toZoneId());
	}

	public LocalDateTime getModificationTime() {
		return LocalDateTime.ofInstant(attrs.lastModifiedTime().toInstant(), TimeZone.getDefault().toZoneId());
	}

	public String getUrl() {
		return path.toUri().toString();
	}

	public String getType() throws IOException {
		return fromMimeType(getMimeType());
	}

	public String getHash() {
		hash.compareAndSet(null, calculateHash());
		return hash.get();
	}

	private String calculateHash() {


		FileInputStream inputStream = null;
		FileChannel channel = null;
		byte[] hashValue;

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
			return new String(hashValue);
		} catch (NoSuchAlgorithmException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			try {
				if (channel != null) {
					channel.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {

			}
		}

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

}
