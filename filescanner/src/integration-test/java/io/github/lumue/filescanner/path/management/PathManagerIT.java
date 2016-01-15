package io.github.lumue.filescanner.path.management;

import io.github.lumue.filescanner.path.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by lm on 11.12.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
@IntegrationTest
public class PathManagerIT {

    @Autowired
    private PathManager pathManager;

    @Test
    public void testAddPath() throws Exception {

    }

    @Test
    public void testGetList() throws Exception {

    }
}
