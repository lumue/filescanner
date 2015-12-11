package io.github.lumue.filescanner.webapp;

import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

import io.github.lumue.filescanner.metadata.MetadataConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import reactor.core.Environment;
import reactor.core.Reactor;

import java.io.IOException;

@Configuration
@Import(MetadataConfiguration.class)
public class WebappConfiguration {

	public WebappConfiguration() {
	}


	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JSR310Module());
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		return objectMapper;
	}


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

	@Bean public Environment environment(){
		Environment environment = new Environment();
		return environment;
	}

		@Bean
		public Reactor reactor(Environment environment) {
			return environment.getRootReactor();
		}

}
