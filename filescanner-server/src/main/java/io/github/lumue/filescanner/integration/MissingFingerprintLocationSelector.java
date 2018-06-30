package io.github.lumue.filescanner.integration;

import io.github.lumue.filescanner.metadata.location.Location;
import io.github.lumue.filescanner.util.FileNamingUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class MissingFingerprintLocationSelector implements MessageSelector {
	private final static Logger LOGGER = LoggerFactory.getLogger(MissingFingerprintLocationSelector.class);
	
	
	
	@Override
	public boolean accept(Message<?> message) {
		try {
			Location location= (Location) message.getPayload();
			return StringUtils.isEmpty(location.getHash());
		}catch (Throwable t) {
			return false;
		}
	}
}
