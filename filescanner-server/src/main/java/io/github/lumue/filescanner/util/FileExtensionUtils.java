package io.github.lumue.filescanner.util;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class FileExtensionUtils {
	
	
	private final static Set<String> MOVIE_EXTENSIONS = new HashSet<>(Arrays.asList("flv", "mp4", "avi"));
	
	
	public static boolean isVideoFileExtension(Path path){
		final String filename = path.toString();
		return MOVIE_EXTENSIONS.contains(FilenameUtils.getExtension(filename).toLowerCase());
	}
	
	public static boolean isVideoFileExtension(File file){
		final String filename = file.getName();
		return MOVIE_EXTENSIONS.contains(FilenameUtils.getExtension(filename).toLowerCase());
	}
	
}
