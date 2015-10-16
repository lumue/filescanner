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

	private MetadataRepository metadataRepository;

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

			MetadataAccessor filesystemMetadataAccessor = new FilesystemMetadataAccessor(path);

			boolean exists = metadataRepository
					.exists(filesystemMetadataAccessor.getUrl());

			if (exists) {
				updateMetadata(filesystemMetadataAccessor);
			} else {
				insertMetadata(filesystemMetadataAccessor);
			}

		} catch (Throwable t) {
			LOGGER.error("error reading file metadata for "
					+ path, t);
		}

		LOGGER.debug("recorded metadata for" + path);
	}

	private void updateMetadata(
			MetadataAccessor filesystemMetadataAccessor)
			throws IOException {

		FileMetadata fileMetadata = metadataRepository
				.findOne(filesystemMetadataAccessor.getUrl());

		updateFileMetadata(filesystemMetadataAccessor, fileMetadata);

		metadataRepository.save(fileMetadata);

	}

	private void insertMetadata(
			MetadataAccessor filesystemMetadataAccessor)
			throws IOException {

		FileMetadata fileMetadata = new FileMetadata(
				filesystemMetadataAccessor.getName(),
				filesystemMetadataAccessor.getUrl(),
				filesystemMetadataAccessor.getMimeType(),
				filesystemMetadataAccessor.getCreationTime());

		updateFileMetadata(filesystemMetadataAccessor, fileMetadata);

		metadataRepository.save(fileMetadata);

	}

	private void updateFileMetadata(
			MetadataAccessor filesystemMetadataAccessor,
			FileMetadata fileMetadata) throws IOException {
		fileMetadata.setLastAccessTime(
				filesystemMetadataAccessor.getLastAccessTime());
		fileMetadata.setModificationTime(
				filesystemMetadataAccessor.getModificationTime());
		fileMetadata.setSize(filesystemMetadataAccessor.getSize());
		fileMetadata.setType(filesystemMetadataAccessor.getType());
		// fileMetadata.setHash(filesystemMetadataAccessor.getHash());
	}

}
