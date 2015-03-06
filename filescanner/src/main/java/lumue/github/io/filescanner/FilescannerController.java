package lumue.github.io.filescanner;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilescannerController {

	private final Filescanner filescanner;

	@Autowired
	public FilescannerController(Filescanner filescanner) {
		super();
		this.filescanner = Objects.requireNonNull(filescanner);
	}

	@RequestMapping("/startScan")
	public void startScan(@RequestParam String path) {
		filescanner.startScan(path);
	}

}
