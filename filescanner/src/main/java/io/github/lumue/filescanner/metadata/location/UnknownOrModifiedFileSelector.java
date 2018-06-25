package io.github.lumue.filescanner.metadata.location;

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
	
	@Autowired
	public UnknownOrModifiedFileSelector(LocationService locationService) {
		this.locationService = locationService;
	}
	
	@Override
	public boolean accept(Message<?> message) {
		File file = (File) message.getPayload();
		
		if(!file.toURI().toString().endsWith("mp4"))
			return false;
		
		try {
			FileMetadataAccessor fileMetadataAccessor = new FileMetadataAccessor(file.toPath());
			final LocalDateTime lastScan = locationService.getForURL(file.toURI().toString())
					.map(Location::getLastScanTime)
					.orElse(LocalDateTime.MIN);
			
			return !lastScan.isAfter(fileMetadataAccessor.getModificationTime());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}
}
