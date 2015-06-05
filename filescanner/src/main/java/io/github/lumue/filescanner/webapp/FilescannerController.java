package io.github.lumue.filescanner.webapp;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.lumue.filescanner.path.Pathmonitor;
import io.github.lumue.filescanner.path.Pathscanner;

@RestController
public class FilescannerController {

	private final Pathscanner filescanner;

	private final Pathmonitor pathmonitor;

	@Value("${filescanner.path.root}")
	private String rootPath;

	@Autowired
	public FilescannerController(Pathscanner filescanner,
			Pathmonitor pathmonitor) {
		super();
		this.filescanner = Objects.requireNonNull(filescanner);
		this.pathmonitor = pathmonitor;
	}

	@RequestMapping("/startScan")
	public void startScan(@RequestParam String path) {
		filescanner.startScan(rootPath + path);
	}

	@RequestMapping("/startMonitoring")
	public void startMonitoring(@RequestParam String path) throws IOException {
		pathmonitor.registerTree(Paths.get(rootPath + path));
	}

}
