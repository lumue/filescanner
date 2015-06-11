package io.github.lumue.filescanner.metadata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.lumue.filescanner.metadata.core.FileMetadata;
import io.github.lumue.filescanner.metadata.elasticsearch.MetadataRepository;

@RestController
public class MetadataController {

	private final MetadataRepository metadataRepository;

	@Autowired
	public MetadataController(MetadataRepository metadataRepository) {
		super();
		this.metadataRepository = metadataRepository;
	}

	@RequestMapping("/metadata/find")
	public FileMetadata find(@RequestParam String url) {
		return metadataRepository.findOne(url);
	}

}
