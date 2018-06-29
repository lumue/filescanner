package io.github.lumue.filescanner.metadata.location;

import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LocationService {

	private final LocationRepository locationRepository;
	
	private final ReactiveLocationRepository reactiveLocationRepository;
	
	@Autowired
	public LocationService(LocationRepository locationRepository, ReactiveLocationRepository reactiveLocationRepository) {
		this.locationRepository = locationRepository;
		this.reactiveLocationRepository = reactiveLocationRepository;
	}
	
	@Timed("filescanner.location_service.get_for_url")
	public Optional<Location> getForURL(String url) {
		return locationRepository.findById(url);
	}
	
	@Timed("filescanner.location_service.get_for_file")
	public Optional<Location> getForFile(File file) {
		try {
			FileAttributeAccessor fileAttributeAccessor = new FileAttributeAccessor(file.toPath());
			return locationRepository.findById(fileAttributeAccessor.getUrl());
		}
		catch (IOException e){
			throw new RuntimeException(e);
		}
	}
	
	@Timed("filescanner.location_service.get_for_file_or_null")
	public Location getForFileOrNull(File file) {
		return getForFile(file).orElse(null);
	}
	
	@Timed("filescanner.location_service.create_or_update")
	public Location createOrUpdate(File file) {
		try {
			FileAttributeAccessor fileAttributeAccessor = new FileAttributeAccessor(file.toPath());
			final Location location = locationRepository.findById(fileAttributeAccessor.getUrl())
					.map(l -> Location.updateWithAccessor(l, fileAttributeAccessor))
					.orElse(Location.createWithAccessor(fileAttributeAccessor));
			location.setLastScanTime(LocalDateTime.now());
			return locationRepository.save(location);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Timed("filescanner.location_service.is_current")
	public boolean isLocationCurrent(File file)
	{
		try {
			FileAttributeAccessor fileAttributeAccessor = new FileAttributeAccessor(file.toPath());
			final Optional<Location> location = locationRepository.findById(fileAttributeAccessor.getUrl());
			
			
			final LocalDateTime lastScan = location
					.map(Location::getLastScanTime)
					.orElse(LocalDateTime.MIN);
			
			return lastScan.isAfter(fileAttributeAccessor.getModificationTime());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Flux<Location> findAll(){
		return reactiveLocationRepository.findAll();
	}
}
