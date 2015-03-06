package lumue.github.io.filescanner;

import lumue.github.io.filescanner.FilescannerApplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FilescannerApplication.class)
@WebAppConfiguration
public class FilescannerApplicationTests {

	@Test
	public void contextLoads() {
	}

}
