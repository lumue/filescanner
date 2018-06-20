package io.github.lumue.filescanner.util;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;


public class FileExtensionUtils {
	
	
	private final static List<String> MOVIE_EXTENSIONS = Arrays.asList("flv", "mp4", "avi");
	
	
	public static boolean isVideoFileExtension(Path path){
		String extension = FilenameUtils.getExtension(path.toString());
		for (String m : MOVIE_EXTENSIONS) {
			if (m.equalsIgnoreCase(extension))
				return true;
		}
		return false;
	}
	
}
