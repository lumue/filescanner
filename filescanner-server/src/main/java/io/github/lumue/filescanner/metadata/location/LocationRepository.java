package io.github.lumue.filescanner.metadata.location;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends MongoRepository<Location, String> {
	
	List<Location> findByHash(String hash);
}