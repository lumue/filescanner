package io.github.lumue.filescanner.test.repository;

import io.github.lumue.filescanner.test.AbstractFullApplicationStackIntegrationTest;
import org.junit.Test;
import org.springframework.data.repository.PagingAndSortingRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by lm on 17.01.16.
 */
public abstract class AbstractRepositoryIT<T> extends AbstractFullApplicationStackIntegrationTest {
    @Test
    public void testSaveAndFindAll() throws Exception {
        T testentity = createTestentity();
        getRepository().save(testentity);
        Iterable<T> pathList = getRepository().findAll();
        assertTrue("findAll result should not be null",pathList!=null);
        assertTrue("findAll result should not be empty",pathList.iterator().hasNext());

    }

    protected abstract T createTestentity();

    @Test
    public void testSaveAndGet() throws Exception {
        T testentity = createTestentity();
        getRepository().save(testentity);
        T returnedEntity = getRepository().findOne("testname");
        assertTrue("result should not be null",returnedEntity!=null);
        assertEquals("returned path should equal testpath ",testentity,returnedEntity);
    }

    protected abstract PagingAndSortingRepository<T,String> getRepository();
}
