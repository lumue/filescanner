package io.github.lumue.filescanner.metadata.core;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonProperty;

@Document(indexName = "metadata", type = "filemetadata")
public class FileMetadata {

	@Id
	@JsonProperty("url")
	@Field(type = FieldType.String, index = FieldIndex.analyzed, searchAnalyzer = "standard", indexAnalyzer = "standard", store = true)
	private String url;

	@JsonProperty("name")
	@Field(type = FieldType.String, index = FieldIndex.analyzed, searchAnalyzer = "standard", indexAnalyzer = "standard", store = true)
	private String name;

	@JsonProperty("mimeType")
	@Field(type = FieldType.String, index = FieldIndex.analyzed, searchAnalyzer = "standard", indexAnalyzer = "standard", store = true)
	private String mimeType;

	@JsonProperty("creationTime")
	@Field(type = FieldType.Date, index = FieldIndex.analyzed, searchAnalyzer = "standard", indexAnalyzer = "standard", store = true)
	private LocalDateTime creationTime;

	@JsonProperty("size")
	@Field(type = FieldType.Long, index = FieldIndex.analyzed, searchAnalyzer = "standard", indexAnalyzer = "standard", store = true)
	private Long size;

	@JsonProperty("lastAccessTime")
	@Field(type = FieldType.Date, index = FieldIndex.analyzed, searchAnalyzer = "standard", indexAnalyzer = "standard", store = true)
	private LocalDateTime lastAccessTime;

	@JsonProperty("modificationTime")
	@Field(type = FieldType.Date, index = FieldIndex.analyzed, searchAnalyzer = "standard", indexAnalyzer = "standard", store = true)
	private LocalDateTime modificationTime;

	@JsonProperty("type")
	@Field(type = FieldType.String, index = FieldIndex.analyzed, searchAnalyzer = "standard", indexAnalyzer = "standard", store = true)
	private String type;

	@JsonProperty("hash")
	@Field(type = FieldType.String, index = FieldIndex.analyzed, searchAnalyzer = "standard", indexAnalyzer = "standard", store = true)
	private String hash;

	public FileMetadata() {
		super();
	}

	public FileMetadata(
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

}
