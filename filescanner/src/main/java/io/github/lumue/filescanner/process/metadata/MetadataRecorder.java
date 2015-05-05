package io.github.lumue.filescanner.process.metadata;

import static reactor.event.selector.Selectors.$;

import java.io.IOException;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import reactor.core.Reactor;
import reactor.event.Event;
import reactor.function.Consumer;

@Component
public class MetadataRecorder implements Consumer<Event<Path>> {

	private MetadataRepository metadataRepository;

	private final static Logger LOGGER = LoggerFactory.getLogger(MetadataRecorder.class);

	@Autowired
	public MetadataRecorder(MetadataRepository metadataRepository, Reactor reactor) {
		super();
		this.metadataRepository = metadataRepository;
		reactor.on($("files"), this);
	}

	@Override
	public void accept(Event<Path> pathEvent) {

		LOGGER.debug("consuming new Event " + pathEvent);

		try {

			FilesystemMetadataAccessor filesystemMetadataAccessor = new FilesystemMetadataAccessor(pathEvent.getData());

			boolean exists = metadataRepository.exists(filesystemMetadataAccessor.getUrl());

			if (exists) {
				updateMetadata(filesystemMetadataAccessor);
			} else {
				insertMetadata(filesystemMetadataAccessor);
			}

		} catch (IOException e) {
			pathEvent.consumeError(e);
		}

	}

	private void updateMetadata(FilesystemMetadataAccessor filesystemMetadataAccessor) throws IOException {

		LOGGER.debug("updating metadata for " + filesystemMetadataAccessor.getUrl());

		FileMetadata fileMetadata = metadataRepository.findOne(filesystemMetadataAccessor.getUrl());

		fileMetadata.setLastAccessTime(filesystemMetadataAccessor.getLastAccessTime());
		fileMetadata.setModificationTime(filesystemMetadataAccessor.getModificationTime());
		fileMetadata.setSize(filesystemMetadataAccessor.getSize());
		fileMetadata.setType(filesystemMetadataAccessor.getType());

		metadataRepository.save(fileMetadata);
	}

	private void insertMetadata(FilesystemMetadataAccessor filesystemMetadataAccessor) throws IOException {

		LOGGER.debug("inserting metadata for " + filesystemMetadataAccessor.getUrl());

		FileMetadata fileMetadata = new FileMetadata(filesystemMetadataAccessor.getUrl(),
				filesystemMetadataAccessor.getMimeType(),
				filesystemMetadataAccessor.getCreationTime());

		fileMetadata.setLastAccessTime(filesystemMetadataAccessor.getLastAccessTime());
		fileMetadata.setModificationTime(filesystemMetadataAccessor.getModificationTime());
		fileMetadata.setSize(filesystemMetadataAccessor.getSize());
		fileMetadata.setType(filesystemMetadataAccessor.getType());

		metadataRepository.save(fileMetadata);
	}

}
