package io.github.lumue.filescanner.discover;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lm on 09.12.15.
 */
@Repository
public interface ManagedPathRepository extends ReactiveMongoRepository<ManagedPath, String> {
}