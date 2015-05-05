package io.github.lumue.filescanner.process.metadata;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

public interface MetadataRepository extends
		ElasticsearchCrudRepository<FileMetadata, String> {

}
