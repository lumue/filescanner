package io.github.lumue.filescanner.metadata.repository;

import io.github.lumue.filescanner.metadata.location.Location;
import io.github.lumue.filescanner.metadata.location.LocationRepository;
import io.github.lumue.filescanner.test.repository.AbstractRepositoryIT;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

/**
 * Created by lm on 17.01.16.
 */
public class DocumentRepositoryIT extends AbstractRepositoryIT<Location>{

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void shouldReturnStreamContainingTestentityFromStreamAll() throws Exception {

        //given a document repository containing testentity
        Location testentity = locationRepository.save(createTestentity());

        //executing findDocumentMetadata should contain testentity
        try(Stream<Location> result= locationRepository.findAll().stream()){
            Assert.assertTrue("result should contain testentity",
                    result
                            .filter(documentMetadata -> testentity.equals(documentMetadata))
                            .findFirst()
                            .isPresent());
        }

    }

    @Override
    protected Location createTestentity() {
        return new Location.DocumentMetadataBuilder()
                .setName("testdocument")
                .setUrl("test:/url")
                .createDocumentMetadata();
    }

    @Override
    protected LocationRepository getRepository() {
        return locationRepository;
    }
}