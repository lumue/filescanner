package io.github.lumue.filescanner.metadata.content;

import io.github.lumue.filescanner.metadata.location.Location;
import io.github.lumue.filescanner.metadata.location.LocationService;
import io.github.lumue.filescanner.metadata.location.ReactiveLocationRepository;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service("contentService")
public class ContentService {
	
	private final static Logger LOGGER= LoggerFactory.getLogger(ContentService.class);
	
	private final ContentRepository contentRepository;
	
	private final LocationService locationService;
	
	private final ReactiveLocationRepository reactiveLocationRepository;
	
	private final ReactiveContentRepository reactiveContentRepository;
	
	private final ThreadPoolTaskScheduler taskScheduler;
	
	@Autowired
	public ContentService(ContentRepository contentRepository, LocationService locationService, ReactiveLocationRepository reactiveLocationRepository, ReactiveContentRepository reactiveContentRepository, ThreadPoolTaskScheduler taskScheduler) {
		this.contentRepository = contentRepository;
		this.locationService = locationService;
		this.reactiveLocationRepository = reactiveLocationRepository;
		this.reactiveContentRepository = reactiveContentRepository;
		this.taskScheduler = taskScheduler;
	}
	
	@Timed("filescanner.content_service.updateOrCreate")
	public Content updateOrCreateContent(Location location){
		
		LOGGER.debug("updating or creating content for location "+location);
		
		final String contentKey = location.getHash();
		final List<Location> locations = locationService.findByHash(contentKey);
		final Content content = contentRepository.findById(contentKey)
				.map(c -> {
					if(c.getContentType()==null)
						return Content.fromLocations(locations);
					else
						return c;
				})
				.orElse(Content.fromLocations(locations));
		
		
		return contentRepository.save(content);
	}
	
	public Flux<Content> findAll() {
		return reactiveContentRepository.findAll();
	}
	
	public Flux<Location> findDuplicateLocations() {
		return reactiveContentRepository.findWithSecondaryLocations()
				.map(Content::getSecondaryLocations)
				.flatMap(Flux::fromIterable);
	}
	
	
}
