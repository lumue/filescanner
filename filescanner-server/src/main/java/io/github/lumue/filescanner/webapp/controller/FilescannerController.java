package io.github.lumue.filescanner.webapp.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.github.lumue.filescanner.discover.Pathmonitor;
import io.github.lumue.filescanner.discover.Pathscanner;
import reactor.core.publisher.Flux;

@RestController
public class FilescannerController {

	private final Pathscanner filescanner;

	private final Pathmonitor pathmonitor;

	@Autowired
	public FilescannerController(Pathscanner filescanner,
			Pathmonitor pathmonitor) {
		super();
		this.filescanner = Objects.requireNonNull(filescanner);
		this.pathmonitor = pathmonitor;
	}

	@GetMapping("/startScan")
	public void startScan(@RequestParam String path) {
		filescanner.startScan( path);
	}
	
	@GetMapping("/files")
	public Flux<File> files() {
		return filescanner.scan("/mnt/nasbox/media/adult");
	}

	@RequestMapping("/startMonitoring")
	public void startMonitoring(@RequestParam String path) throws IOException {
		pathmonitor.newMonitor(Paths.get( path));
	}

}
