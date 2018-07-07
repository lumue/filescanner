package io.github.lumue.filescanner.metadata.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.lumue.filescanner.metadata.location.Location;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "contentcollection")
public class Content {
	
	
	public static final LocationComparator LOCATION_COMPARATOR = new LocationComparator();
	
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
	private String contentKey;
	
	@DBRef
	private List<Location> locations = new LinkedList<>();
	
	public static Content fromLocation(Location location) {
		final Content content = new Content();
		content.contentKey = location.getHash();
		content.addLocation(location);
		return content;
	}
	
	public void addLocation(Location location) {
		if (!contentKey.equals(location.getHash()))
			locations.add(location);
		locationsUpdated();
	}
	
	private void locationsUpdated() {
		locations.sort(LOCATION_COMPARATOR.reversed());
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
						.filter(l -> l.getHash().equals(getContentKey()))
						.collect(Collectors.toList())
		);
		locationsUpdated();
	}
}
