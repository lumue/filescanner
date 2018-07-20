package io.github.lumue.filescanner.metadata.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.lumue.filescanner.metadata.location.Location;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Document(collection = "contentcollection")
@TypeAlias("Content")
public class Content {
	
	
	public static final LocationComparator LOCATION_COMPARATOR = new LocationComparator();
	
	@PersistenceConstructor
	public Content(String contentKey, String contentType, String mimeType, Long size, LocalDateTime updated) {
		this.contentKey = contentKey;
		this.contentType = contentType;
		this.mimeType = mimeType;
		this.size = size;
		this.updated=updated;
	}
	
	public static Content fromLocations(List<Location> locations) {
		Objects.requireNonNull(locations);
		Assert.notEmpty(locations,"locations must not be empty");
		final Location primaryLocation = locations.get(0);
		final Content content = fromLocation(primaryLocation);
		content.setLocations(locations);
		return content;
	}
	
	public List<Location>   getSecondaryLocations() {
		if(locations.size()<2)
			return Collections.emptyList();
		return Collections.unmodifiableList(locations.subList(1,locations.size()-1));
	}
	
	private static class LocationComparator implements Comparator<Location> {
		
		@Override
		public int compare(Location o1, Location o2) {
			
			if(o1.getUrl().equals(o2.getUrl()))
				return 0;
			
			final boolean o1HasInfoJson = o1.getInfoJsonLocation() != null;
			final boolean o2HasInfoJson = o2.getInfoJsonLocation() != null;
			
			if (
					(o1HasInfoJson && o2HasInfoJson)
							|| (!o1HasInfoJson && !o2HasInfoJson)
			) {
				return o1.getCreationTime().compareTo(o2.getCreationTime());
			}
			
			if (o1HasInfoJson)
				return 1;
			
			return -1;
			
		}
		
		
		
	}
	
	@Id
	@JsonProperty("url")
	private final String contentKey;
	
	private List<Location> locations = new LinkedList<>();
	
	@JsonProperty("contentType")
	private final String contentType;
	
	@JsonProperty("mimeType")
	@Indexed
	private final String mimeType;
	
	@JsonProperty("size")
	private final Long size;
	public static Content fromLocation(Location location) {
		final Content content = new Content(location.getHash(),location.getType(), location.getMimeType(), location.getSize(), LocalDateTime.now());
		content.addLocation(location);
		return content;
	}
	
	@JsonProperty("updated")
	private LocalDateTime updated;
	
	
	public String getMimeType() {
		return mimeType;
	}
	
	public Long getSize() {
		return size;
	}
	
	public LocalDateTime getUpdated() {
		return updated;
	}
	
	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}
	
	public void addLocation(Location location) {
		if (!contentKey.equals(location.getHash()))
			locations.add(location);
		locationsUpdated();
	}
	
	private void locationsUpdated() {
		locations.sort(LOCATION_COMPARATOR.reversed());
		setUpdated(updated);
	}
	
	public String getContentType() {
		return contentType;
	}
	
	
	
	public String getContentKey() {
		return contentKey;
	}
	
	public List<Location> getLocations() {
		return locations;
	}
	
	public void removeLocations(List<Location> toRemove) {
		locations.removeAll(toRemove);
		locationsUpdated();
	}
	
	public void setLocations(List<Location> locationList) {
		this.locations.clear();
		this.locations.addAll(
				locationList.stream()
						.map(l -> verifyLocation(l))
						.collect(Collectors.toList())
		);
		locationsUpdated();
	}
	
	private Location verifyLocation(Location l) {
		if(!l.getHash().equals(getContentKey()))
			throw new IllegalArgumentException("can not add location "+l+" to content "+this+". contentKey does not match");
		if(!l.getType().equals(getContentType()))
			throw new IllegalArgumentException("can not add location "+l+" to content +"+this+". content type does not match");
		if(!l.getSize().equals(getSize()))
			throw new IllegalArgumentException("can not add location "+l+" to content +"+this+". content size does not match");
		return l;
	}
}
