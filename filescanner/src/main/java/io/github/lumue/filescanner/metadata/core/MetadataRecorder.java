package io.github.lumue.filescanner.metadata.core;

import java.io.IOException;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.lumue.filescanner.metadata.repository.MetadataRepository;

@Component
public class MetadataRecorder {

	private final MetadataRepository metadataRepository;

	private final static Logger LOGGER = LoggerFactory
			.getLogger(MetadataRecorder.class);

	@Autowired
	public MetadataRecorder(
			MetadataRepository metadataRepository) {
		super();
		this.metadataRepository = metadataRepository;

	}

	public void recordMetadata(Path path) {

		LOGGER.debug("recording metadata for" + path);

		try {

			MetadataAccessor metadataAccessor = new TikaMetadataAccessor(path);

			boolean exists = metadataRepository
					.exists(metadataAccessor.getUrl());

			if (exists) {
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
			MetadataAccessor accessor)
			throws IOException {

		DocumentMetadata documentMetadata = metadataRepository
				.findOne(accessor.getUrl());

		DocumentMetadata.updateWithAccssor(documentMetadata, accessor);

		metadataRepository.save(documentMetadata);

	}

	private void insertMetadata(
			MetadataAccessor accessor)
			throws IOException {

		DocumentMetadata documentMetadata = DocumentMetadata.createWithAccessor(accessor);

		metadataRepository.save(documentMetadata);

	}



}
