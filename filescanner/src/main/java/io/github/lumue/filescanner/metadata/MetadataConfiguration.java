package io.github.lumue.filescanner.metadata;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableElasticsearchRepositories("io.github.lumue.filescanner.metadata.repository")
@ComponentScan("io.github.lumue.filescanner.metadata")
@Scope(proxyMode = ScopedProxyMode.NO)
public class MetadataConfiguration {
	@Bean
	public ThreadPoolTaskExecutor metadataRecorderTaskRunner() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setMaxPoolSize(10);
		threadPoolTaskExecutor.setCorePoolSize(10);
		return threadPoolTaskExecutor;
	}
}
