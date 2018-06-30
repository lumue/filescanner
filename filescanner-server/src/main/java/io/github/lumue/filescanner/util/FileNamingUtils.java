package io.github.lumue.filescanner.util;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class FileNamingUtils {
	
	
	private final static Set<String> MOVIE_EXTENSIONS = new HashSet<>(Arrays.asList("flv", "mp4", "avi"));
	private final static Set<String> METADATA_EXTENSIONS = new HashSet<>(Arrays.asList( "nfo", "info.json"));
	
	public static boolean isVideoFileExtension(Path path){
		final String filename = path.toString();
		return MOVIE_EXTENSIONS.contains(FilenameUtils.getExtension(filename).toLowerCase());
	}
	
	public static boolean isVideoFileExtension(File file){
		final String filename = file.getName();
		return MOVIE_EXTENSIONS.contains(FilenameUtils.getExtension(filename).toLowerCase());
	}
	
	public static String getInfoJsonFilename(String filename){
		String baseName = FilenameUtils.getBaseName(filename);
		String infoJsonFilename = FilenameUtils.getFullPath(filename) + baseName + ".info.json";
		return  infoJsonFilename;
	}
	
	public static String getNfoFilename(String filename){
		String baseName = FilenameUtils.getBaseName(filename);
		String infoJsonFilename = FilenameUtils.getFullPath(filename) + baseName + ".nfo";
		return  infoJsonFilename;
	}
	
	public static boolean isMetadataFileExtension(String filename) {
		return METADATA_EXTENSIONS.contains(FilenameUtils.getExtension(filename).toLowerCase());
	}
}
