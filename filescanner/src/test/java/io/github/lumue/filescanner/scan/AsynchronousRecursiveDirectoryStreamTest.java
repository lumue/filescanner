package io.github.lumue.filescanner.scan;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.github.lumue.filescanner.test.FiletreeMaker;

public class AsynchronousRecursiveDirectoryStreamTest {

	private final FiletreeMaker filetreeMaker = new FiletreeMaker((short) 5,
			(short) 5, (short) 50);

	@Before
	public void setUp() throws Exception {
		filetreeMaker.writeTree("testtree");
	}

	@After
	public void tearDown() throws Exception {
		filetreeMaker.deleteTree("testtree");
	}

	@Test
	public void testIterate() {
		fail("Not yet implemented");
	}

}
