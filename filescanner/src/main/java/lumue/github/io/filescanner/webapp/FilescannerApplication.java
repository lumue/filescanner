package lumue.github.io.filescanner.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import reactor.spring.context.config.EnableReactor;

@SpringBootApplication
@EnableReactor
@ComponentScan("lumue.github.io.filescanner")
public class FilescannerApplication {


    public static void main(String[] args) {
		SpringApplication.run(FilescannerApplication.class, args);
    }

}
