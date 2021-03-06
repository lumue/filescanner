package io.github.lumue.filescanner.integration;

import io.github.lumue.filescanner.metadata.location.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.File;

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
		try {
			File file = (File) message.getPayload();
			
			if (locationService.isLocationCurrent(file)) {
				LOGGER.debug("data for file " + file.toURI().toString() + " is up to date. message discarded");
				return false;
			}
			
			LOGGER.debug("data for file " + file.toURI().toString() + " does not exist or is outdated. message accepted");
			return true;
		}catch (Throwable t) {
			LOGGER.error("error filtering "+message,t);
			return false;
		}
	}
}
