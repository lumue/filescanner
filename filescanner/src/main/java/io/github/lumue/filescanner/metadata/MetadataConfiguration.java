package io.github.lumue.filescanner.metadata;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@ComponentScan("io.github.lumue.filescanner.metadata")
@Scope(proxyMode = ScopedProxyMode.NO)
public class MetadataConfiguration {
	@Bean
	public ThreadPoolTaskExecutor metadataRecorderTaskRunner() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setMaxPoolSize(100);
		threadPoolTaskExecutor.setCorePoolSize(100);
		threadPoolTaskExecutor.setQueueCapacity(1000);
		return threadPoolTaskExecutor;
	}
}
