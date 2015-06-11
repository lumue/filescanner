package io.github.lumue.filescanner.metadata.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import io.github.lumue.filescanner.metadata.core.FileMetadata;

public interface MetadataRepository extends
		ElasticsearchCrudRepository<FileMetadata, String> {

}
