package io.github.lumue.filescanner.metadata.repository;

import io.github.lumue.filescanner.metadata.core.DocumentMetadata;
import io.github.lumue.filescanner.test.repository.AbstractRepositoryIT;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

/**
 * Created by lm on 17.01.16.
 */
public class DocumentRepositoryIT extends AbstractRepositoryIT<DocumentMetadata>{

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    public void shouldReturnStreamContainingTestentityFromStreamAll() throws Exception {

        //given a document repository containing testentity
        DocumentMetadata testentity = documentRepository.save(createTestentity());

        //executing findDocumentMetadata should contain testentity
        try(Stream<DocumentMetadata> result=documentRepository.findDocumentMetadata()){
            Assert.assertTrue("result should contain testentity",
                    result
                            .filter(documentMetadata -> testentity.equals(documentMetadata))
                            .findFirst()
                            .isPresent());
        }

    }

    @Override
    protected DocumentMetadata createTestentity() {
        return new DocumentMetadata.DocumentMetadataBuilder()
                .setName("testdocument")
                .setUrl("test:/url")
                .createDocumentMetadata();
    }

    @Override
    protected DocumentRepository getRepository() {
        return documentRepository;
    }
}