package io.github.lumue.filescanner.metadata.location;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class MetadataLocation {
	
	@JsonProperty("url")
	private String url;
	
	@JsonProperty("content")
	private byte[] content;
	
	@JsonProperty("created")
	private LocalDateTime created;
	
	public MetadataLocation() {
	}
	
	public MetadataLocation(String url, byte[] content) {
		this.url=url;
		this.content=content;
		this.created=LocalDateTime.now();
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public LocalDateTime getCreated() {
		return created;
	}
	
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}
	
	public byte[] getContent() {
		return content;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}
}
