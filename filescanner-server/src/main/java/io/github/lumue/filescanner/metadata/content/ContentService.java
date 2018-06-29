package io.github.lumue.filescanner.metadata.content;

import io.github.lumue.filescanner.metadata.location.Location;
import io.github.lumue.filescanner.metadata.location.LocationRepository;
import io.github.lumue.filescanner.metadata.location.ReactiveLocationRepository;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("contentService")
public class ContentService {
	
	private final ContentRepository contentRepository;
	
	private final LocationRepository locationRepository;
	
	private final ReactiveLocationRepository reactiveLocationRepository;
	
	
	@Autowired
	public ContentService(ContentRepository contentRepository, LocationRepository locationRepository, ReactiveLocationRepository reactiveLocationRepository) {
		this.contentRepository = contentRepository;
		this.locationRepository = locationRepository;
		this.reactiveLocationRepository = reactiveLocationRepository;
	}
	
	@Timed("filescanner.content_service.updateOrCreate")
	public Content updateOrCreateContent(Location location){
		final String contentKey = location.getHash();
		final Content content = contentRepository.findById(contentKey)
				.map(c -> {
					c.addLocation(location);
					return c;
				})
				.orElse(Content.fromLocation(location));
		
		
		content.setLocations(locationRepository.findByHash(contentKey));
		
		return contentRepository.save(content);
	}
	
	@Timed("filescanner.content_service.updateOrCreateForAllLocations")
	public void updateOrCreateContentForAllLocations(){
	
	}
}
