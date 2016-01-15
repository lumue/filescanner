package io.github.lumue.filescanner.path.repository;

import io.github.lumue.filescanner.path.TestConfiguration;
import io.github.lumue.filescanner.path.management.ManagedPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by lm on 09.12.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
@IntegrationTest
public class ManagedPathRepositoryIT {

    @Autowired
    private ManagedPathRepository repository;



    @Test
    public void testSaveAndFindAll() throws Exception {
        ManagedPath path = createTestpath();
        repository.save(path);
        Iterable<ManagedPath> pathList = repository.findAll();
        assertTrue("findAll result should not be null",pathList!=null);
        assertTrue("findAll result should not be empty",pathList.iterator().hasNext());

    }

    private ManagedPath createTestpath() {
        ManagedPath path = new ManagedPath("/testpath", "testname");
        return path;
    }

    @Test
    public void testSaveAndGet() throws Exception {
        ManagedPath path = createTestpath();
        repository.save(path);
        ManagedPath returnedPath = repository.findOne("testname");
        assertTrue("result should not be null",returnedPath!=null);
        assertEquals("returned path should equal testpath ",path,returnedPath);
    }


}
