package io.github.lumue.filescanner.metadata.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class DocumentMetadata {
	
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
	
	@JsonProperty("properties")
	private Map<String, String> properties = new HashMap<>();
	
	@JsonProperty
	private String originaltitle;
	
	@JsonProperty
	private String sorttitle;
	
	@JsonProperty
	private String set;
	
	@JsonProperty
	private String rating;
	
	@JsonProperty
	private String year;
	
	@JsonProperty
	private String top250;
	
	@JsonProperty
	private String votes;
	
	@JsonProperty
	private String outline;
	
	@JsonProperty
	private String plot;
	
	@JsonProperty
	private String tagline;
	
	@JsonProperty
	private Duration runtime;
	
	@JsonProperty
	private String thumb;
	
	@JsonProperty
	private String genre;
	
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
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}
	
	public Map<String, String> getProperties() {
		return properties;
	}
	
	public String getSorttitle() {
		return sorttitle;
	}
	
	public void setSorttitle(String sorttitle) {
		this.sorttitle = sorttitle;
	}
	
	public String getSet() {
		return set;
	}
	
	public void setSet(String set) {
		this.set = set;
	}
	
	public String getRating() {
		return rating;
	}
	
	public void setRating(String rating) {
		this.rating = rating;
	}
	
	public String getYear() {
		return year;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
	
	public String getTop250() {
		return top250;
	}
	
	public void setTop250(String top250) {
		this.top250 = top250;
	}
	
	public String getVotes() {
		return votes;
	}
	
	public void setVotes(String votes) {
		this.votes = votes;
	}
	
	public String getOutline() {
		return outline;
	}
	
	public void setOutline(String outline) {
		this.outline = outline;
	}
	
	public String getPlot() {
		return plot;
	}
	
	public void setPlot(String plot) {
		this.plot = plot;
	}
	
	public String getTagline() {
		return tagline;
	}
	
	public void setTagline(String tagline) {
		this.tagline = tagline;
	}
	
	public Duration getRuntime() {
		return runtime;
	}
	
	public void setRuntime(Duration runtime) {
		this.runtime = runtime;
	}
	
	public String getThumb() {
		return thumb;
	}
	
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	
	public String getGenre() {
		return genre;
	}
	
	public void setGenre(String genre) {
		this.genre = genre;
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
		DocumentMetadata documentMetadata = new DocumentMetadataBuilder()
				.setName(accessor.getName())
				.setUrl(accessor.getUrl())
				.setMimeType(accessor.getMimeType())
				.setCreationTime(accessor.getCreationTime())
				.createDocumentMetadata();
		updateWithAccssor(documentMetadata, accessor);
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
		for (String key : accessor.getPropertyKeys()
		) {
			documentMetadata.setProperty(key, accessor.getProperty(key));
		}
	}
	
	public void setProperty(String key, String value) {
		this.properties.put(key, value);
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
		
		public DocumentMetadata createDocumentMetadata() {
			return new DocumentMetadata(name, url, mimeType, creationTime);
		}
	}
}
