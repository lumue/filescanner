package io.github.lumue.filescanner.webapp.controller;

import io.github.lumue.filescanner.metadata.content.Content;
import io.github.lumue.filescanner.metadata.content.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/content")
public class ContentController {

	private final ContentService contentService;
	
	@Autowired
	public ContentController(ContentService contentService) {
		this.contentService = contentService;
	}
	
	@GetMapping
	public Flux<Content> findAll(){
		return contentService.findAll();
	}

}
