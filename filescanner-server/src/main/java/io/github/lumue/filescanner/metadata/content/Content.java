package io.github.lumue.filescanner.metadata.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.lumue.filescanner.metadata.location.Location;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "contentcollection")
public class Content {
	
	@Id
	@JsonProperty("url")
	private String contentKey;
	
	@DBRef
	private  List<Location> locations=new LinkedList<>();
	
	public static Content fromLocation(Location location) {
		final Content content = new Content();
		content.contentKey=location.getHash();
		content.addLocation(location);
		return content;
	}
	
	public void addLocation(Location location) {
		if(!contentKey.equals(location.getHash()))
		locations.add(location);
	}
	
	public String getContentKey() {
		return contentKey;
	}
	
	public List<Location> getLocations() {
		return locations;
	}
	
	public void removeLocations(List<Location> toRemove) {
	    locations.removeAll(toRemove);
	}
	
	public void setLocations(List<Location> locationList) {
		this.locations.clear();
		this.locations.addAll(
				locationList.stream()
				.filter(l->l.getHash().equals(getContentKey()))
				.collect(Collectors.toList())
		);
		
	}
}
