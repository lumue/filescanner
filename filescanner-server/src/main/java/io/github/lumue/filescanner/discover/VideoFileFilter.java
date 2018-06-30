package io.github.lumue.filescanner.discover;

import io.github.lumue.filescanner.util.FileNamingUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.file.filters.AbstractFileListFilter;
import org.springframework.integration.file.filters.FileListFilter;

import java.io.File;
import java.io.FileFilter;

public class VideoFileFilter extends AbstractFileListFilter<File> {
	
	private final FileFilter hiddenFilter=HiddenFileFilter.VISIBLE;
	
	private final FileListFilter<File> unseenOrModifiedFilter=new AcceptUnseenOrModifiedSinceFileFilter<File>(20000);
	
	private final  static Logger LOGGER= LoggerFactory.getLogger(VideoFileFilter.class);
	
	@Override
	public boolean accept(File file) {
		try {
			return hiddenFilter.accept(file) && FileNamingUtils.isVideoFileExtension(file);
		}
		catch (Throwable t){
			LOGGER.error("error filtering "+file,t);
			return false;
		}
	}
}
