package io.github.lumue.filescanner.metadata.repository;


import io.github.lumue.filescanner.metadata.core.DocumentMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface DocumentRepository extends MongoRepository<DocumentMetadata, String> {

}