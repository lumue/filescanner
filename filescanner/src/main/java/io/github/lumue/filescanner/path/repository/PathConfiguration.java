package io.github.lumue.filescanner.path.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;

/**
 * Created by lm on 10.12.15.
 */
@Configuration
@EnableElasticsearchRepositories("io.github.lumue.filescanner.path.repository")
public class PathConfiguration {
    @Bean(name = "filesystemSessionTaskRunner")
    public ThreadPoolTaskExecutor filesystemSessionTaskRunner() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(10);
        threadPoolTaskExecutor.setCorePoolSize(10);
        return threadPoolTaskExecutor;
    }
}