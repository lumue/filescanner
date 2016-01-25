package io.github.lumue.filescanner.metadata.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import io.github.lumue.filescanner.metadata.core.DocumentMetadata;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface DocumentRepository extends ElasticsearchCrudRepository<DocumentMetadata, String> {

    Stream<DocumentMetadata> findDocumentMetadata();
}