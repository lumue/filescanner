package io.github.lumue.filescanner.config;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lm on 09.12.15.
 */
@Repository
public interface ManagedPathRepository extends MongoRepository<ManagedPath, String> {

}