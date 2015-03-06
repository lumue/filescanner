package lumue.github.io.filescanner;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;


public interface MetadataRepository extends ElasticsearchCrudRepository<FileMetadata, String> {


}
