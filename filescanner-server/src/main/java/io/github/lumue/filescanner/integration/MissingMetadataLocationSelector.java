package io.github.lumue.filescanner.integration;

import io.github.lumue.filescanner.metadata.location.Location;
import io.github.lumue.filescanner.util.FileNamingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;

@Component
public class MissingMetadataLocationSelector implements MessageSelector {
	
	
	private final static Logger LOGGER = LoggerFactory.getLogger(MissingMetadataLocationSelector.class);
	
	
	
	@Override
	public boolean accept(Message<?> message) {
		Location location= (Location) message.getPayload();
		
		try {
			final String filename = Paths.get(new URI(location.getUrl())).toAbsolutePath().toString();
			LOGGER.debug("looking up metadata for "+filename);
			
			String infoJsonFilename = FileNamingUtils.getInfoJsonFilename(filename);
			LOGGER.debug("looking up infojson metadata for "+filename+" under "+infoJsonFilename);
			final File infoJsonFile = new File(infoJsonFilename);
			if(infoJsonFile.exists()) {
				LOGGER.info("discovered infojson metadata for "+filename+" under "+infoJsonFilename);
				return true;
			}
			
			String nfoFilename = FileNamingUtils.getNfoFilename(filename);
			LOGGER.debug("looking up nfo metadata for "+filename+" under "+nfoFilename);
			final File nfoFile = new File(nfoFilename);
			if(nfoFile.exists()&&location.getNfoLocation()==null){
				LOGGER.info("discovered nfo metadata for "+filename+" under "+nfoFilename);
				return true;
			}
			
			
			
			return false;
		}catch (Throwable t) {
			LOGGER.error("error looking up metadata files for "+message);
			return false;
		}
	}
}
