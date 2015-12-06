package io.github.lumue.filescanner.metadata.core;

import java.io.IOException;

import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableElasticsearchRepositories("io.github.lumue.filescanner.metadata.repository")
@ComponentScan("io.github.lumue.filescanner.metadata")
@Scope(proxyMode = ScopedProxyMode.NO)
public class MetadataConfiguration {

	@Bean
	public ElasticsearchTemplate elasticsearchTemplate(Client client, EntityMapper entityMapper) {
		return new ElasticsearchTemplate(client,entityMapper );
	}

	@Bean
	public EntityMapper entityMapper(final ObjectMapper objectMapper) {

		return new EntityMapper() {

			@Override
			public String mapToString(Object object) throws IOException {
				return objectMapper.writeValueAsString(object);
			}

			@Override
			public <T> T mapToObject(String source, Class<T> clazz)
					throws IOException {
				return objectMapper.readValue(source, clazz);
			}

		};
	}

	@Bean
	public AsyncTaskExecutor metadataRecorderTaskRunner() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setMaxPoolSize(10);
		threadPoolTaskExecutor.setCorePoolSize(10);
		return threadPoolTaskExecutor;
	}
}
