package io.github.lumue.filescanner.metadata.content;


import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ReactiveContentRepository extends ReactiveMongoRepository<Content, String> {
	@Query("{\"locations.1\":{$exists:1}}")
	Flux<Content> findWithSecondaryLocations();
}