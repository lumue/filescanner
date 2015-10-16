package io.github.lumue.filescanner.metadata.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import io.github.lumue.filescanner.metadata.core.FileMetadata;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataRepository extends
		ElasticsearchCrudRepository<FileMetadata, String> {

}