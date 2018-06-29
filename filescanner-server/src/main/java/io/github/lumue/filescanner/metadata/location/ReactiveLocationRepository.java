package io.github.lumue.filescanner.metadata.location;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactiveLocationRepository extends ReactiveMongoRepository<Location, String> {

}