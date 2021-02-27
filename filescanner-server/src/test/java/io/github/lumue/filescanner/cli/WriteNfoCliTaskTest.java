package io.github.lumue.filescanner.cli;

import org.junit.Test;

/**
 * Created by lm on 23.04.16.
 */
public class WriteNfoCliTaskTest {
	@Test
	public void execute() throws Exception {
		WriteNfoCliTask task = new WriteNfoCliTask("/mnt/vm-mediafiles/video/adult", false);
		task.execute();
	}

}