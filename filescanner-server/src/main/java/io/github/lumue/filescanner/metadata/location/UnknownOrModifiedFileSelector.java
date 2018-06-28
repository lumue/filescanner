package io.github.lumue.filescanner.metadata.location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class UnknownOrModifiedFileSelector implements MessageSelector {
	
	private final LocationService locationService;
	
	private final static Logger LOGGER = LoggerFactory.getLogger(UnknownOrModifiedFileSelector.class);
	
	@Autowired
	public UnknownOrModifiedFileSelector(LocationService locationService) {
		this.locationService = locationService;
	}
	
	@Override
	public boolean accept(Message<?> message) {
		
		File file = (File) message.getPayload();
		
		if (locationService.isLocationCurrent(file)) {
			LOGGER.debug("data for file " + file.toURI().toString() + " is up to date. message discarded");
			return false;
		}
		
		LOGGER.debug("data for file " + file.toURI().toString() + " does not exist or is outdated. message accepted");
		return true;
		
	}
}
