package io.github.lumue.filescanner.path;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
