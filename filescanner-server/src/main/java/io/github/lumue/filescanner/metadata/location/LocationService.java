package io.github.lumue.filescanner.metadata.location;

import io.micrometer.core.annotation.Timed;
import jdk.management.resource.internal.inst.StaticInstrumentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LocationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);
	private final LocationRepository locationRepository;
	
	private final ReactiveLocationRepository reactiveLocationRepository;
	
	@Autowired
	public LocationService(LocationRepository locationRepository, ReactiveLocationRepository reactiveLocationRepository) {
		this.locationRepository = locationRepository;
		this.reactiveLocationRepository = reactiveLocationRepository;
	}
	
	@Timed("filescanner.location_service.get_for_url")
	public Optional<Location> getForURL(String url) {
		LOGGER.debug("getting Location for url"+url);
		return locationRepository.findById(url);
	}
	
	@Timed("filescanner.location_service.get_for_file")
	public Optional<Location> getForFile(File file) {
		LOGGER.debug("getting Location for file"+file);
		try {
			FileAttributeAccessor fileAttributeAccessor = new FileAttributeAccessor(file.toPath());
			return locationRepository.findById(fileAttributeAccessor.getUrl());
		}
		catch (IOException e){
			LOGGER.error("error accessing file "+file,e);
			throw new RuntimeException(e);
		}
	}
	
	@Timed("filescanner.location_service.get_for_file_or_null")
	public Location getForFileOrNull(File file) {
		return getForFile(file).orElse(null);
	}
	
	@Timed("filescanner.location_service.create_or_update")
	public Location createOrUpdate(File file) {
		LOGGER.debug("refreshing location entry for "+file);
		try {
			FileAttributeAccessor fileAttributeAccessor = new FileAttributeAccessor(file.toPath());
			final Location location = locationRepository.findById(fileAttributeAccessor.getUrl())
					.map(l -> Location.updateWithAccessor(l, fileAttributeAccessor))
					.orElse(Location.createWithAccessor(fileAttributeAccessor));
			location.setLastScanTime(LocalDateTime.now());
			return locationRepository.save(location);
		} catch (IOException e) {
			LOGGER.error("error accessing file "+file,e);
			throw new RuntimeException(e);
		}
	}
	
	@Timed("filescanner.location_service.is_current")
	public boolean isLocationCurrent(File file)
	{
		LOGGER.debug("checking location entry for "+file);
		try {
			FileAttributeAccessor fileAttributeAccessor = new FileAttributeAccessor(file.toPath());
			final Optional<Location> location = locationRepository.findById(fileAttributeAccessor.getUrl());
			
			
			final LocalDateTime lastScan = location
					.map(Location::getLastScanTime)
					.orElse(LocalDateTime.MIN);
			
			return lastScan.isAfter(fileAttributeAccessor.getModificationTime());
		} catch (IOException e) {
			LOGGER.error("error accessing file "+file,e);
			return false;
		}
	}
	
	public Flux<Location> findAll(){
		return reactiveLocationRepository.findAll();
	}
}
