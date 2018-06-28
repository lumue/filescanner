package io.github.lumue.filescanner.discover;

import io.github.lumue.filescanner.util.FileExtensionUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.file.filters.AbstractFileListFilter;

import java.io.File;

public class VideoFileFilter extends AbstractFileListFilter<File> {
	
	private final IOFileFilter hiddenFilter=HiddenFileFilter.VISIBLE;
	
	private final  static Logger LOGGER= LoggerFactory.getLogger(VideoFileFilter.class);
	
	@Override
	public boolean accept(File file) {
		try {
			return hiddenFilter.accept(file) && FileExtensionUtils.isVideoFileExtension(file);
		}
		catch (Throwable t){
			LOGGER.error("error filtering "+file,t);
			return false;
		}
	}
}
