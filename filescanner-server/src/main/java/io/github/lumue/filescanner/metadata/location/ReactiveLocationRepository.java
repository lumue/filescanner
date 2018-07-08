package io.github.lumue.filescanner.metadata.location;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ReactiveLocationRepository extends ReactiveMongoRepository<Location, String> {
	

}