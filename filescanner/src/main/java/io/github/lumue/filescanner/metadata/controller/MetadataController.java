package io.github.lumue.filescanner.metadata.controller;

import com.google.common.collect.Lists;
import io.github.lumue.filescanner.metadata.core.DocumentMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.github.lumue.filescanner.metadata.repository.MetadataRepository;

import java.util.List;

@RestController
@RequestMapping("/documents")
@CrossOrigin
public class MetadataController {

	private final MetadataRepository metadataRepository;

	@Autowired
	public MetadataController(MetadataRepository metadataRepository) {
		super();
		this.metadataRepository = metadataRepository;
	}

	@RequestMapping("/{url}")
	public DocumentMetadata find(@PathVariable("url") String url) {
		return metadataRepository.findOne(url);
	}

	@RequestMapping(method=RequestMethod.GET)
	public List<DocumentMetadata> list() {
		return Lists.newArrayList(metadataRepository.findAll());
	}
}
