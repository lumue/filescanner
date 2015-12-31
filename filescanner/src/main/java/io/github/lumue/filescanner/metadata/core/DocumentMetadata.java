package io.github.lumue.filescanner.metadata.core;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonProperty;

@Document(indexName = "filescanner.metadata", type = "document")
public class DocumentMetadata {

	@Id
	@JsonProperty("url")
	@Field(type = FieldType.String, store = true,index = FieldIndex.not_analyzed)
	private String url;

	@JsonProperty("name")
	@Field(type = FieldType.String, store = true,index = FieldIndex.analyzed)
	private String name;

	@JsonProperty("mimeType")
	@Field(type = FieldType.String,  store = true)
	private String mimeType;

	@JsonProperty("creationTime")
	@Field(type = FieldType.Date,  store = true,index = FieldIndex.analyzed)
	private LocalDateTime creationTime;

	@JsonProperty("size")
	@Field(type = FieldType.Long,  store = true)
	private Long size;

	@JsonProperty("lastAccessTime")
	@Field(type = FieldType.Date,  store = true)
	private LocalDateTime lastAccessTime;

	@JsonProperty("modificationTime")
	@Field(type = FieldType.Date,  store = true)
	private LocalDateTime modificationTime;

	@JsonProperty("type")
	@Field(type = FieldType.String,  store = true,index = FieldIndex.analyzed)
	private String type;

	@JsonProperty("hash")
	@Field(type = FieldType.String,  store = true)
	private String hash;

	@JsonProperty("properties")
	@Field(type = FieldType.Nested,  store = true)
	private Map<String,String> properties=new HashMap<>();

	public DocumentMetadata() {
		super();
	}

	public DocumentMetadata(
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

	public static DocumentMetadata createWithAccessor(MetadataAccessor accessor) throws IOException {
		DocumentMetadata documentMetadata = new DocumentMetadata(
				accessor.getName(),
				accessor.getUrl(),
				accessor.getMimeType(),
				accessor.getCreationTime());
		updateWithAccssor(documentMetadata,accessor	);
		return documentMetadata;
	}

	public static void updateWithAccssor(DocumentMetadata documentMetadata, MetadataAccessor accessor) throws IOException {
		documentMetadata.setLastAccessTime(
				accessor.getLastAccessTime());
		documentMetadata.setModificationTime(
				accessor.getModificationTime());
		documentMetadata.setSize(accessor.getSize());
		documentMetadata.setType(accessor.getType());
		//documentMetadata.setHash(accessor.getHash());
		for (String key:accessor.getPropertyKeys()
		     ) {
			documentMetadata.setProperty(key,accessor.getProperty(key));
		}
	}

	public void setProperty(String key,String value) {
		this.properties.put(key,value);
	}

}
