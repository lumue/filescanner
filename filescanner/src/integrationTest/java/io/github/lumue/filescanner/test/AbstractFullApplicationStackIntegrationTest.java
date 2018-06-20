package io.github.lumue.filescanner.test;

import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by lm on 17.01.16.
 *
 * Base for integration tests . loads the complete application, including http container
 */
@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource("classpath:test.properties")
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractFullApplicationStackIntegrationTest {
}
