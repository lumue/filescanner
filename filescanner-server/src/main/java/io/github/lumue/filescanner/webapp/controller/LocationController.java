package io.github.lumue.filescanner.webapp.controller;

import io.github.lumue.filescanner.metadata.location.Location;
import io.github.lumue.filescanner.metadata.location.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/locations")
public class LocationController {

	private final LocationService locationService;
	
	@Autowired
	public LocationController(LocationService locationService) {
		this.locationService = locationService;
	}
	
	@GetMapping
	public Flux<Location> findAll(){
	
		
		return locationService.findAll();
	}
	
	@GetMapping("/{query}")
	public Flux<Location> findByQuery(@PathVariable("query") String query){
		
			return locationService.findDuplicateLocations();
		
	}
	
	@DeleteMapping("/{query}")
	public Mono<Void> deleteByQuery(@PathVariable("query") String query){
		
		return locationService.deleteDuplicateLocations();
		
	}

}
