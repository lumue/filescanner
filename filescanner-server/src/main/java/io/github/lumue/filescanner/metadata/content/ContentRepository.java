package io.github.lumue.filescanner.metadata.content;


import io.github.lumue.filescanner.metadata.location.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends MongoRepository<Content, String> {

}