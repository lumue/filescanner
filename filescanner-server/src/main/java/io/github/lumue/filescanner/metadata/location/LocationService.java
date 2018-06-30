package io.github.lumue.filescanner.metadata.location;

import io.github.lumue.filescanner.util.FileNamingUtils;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	
	@Timed("filescanner.location_service.create_or_update_fingerprint")
	public Location createOrUpdateFingerprint(Location inlocation) {
		LOGGER.debug("refreshing fingerprint for "+inlocation);
		try {
			FileAttributeAccessor fileAttributeAccessor = new FileAttributeAccessor(Paths.get(inlocation.getUrl()));
			final Location location = locationRepository.findById(inlocation.getUrl()).orElse(inlocation);
			Location.fingerprintLocation(location, fileAttributeAccessor);
			location.setLastScanTime(LocalDateTime.now());
			return locationRepository.save(location);
		} catch (IOException e) {
			LOGGER.error("error accessing file "+inlocation.getUrl(),e);
			throw new RuntimeException(e);
		}
	}
	
	@Timed("filescanner.location_service.discover_metadata")
	public Location resolveMetadataLocations(Location location) {
		LOGGER.debug("resolving metadata locations for "+location);
		try {
			final String filename = location.getUrl().replaceFirst("file://","");
			
			String infoJsonFilename = FileNamingUtils.getInfoJsonFilename(filename);
			createMetadataLocation(infoJsonFilename)
			.ifPresent(location::setInfoJsonLocation);
			
			String nfoFilename = FileNamingUtils.getNfoFilename(filename);
			createMetadataLocation(nfoFilename)
			.ifPresent(location::setNfoLocation);
			
			
			return locationRepository.save(location);
			
		} catch (IOException e) {
			LOGGER.error("error ",e);
			throw new RuntimeException(e);
		}
	}
	
	private Optional<MetadataLocation> createMetadataLocation(String infoJsonFilename) throws IOException {
		final File infoJsonFile = new File(infoJsonFilename);
		Optional<MetadataLocation> infoJsonLocation=Optional.empty();
		if(infoJsonFile.exists()){
			final Path path = infoJsonFile.toPath();
			final FileAttributeAccessor attributeAccessor = new FileAttributeAccessor(path);
			infoJsonLocation=Optional.of(new MetadataLocation(attributeAccessor.getUrl(), Files.readAllBytes(path)));
		}
		return infoJsonLocation;
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
