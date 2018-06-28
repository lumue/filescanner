package io.github.lumue.filescanner.webapp;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.google.common.collect.Lists;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.core.publisher.TopicProcessor;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;




@Configuration
@ComponentScan("io.github.lumue.filescanner")
@ImportResource("classpath*:io/github/lumue/filescanner/integrationflow/application-integration-flow.xml")
@EnableAutoConfiguration
@EnableMongoRepositories(basePackages = {"io.github.lumue.filescanner.metadata.location","io.github.lumue.filescanner.config"})
@EnableAspectJAutoProxy
@PropertySource(ignoreResourceNotFound = true, value = "file://${filescanner.properties}")
public class WebappConfiguration implements WebMvcConfigurer {
	
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
	
	
	@Bean public ExecutorService fileEventHandlerExecutor() {
		final ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setThreadNamePrefix("file-event-handler");
		threadPoolTaskExecutor.setThreadGroupName("file-event-handler");
		threadPoolTaskExecutor.setCorePoolSize(10);
		threadPoolTaskExecutor.setMaxPoolSize(20);
		threadPoolTaskExecutor.setQueueCapacity(5000);
		threadPoolTaskExecutor.initialize();
		return threadPoolTaskExecutor.getThreadPoolExecutor();
	}
	
	@Bean public TaskExecutor insertUpdateExecutor() {
		final ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setThreadNamePrefix("insert-update-executor");
		threadPoolTaskExecutor.setThreadGroupName("insert-update-executor");
		threadPoolTaskExecutor.setCorePoolSize(1);
		threadPoolTaskExecutor.setMaxPoolSize(3);
		threadPoolTaskExecutor.setQueueCapacity(50000);
		threadPoolTaskExecutor.initialize();
		return threadPoolTaskExecutor;
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JSR310Module());
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		return objectMapper;
	}
	
	@Bean(name = "filesystemSessionTaskRunner")
	public ThreadPoolTaskExecutor filesystemSessionTaskRunner() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setMaxPoolSize(10);
		threadPoolTaskExecutor.setCorePoolSize(10);
		return threadPoolTaskExecutor;
	}
	
	@Bean
	TimedAspect timedAspect(MeterRegistry registry) {
		return new TimedAspect(registry);
	}
	
}
