package io.github.lumue.filescanner.webapp;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.io.IOException;

/**
 * Created by lm on 17.01.16.
 */
@Configuration
public class ElasticsearchConfiguration {
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
}
