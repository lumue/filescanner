package io.github.lumue.filescanner.metadata.controller;

import io.github.lumue.filescanner.metadata.core.DocumentMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.lumue.filescanner.metadata.repository.MetadataRepository;

@RestController
public class MetadataController {

	private final MetadataRepository metadataRepository;

	@Autowired
	public MetadataController(MetadataRepository metadataRepository) {
		super();
		this.metadataRepository = metadataRepository;
	}

	@RequestMapping("/metadata/find")
	public DocumentMetadata find(@RequestParam String url) {
		return metadataRepository.findOne(url);
	}

}
