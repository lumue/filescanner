package lumue.github.io.filescanner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class FilesystemMetadataAccessor {

	private final BasicFileAttributes attrs;

	private final Path path;

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

}
