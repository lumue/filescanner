package io.github.lumue.filescanner.metadata.content;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

/**
 * Access additional Document Metadata with apache tika (http://tika.apache.org)
 *
 * Created by lm on 16.10.15.
 */
public class TikaMetadataAccessor {


	private final Metadata tikaMetadata;

	public TikaMetadataAccessor(Path path) throws Exception {
		tikaMetadata=parse(path);
	}

	private static Metadata parse(Path path) throws Exception {
		InputStream stream = new FileInputStream(path.toFile());
		AutoDetectParser parser = new AutoDetectParser();
		BodyContentHandler handler = new BodyContentHandler(-1);
		Metadata metadata = new Metadata();
		try {
			parser.parse(stream,handler,metadata);
			return metadata;
		} finally {
			stream.close();
		}
	}


	public String getProperty(String key) {
		return  tikaMetadata.get(key);
	}

	
	public Collection<String> getPropertyKeys() {
		return Arrays.asList(tikaMetadata.names());
	}
}
