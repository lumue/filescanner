package io.github.lumue.filescanner.metadata.content;

import io.github.lumue.filescanner.metadata.location.Location;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContentService {
	
	private final ContentRepository contentRepository;
	
	@Autowired
	public ContentService(ContentRepository contentRepository) {
		this.contentRepository = contentRepository;
	}
	
	@Timed("filescanner.content_service.updateOrCreate")
	public Content updateOrCreateContent(Location location){
		final Content content = contentRepository.findById(location.getHash())
				.map(c -> {
					c.addLocation(location);
					return c;
				})
				.orElse(Content.fromLocation(location));
		
		final List<Location> toRemove = content.getLocations()
				.stream()
				.filter(l -> !(l.getHash().equals(content.getContentKey())))
				.collect(Collectors.toList());
		
		content.removeLocations(toRemove);
		toRemove.forEach(this::updateOrCreateContent);
		
		return contentRepository.save(content);
	}
}
