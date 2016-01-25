package io.github.lumue.filescanner.webapp;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import reactor.core.Environment;
import reactor.core.Reactor;
import reactor.spring.context.config.EnableReactor;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Configuration
@EnableReactor
@ComponentScan("io.github.lumue.filescanner")
@EnableAutoConfiguration
public class WebappConfiguration extends WebMvcConfigurerAdapter {

	public WebappConfiguration() {
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(jackson2HttpMessageConverter());
	}

	private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter jackson =
				new MappingJackson2HttpMessageConverter();
		ObjectMapper om = jackson.getObjectMapper();
		JsonSerializer<?> streamSer = new StdSerializer<Stream<?>>(Stream.class, true) {
			@Override public void serialize(
					Stream<?> stream, JsonGenerator jgen, SerializerProvider provider
			) throws IOException, JsonGenerationException
			{
				provider.findValueSerializer(Iterator.class, null)
						.serialize(stream.iterator(), jgen, provider);
				stream.close();
			}
		};
		om.registerModule(new SimpleModule("Streams API", Version.unknownVersion(), Lists.newArrayList(streamSer)));
		return jackson;
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
