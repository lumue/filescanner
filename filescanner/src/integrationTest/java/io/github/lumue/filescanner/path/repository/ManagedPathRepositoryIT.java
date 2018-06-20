package io.github.lumue.filescanner.path.repository;

import io.github.lumue.filescanner.path.management.ManagedPath;
import io.github.lumue.filescanner.test.repository.AbstractRepositoryIT;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lm on 09.12.15.
 */
public class ManagedPathRepositoryIT extends AbstractRepositoryIT<ManagedPath> {


    @Autowired
    private ManagedPathRepository repository;


    @Override
    protected ManagedPath createTestentity() {
        ManagedPath path = new ManagedPath("/testpath", "testname");
        return path;
    }

    @Override
    protected ManagedPathRepository getRepository() {
        return repository;
    }

}
