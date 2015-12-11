package io.github.lumue.filescanner.metadata.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import io.github.lumue.filescanner.metadata.core.DocumentMetadata;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataRepository extends ElasticsearchCrudRepository<DocumentMetadata, String> {

}