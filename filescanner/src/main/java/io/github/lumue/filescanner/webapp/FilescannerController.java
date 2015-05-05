package io.github.lumue.filescanner.webapp;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.lumue.filescanner.scan.Filescanner;
import io.github.lumue.filescanner.scan.Pathmonitor;

@RestController
public class FilescannerController {

	private final Filescanner filescanner;

	private final Pathmonitor pathmonitor;

	@Autowired
	public FilescannerController(Filescanner filescanner,
			Pathmonitor pathmonitor) {
		super();
		this.filescanner = Objects.requireNonNull(filescanner);
		this.pathmonitor = pathmonitor;
	}

	@RequestMapping("/startScan")
	public void startScan(@RequestParam String path) {
		filescanner.startScan(path);
	}

	@RequestMapping("/startMonitoring")
	public void startMonitoring(@RequestParam String path) throws IOException {
		pathmonitor.registerTree(Paths.get(path));
	}

}
