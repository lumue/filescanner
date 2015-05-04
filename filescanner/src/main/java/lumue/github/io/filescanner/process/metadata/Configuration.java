package lumue.github.io.filescanner.process.metadata;

import java.io.IOException;

import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

@org.springframework.context.annotation.Configuration
public class Configuration {

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
