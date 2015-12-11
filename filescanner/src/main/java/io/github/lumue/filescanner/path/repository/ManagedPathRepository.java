package io.github.lumue.filescanner.path.repository;


import io.github.lumue.filescanner.path.management.ManagedPath;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lm on 09.12.15.
 */
@Repository
public interface ManagedPathRepository extends ElasticsearchCrudRepository<ManagedPath, String> {

}