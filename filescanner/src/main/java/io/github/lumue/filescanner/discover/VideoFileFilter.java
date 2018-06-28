package io.github.lumue.filescanner.discover;

import io.github.lumue.filescanner.util.FileExtensionUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.springframework.integration.file.filters.AbstractFileListFilter;

import java.io.File;

public class VideoFileFilter extends AbstractFileListFilter<File> {
	
	private final IOFileFilter hiddenFilter=HiddenFileFilter.VISIBLE;
	
	@Override
	public boolean accept(File file) {
		return hiddenFilter.accept(file) &&FileExtensionUtils.isVideoFileExtension(file.toPath());
	}
}
