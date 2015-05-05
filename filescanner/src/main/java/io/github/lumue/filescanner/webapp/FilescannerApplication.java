package io.github.lumue.filescanner.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import reactor.spring.context.config.EnableReactor;

@SpringBootApplication
@EnableReactor
@ComponentScan("io.github.lumue.filescanner")
@EnableAutoConfiguration
@Import(WebappConfiguration.class)
public class FilescannerApplication {


    public static void main(String[] args) {
		SpringApplication.run(FilescannerApplication.class, args);
    }

}
