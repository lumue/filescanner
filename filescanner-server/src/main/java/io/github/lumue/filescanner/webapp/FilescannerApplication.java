package io.github.lumue.filescanner.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(WebappConfiguration.class)
public class FilescannerApplication {


    public static void main(String[] args) {
		SpringApplication.run(FilescannerApplication.class, args);
    }

}
