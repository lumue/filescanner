package io.github.lumue.filescanner.discover;

import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.FileListFilter;

import java.io.File;
import java.util.List;

public class IncomingFilesFilter implements FileListFilter<File> {
	
	private final static CompositeFileListFilter<File> delegate=new CompositeFileListFilter<>();
	
	public IncomingFilesFilter() {
		delegate.addFilter(new AcceptOnceFileListFilter<>(20000));
		delegate.addFilter(new VideoFileFilter());
	}
	
	@Override
	public List<File> filterFiles(File[] files) {
		return delegate.filterFiles(files);
	}
}
