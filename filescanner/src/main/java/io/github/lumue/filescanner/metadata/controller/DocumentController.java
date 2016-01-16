package io.github.lumue.filescanner.metadata.controller;

import com.google.common.collect.Lists;
import io.github.lumue.filescanner.metadata.core.DocumentMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.github.lumue.filescanner.metadata.repository.DocumentRepository;

import java.util.List;

@RestController
@RequestMapping("/documents")
@CrossOrigin
public class DocumentController {

	private final DocumentRepository documentRepository;

	@Autowired
	public DocumentController(DocumentRepository documentRepository) {
		super();
		this.documentRepository = documentRepository;
	}

	@RequestMapping("/{url}")
	public DocumentMetadata find(@PathVariable("url") String url) {
		return documentRepository.findOne(url);
	}

	@RequestMapping(method=RequestMethod.GET)
	public List<DocumentMetadata> list() {
		return Lists.newArrayList(documentRepository.findAll());
	}
}
