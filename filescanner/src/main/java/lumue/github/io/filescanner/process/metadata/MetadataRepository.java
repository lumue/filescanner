package lumue.github.io.filescanner.process.metadata;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories("lumue.github.io.filescanner.process.metadata")
interface MetadataRepository extends
		ElasticsearchCrudRepository<FileMetadata, String> {


}
