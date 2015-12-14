package io.github.lumue.filescanner.path;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.lumue.filescanner.path.repository.PathConfiguration;
import org.elasticsearch.client.Client;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

/**
 * Created by lm on 10.12.15.
 */
@Configuration
@TestPropertySource(locations="classpath:test.properties")
@ComponentScan("io.github.lumue.filescanner.path")
@EnableAutoConfiguration
@Import(PathConfiguration.class)
public class TestConfiguration {

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
