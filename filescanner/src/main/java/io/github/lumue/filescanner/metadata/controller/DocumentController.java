package io.github.lumue.filescanner.metadata.controller;

import io.github.lumue.filescanner.metadata.core.DocumentMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.github.lumue.filescanner.metadata.repository.DocumentRepository;

import java.util.stream.Stream;

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
	public Stream<DocumentMetadata> list() {
		return documentRepository.findDocumentMetadata();
	}
}
