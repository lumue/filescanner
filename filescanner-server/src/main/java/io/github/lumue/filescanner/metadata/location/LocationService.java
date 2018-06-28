package io.github.lumue.filescanner.metadata.location;

import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LocationService {

	private final LocationRepository locationRepository;
	
	@Autowired
	public LocationService(LocationRepository locationRepository) {
		this.locationRepository = locationRepository;
	}
	
	@Timed("location_service.get_for_url")
	public Optional<Location> getForURL(String url) {
		return locationRepository.findById(url);
	}
	
	@Timed("location_service.create_or_update")
	public Location createOrUpdate(File file) {
		try {
			FileMetadataAccessor fileMetadataAccessor = new FileMetadataAccessor(file.toPath());
			final Location location = locationRepository.findById(fileMetadataAccessor.getUrl())
					.map(l -> Location.updateWithAccessor(l, fileMetadataAccessor))
					.orElse(Location.createWithAccessor(fileMetadataAccessor));
			location.setLastScanTime(LocalDateTime.now());
			return locationRepository.save(location);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Timed("location_service.is_current")
	public boolean isLocationCurrent(File file)
	{
		try {
			FileMetadataAccessor fileMetadataAccessor = new FileMetadataAccessor(file.toPath());
			final Optional<Location> location = locationRepository.findById(fileMetadataAccessor.getUrl());
			
			
			final LocalDateTime lastScan = location
					.map(Location::getLastScanTime)
					.orElse(LocalDateTime.MIN);
			
			return lastScan.isAfter(fileMetadataAccessor.getModificationTime());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
