package io.github.lumue.filescanner.metadata.core;

import java.io.IOException;

import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableElasticsearchRepositories("io.github.lumue.filescanner.metadata.core")
@ComponentScan("io.github.lumue.filescanner.metadata.core")
@Scope(proxyMode = ScopedProxyMode.NO)
public class FilescannerConfiguration {

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
}
