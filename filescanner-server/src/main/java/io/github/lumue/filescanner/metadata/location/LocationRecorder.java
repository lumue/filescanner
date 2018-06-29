package io.github.lumue.filescanner.metadata.location;

import java.io.IOException;
import java.nio.file.Path;

import io.github.lumue.filescanner.util.FileExtensionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocationRecorder {

	private final LocationRepository locationRepository;

	private final static Logger LOGGER = LoggerFactory
			.getLogger(LocationRecorder.class);

	@Autowired
	public LocationRecorder(
			LocationRepository locationRepository) {
		super();
		this.locationRepository = locationRepository;

	}

	public void recordMetadata(Path path) {
		
		if (!FileExtensionUtils.isVideoFileExtension(path))
			return;
		
		LOGGER.debug("recording metadata for" + path);

		try {

			FileAttributeAccessor metadataAccessor = new FileAttributeAccessor(path);
			
			final Location metadata = locationRepository.findById(metadataAccessor.getUrl()).orElse(null);
			boolean exists = metadata !=null;

			if (exists  && metadataAccessor.getModificationTime().isAfter(metadata.getModificationTime())) {
				updateMetadata(metadataAccessor);
			} else {
				insertMetadata(metadataAccessor);
			}

		} catch (Throwable t) {
			LOGGER.error("error reading file metadata for "
					+ path, t);
		}

		LOGGER.debug("recorded metadata for" + path);
	}

	private void updateMetadata(
			FileAttributeAccessor accessor)
			throws IOException {

		Location location = locationRepository
				.findById(accessor.getUrl())
				.orElseThrow(()->new IllegalArgumentException("existing url location required"));

		Location.updateWithAccessor(location, accessor);

		locationRepository.save(location);

	}

	private void insertMetadata(
			FileAttributeAccessor accessor)
			throws IOException {

		Location location = Location.createWithAccessor(accessor);

		locationRepository.save(location);

	}



}
