package io.github.lumue.filescanner.metadata.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Document(collection = "locations")
public class Location {
	
	@Id
	@JsonProperty("url")
	private String url;
	
	@JsonProperty("name")
	@Indexed
	private String name;
	
	@JsonProperty("mimeType")
	@Indexed
	private String mimeType;
	
	
	@JsonProperty("creationTime")
	@Indexed
	private LocalDateTime creationTime;
	
	@JsonProperty("lastScanTime")
	@Indexed
	private LocalDateTime lastScanTime;
	
	@JsonProperty("size")
	@Indexed
	private Long size;
	
	@JsonProperty("lastAccessTime")
	@Indexed
	private LocalDateTime lastAccessTime;
	
	@JsonProperty("modificationTime")
	@Indexed
	private LocalDateTime modificationTime;
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("hash")
	@Indexed
	private String hash;
	
	
	public Location() {
		super();
	}
	
	public Location(
			String name,
			String url,
			String mimeType,
			LocalDateTime creationTime) {
		super();
		this.name = name;
		this.url = url;
		this.mimeType = mimeType;
		this.creationTime = creationTime;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	public void setCreationTime(LocalDateTime creationTime) {
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
	
	public void setType(String type) {
		this.type = type;
		
	}
	
	public String getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getHash() {
		return hash;
	}
	
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	public static Location createWithAccessor(FileMetadataAccessor accessor) throws IOException {
		try {
			Location location = new DocumentMetadataBuilder()
					.setName(accessor.getName())
					.setUrl(accessor.getUrl())
					.setMimeType(accessor.getMimeType())
					.setCreationTime(accessor.getCreationTime())
					.createDocumentMetadata();
			updateWithAccessor(location, accessor);
			return location;
		}
		catch(IOException ex){
			throw new RuntimeException(ex);
		}
		
	}
	
	public static Location updateWithAccessor(Location location, FileMetadataAccessor accessor)  {
		try {
			final LocalDateTime modificationTime = accessor.getModificationTime();
			final LocalDateTime metadataModificationTime = Optional.ofNullable(location.getModificationTime()).orElse(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC));
			if ((location.getHash() == null || location.getHash().isEmpty())
					|| (metadataModificationTime.isBefore(modificationTime))
			) {
				location.setHash(accessor.getType() + "_" + accessor.getSize() + "_" + accessor.getHash());
			}
			location.setLastAccessTime(
					accessor.getLastAccessTime());
			location.setModificationTime(
					modificationTime);
			location.setSize(accessor.getSize());
			location.setType(accessor.getType());
			
			return location;
		}
		catch(IOException ex){
			throw new RuntimeException(ex);
		}
	}
	
	public LocalDateTime getLastScanTime() {
		return lastScanTime;
	}
	
	public void setLastScanTime(LocalDateTime lastScanTime) {
		this.lastScanTime = lastScanTime;
	}
	
	
	public static class DocumentMetadataBuilder {
		private String name;
		private String url;
		private String mimeType = null;
		private LocalDateTime creationTime = LocalDateTime.now();
		
		public DocumentMetadataBuilder setName(String name) {
			this.name = name;
			return this;
		}
		
		public DocumentMetadataBuilder setUrl(String url) {
			this.url = url;
			return this;
		}
		
		public DocumentMetadataBuilder setMimeType(String mimeType) {
			this.mimeType = mimeType;
			return this;
		}
		
		public DocumentMetadataBuilder setCreationTime(LocalDateTime creationTime) {
			this.creationTime = creationTime;
			return this;
		}
		
		public Location createDocumentMetadata() {
			return new Location(name, url, mimeType, creationTime);
		}
	}
}
