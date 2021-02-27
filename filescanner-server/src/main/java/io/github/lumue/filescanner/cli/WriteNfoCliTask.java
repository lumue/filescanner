package io.github.lumue.filescanner.cli;


import io.github.lumue.filescanner.discover.FilesystemScanTask;
import io.github.lumue.filescanner.metadata.nfo.NfoWriterFileHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.util.Objects;

/**
 * Created by lm on 22.01.16.
 */
public class WriteNfoCliTask implements CliTask {

private final String path;
private final boolean overwriteExistingNfo;


private final static Logger LOGGER = LoggerFactory.getLogger(WriteNfoCliTask.class);

public WriteNfoCliTask(String path, final boolean overwriteExistingNfo) throws JAXBException {
	this.path = Objects.requireNonNull(path);
	this.overwriteExistingNfo = overwriteExistingNfo;

}

@Override
public void execute() throws Exception {
	FilesystemScanTask filesystemScanTask = new FilesystemScanTask(path, new NfoWriterFileHandler(overwriteExistingNfo));
	filesystemScanTask.run();
}


}
