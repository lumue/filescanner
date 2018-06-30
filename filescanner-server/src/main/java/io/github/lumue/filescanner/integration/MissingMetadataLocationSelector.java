package io.github.lumue.filescanner.integration;

import io.github.lumue.filescanner.metadata.location.Location;
import io.github.lumue.filescanner.util.FileNamingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class MissingMetadataLocationSelector implements MessageSelector {
	
	
	private final static Logger LOGGER = LoggerFactory.getLogger(MissingMetadataLocationSelector.class);
	
	
	
	@Override
	public boolean accept(Message<?> message) {
		try {
			Location location= (Location) message.getPayload();
			final String filename = location.getUrl().replaceFirst("file://","");
			
			String infoJsonFilename = FileNamingUtils.getInfoJsonFilename(filename);
			final File infoJsonFile = new File(infoJsonFilename);
			if(infoJsonFile.exists()&&location.getInfoJsonLocation()==null)
				return true;
			
			String nfoFilename = FileNamingUtils.getNfoFilename(filename);
			final File nfoFile = new File(nfoFilename);
			if(nfoFile.exists()&&location.getNfoLocation()==null)
				return true;
			
			
			return false;
		}catch (Throwable t) {
			return false;
		}
	}
}
