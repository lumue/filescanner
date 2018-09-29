package io.github.lumue.filescanner.webapp.controller;

import io.github.lumue.filescanner.metadata.location.Location;
import io.github.lumue.filescanner.metadata.location.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URL;

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
	
	@GetMapping("/{url}")
	public Mono<Location> findByUrl(@PathVariable("url") URI url){
		return locationService.findByURI(url);
	}
	
	@GetMapping("/")
	public Flux<Location> findByQuery(@RequestParam("q") String query){
		
			return locationService.findDuplicateLocations();
		
	}
	
	@DeleteMapping("/")
	public Mono<Void> deleteByQuery(@RequestParam("q") String query){
		
		return locationService.deleteDuplicateLocations();
		
	}

}
