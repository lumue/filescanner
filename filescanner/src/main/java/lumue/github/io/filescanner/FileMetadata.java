package lumue.github.io.filescanner;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

@Document(indexName = "files")
public class FileMetadata {

	@Id
	@JsonProperty("url")
	private String url;

	@JsonProperty("mimeType")
	private String mimeType;

	@JsonProperty("creationTime")
	private LocalDateTime creationTime;

	@JsonProperty("size")
	private Long size;

	@JsonProperty("lastAccessTime")
	private LocalDateTime lastAccessTime;

	@JsonProperty("modificationTime")
	private LocalDateTime modificationTime;

	public FileMetadata() {
		super();
	}

	public FileMetadata(
			String url,
			String mimeType,
			LocalDateTime creationTime) {
		super();
		this.url = url;
		this.mimeType = mimeType;
		this.creationTime = creationTime;
	}




	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public LocalDateTime getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(LocalDateTime lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public LocalDateTime getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(LocalDateTime modificationTime) {
		this.modificationTime = modificationTime;
	}

	public String getUrl() {
		return url;
	}

	public String getMimeType() {
		return mimeType;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

}
