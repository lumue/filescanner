package io.github.lumue.filescanner.metadata.location;

import io.github.lumue.filescanner.metadata.content.Content;
import io.github.lumue.filescanner.metadata.content.ReactiveContentRepository;
import io.github.lumue.filescanner.util.FileNamingUtils;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);
	private final LocationRepository locationRepository;
	
	private final ReactiveLocationRepository reactiveLocationRepository;
	
	private final ReactiveContentRepository reactiveContentRepository;
	
	@Timed("filescanner.content_service.updateOrCreate")
	public Flux<Location> findDuplicateLocations() {
		return reactiveContentRepository.findWithSecondaryLocations()
				.map(Content::getSecondaryLocations)
				.flatMap(Flux::fromIterable);
	}
	
	@Autowired
	public LocationService(LocationRepository locationRepository, ReactiveLocationRepository reactiveLocationRepository, ReactiveContentRepository reactiveContentRepository) {
		this.locationRepository = locationRepository;
		this.reactiveLocationRepository = reactiveLocationRepository;
		this.reactiveContentRepository = reactiveContentRepository;
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
			final String pathname = Paths.get(new URI(inlocation.getUrl())).toAbsolutePath().toString();
			FileAttributeAccessor fileAttributeAccessor = new FileAttributeAccessor(new File(pathname).toPath());
			final Location location = locationRepository.findById(inlocation.getUrl()).orElse(inlocation);
			Location.fingerprintLocation(location, fileAttributeAccessor);
			location.setLastScanTime(LocalDateTime.now());
			return locationRepository.save(location);
		} catch (IOException | URISyntaxException e) {
			LOGGER.error("error accessing file "+inlocation.getUrl(),e);
			throw new RuntimeException(e);
		}
	}
	
	@Timed("filescanner.location_service.discover_metadata")
	public Location resolveMetadataLocations(Location location) {
		LOGGER.debug("resolving metadata locations for "+location);
		try {
			final String filename = Paths.get(new URI(location.getUrl())).toAbsolutePath().toString();
			
			String infoJsonFilename = FileNamingUtils.getInfoJsonFilename(filename);
			createMetadataLocation(infoJsonFilename)
			.ifPresent(location::setInfoJsonLocation);
			
			String nfoFilename = FileNamingUtils.getNfoFilename(filename);
			createMetadataLocation(nfoFilename)
			.ifPresent(location::setNfoLocation);
			
			String metajsonFilename = FileNamingUtils.getMetaJsonFilename(filename);
			createMetadataLocation(metajsonFilename)
			.ifPresent(location::setMetaJsonLocation);
			
			
			return locationRepository.save(location);
			
		} catch (IOException | URISyntaxException e) {
			LOGGER.error("error ",e);
			throw new RuntimeException(e);
		}
	}
	
	private Optional<MetadataLocation> createMetadataLocation(String metadataFilename) throws IOException {
		final File metadataFile = new File(metadataFilename);
		Optional<MetadataLocation> metadataLocation=Optional.empty();
		if(metadataFile.exists()){
			final Path path = metadataFile.toPath();
			final FileAttributeAccessor attributeAccessor = new FileAttributeAccessor(path);
			metadataLocation=Optional.of(new MetadataLocation(attributeAccessor.getUrl(), Files.readAllBytes(path)));
		}
		return metadataLocation;
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
	
	
	public List<Location> findByHash(String contentKey) {
		return locationRepository.findByHash(contentKey);
	}
	
	@Timed("filescanner.location_service.remove")
	public Mono<Void> remove(Location location) {
		LOGGER.info("removing location "+location.getUrl());
		return Mono.fromRunnable(()->deleteFiles(location)
		).and(reactiveLocationRepository.deleteById(location.getUrl()));
	}
	
	private void deleteFiles(Location location) {
		final String filename;
			final String locationUrl = location.getUrl();
			deleteFileAtUrl(locationUrl);
		Optional.ofNullable(location.getInfoJsonLocation())
				.map(MetadataLocation::getUrl)
				.ifPresent(this::deleteFileAtUrl);
		Optional.ofNullable(location.getNfoLocation())
				.map(MetadataLocation::getUrl)
				.ifPresent(this::deleteFileAtUrl);
	}
	
	private boolean deleteFileAtUrl(String locationUrl) {
		String filename;
		try {
			filename = Paths.get(new URI(locationUrl)).toAbsolutePath().toString();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("not an url: "+locationUrl,e);
		}
		LOGGER.debug("deleting file "+filename);
		return new File(filename).delete();
	}
	
	public Mono<Void> deleteDuplicateLocations() {
		return reactiveContentRepository.findWithSecondaryLocations()
				.map(Content::getSecondaryLocations)
				.flatMap(Flux::fromIterable)
				.flatMap(this::remove)
				.collectList()
				.then();
	}
	
	public Mono<Location> findByURI(URI url) {
		return reactiveLocationRepository.findById(url.toString());
	}
}
