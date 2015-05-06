package io.github.lumue.filescanner.scan;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.github.lumue.filescanner.file.AsynchronousRecursiveDirectoryStream;
import io.github.lumue.filescanner.file.FiletreeBuildingTool;

public class AsynchronousRecursiveDirectoryStreamTest {
	private final FiletreeBuildingTool filetreeMaker = new FiletreeBuildingTool((short) 5,
			(short) 5, (short) 50);

	@Before
	public void setUp() throws Exception {
		filetreeMaker.deleteTree("testtree");
		filetreeMaker.writeTree("testtree");
	}

	@After
	public void tearDown() throws Exception {
		filetreeMaker.deleteTree("testtree");
	}

	@Test
	public void testIterate() throws IOException {
		AsynchronousRecursiveDirectoryStream directoryStream = null;
		try {

			final Set<String> foundnodes = new ConcurrentSkipListSet<String>();

			directoryStream = new AsynchronousRecursiveDirectoryStream(
					Paths.get("testtree"));

			directoryStream.forEach(filenode -> {
				if (filenode == null) {
					return;
				}
				foundnodes.add(filenode.getFileName().toString());
			} );

			Assert.assertEquals(filetreeMaker.getCreatednodes().size(),
					foundnodes.size());

		} finally {
			if (directoryStream != null) {
				directoryStream.close();
			}
		}
	}

}
