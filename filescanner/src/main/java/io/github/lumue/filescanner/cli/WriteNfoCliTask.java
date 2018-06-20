package io.github.lumue.filescanner.cli;

import io.github.lumue.filescanner.metadata.Tags;
import io.github.lumue.filescanner.path.core.FilesystemScanTask;
import io.github.lumue.infojson.DownloadMetadata;
import io.github.lumue.infojson.DownloadMetadataStreamParser;
import io.github.lumue.nfotools.Movie;
import io.github.lumue.nfotools.NfoMovieSerializer;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * Created by lm on 22.01.16.
 */
public class WriteNfoCliTask implements CliTask {

private final String path;
private final NfoMovieSerializer movieSerializer;

private final Tags tags = new Tags();

private int run=0;

private final static Logger LOGGER = LoggerFactory.getLogger(WriteNfoCliTask.class);

private final static List<String> MOVIE_EXTENSIONS = Arrays.asList("flv", "mp4", "avi");

public WriteNfoCliTask(String path) throws JAXBException {
	this.path = Objects.requireNonNull(path);
	JAXBContext jaxbContext = JAXBContext.newInstance(Movie.class, Movie.Actor.class);
	this.movieSerializer = new NfoMovieSerializer(jaxbContext);
}

@Override
public void execute() throws Exception {
	FilesystemScanTask filesystemScanTask = new FilesystemScanTask(path, file -> {
		try {
			String filename = file.toString();
			String extension = FilenameUtils.getExtension(filename);
			if (isVideoFileExtension(extension)) {
				String baseName = FilenameUtils.getBaseName(filename);
				Movie.MovieBuilder movieBuilder = Movie.builder().withTitle(baseName);
				String infoJsonFilename = FilenameUtils.getFullPath(filename) + baseName + ".info.json";
				if (Files.exists(Paths.get(infoJsonFilename))) {
					configureMovieBuilderWithInfoJson(infoJsonFilename, movieBuilder);
				}
				else{
					tags.stream()
							.filter(t->filename.toLowerCase().contains(t))
							.forEach(movieBuilder::withTag);
				}
				if (filename.contains("adult"))
					movieBuilder.withGenre("Porn");
				if (filename.contains("collections")) {
					final String collection = extractCollectionName(infoJsonFilename).toLowerCase();
					movieBuilder.withTag(collection);
					tags.add(collection);
				}
				extractTagsFromPath(file).forEach(tag -> {
					if (tag != null) {
						if(tags.contains(tag))
							movieBuilder.withTag(tag.toLowerCase());
						tags.add(tag);
					}
				});
				String nfoFilename = FilenameUtils.getFullPath(filename) + baseName + ".nfo";
				Movie movie = movieBuilder.build();
				OutputStream outputStream = new FileOutputStream(nfoFilename);
				movieSerializer.serialize(movie, outputStream);
				outputStream.close();
				System.out.println(nfoFilename + " created");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	});
	filesystemScanTask.run();
	tags.removeTagsWithCountLowerThan(10);
	run++;
	filesystemScanTask.run();
}

private Collection<String> extractTagsFromPath(Path file) {
	Collection<String> ret = new ArrayList<>();
	final Path parent = file.getParent();
	int namesCount = parent.getNameCount();
	for (int i = namesCount - 1; i > 0; i--) {
		final String name = parent.getName(i).toString();
		if ("media".equals(name))
			break;
		ret.add(name.toLowerCase());
	}
	return ret;
}

private void configureMovieBuilderWithInfoJson(String infoJsonFilename, Movie.MovieBuilder movieBuilder) throws IOException {
	InputStream inputStream = new FileInputStream(infoJsonFilename);
	DownloadMetadataStreamParser parser = new DownloadMetadataStreamParser();
	DownloadMetadata downloadMetadata = parser.apply(inputStream);
	inputStream.close();
	BasicFileAttributes attr = Files.readAttributes(new File(infoJsonFilename).toPath(), BasicFileAttributes.class);
	final long downloadTimestamp = attr.creationTime().toInstant().getEpochSecond();
	final ZoneOffset offset = ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getOffset(downloadTimestamp * 1000) / 1000);
	LocalDateTime downloadDate = LocalDateTime.ofEpochSecond(downloadTimestamp, 0, offset);
	LocalDateTime uploadDate = null;
	final String uploadDateAsString = downloadMetadata.getUploadDate();
	if (uploadDateAsString != null && !uploadDateAsString.isEmpty()) {
		int year = Integer.parseInt(uploadDateAsString.substring(0, 4));
		int month = Integer.parseInt(uploadDateAsString.substring(4, 6));
		int day = Integer.parseInt(uploadDateAsString.substring(6, 8));
		uploadDate = LocalDateTime.of(year, month, day, 0, 0, 0);
	}
	uploadDate = uploadDate != null ? uploadDate : downloadDate;
	movieBuilder.withAired(uploadDate);
	movieBuilder.withDateAdded(downloadDate);
	
	final String title = downloadMetadata.getTitle()!=null?downloadMetadata.getTitle():"";
	final String fulltitle = downloadMetadata.getFulltitle()!=null?downloadMetadata.getFulltitle():"";
	final String filename=infoJsonFilename
			.replace(".info.json","")
			.trim();
	movieBuilder.withTitle(title);
	movieBuilder.withTag(downloadMetadata.getExtractor());
	movieBuilder.withVotes(String.valueOf(downloadMetadata.getLikeCount()));
	final String description = downloadMetadata.getDescription()!=null?downloadMetadata.getDescription():"";
	movieBuilder.withTagline(description);
	Set<String> tagset = extractTagsFromInfojson(downloadMetadata);
	tags.stream()
			.filter(t ->
				filename.toLowerCase().contains(t)
				|| title.toLowerCase().contains(t)
				|| fulltitle.toLowerCase().contains(t)
				|| description.toLowerCase().contains(t)			)
			.forEach(tagset::add);
	if(run<1)
		tagset.forEach(t->tags.add(t.toLowerCase()));
	else
		tagset.stream().filter(tags::contains).forEach(movieBuilder::withTag);
	
}

private Set<String> extractTagsFromInfojson(DownloadMetadata downloadMetadata) {
	Set<String> tagset = new HashSet<>();
	Set<String> candidates=new HashSet<>();
	Optional.ofNullable(downloadMetadata.getTags()).ifPresent(candidates::addAll);
	Optional.ofNullable(downloadMetadata.getCategories()).ifPresent(candidates::addAll);
	Optional.ofNullable(downloadMetadata.getUploader()).ifPresent(candidates::add);
	candidates.stream()
				.filter(Objects::nonNull)
				.map(this::normalizeTagName)
				.forEach(tagset::add);
	return tagset;
}
	
	private String normalizeTagName(String tag) {
		return tag.toLowerCase().trim().replaceAll("\n", "");
	}
	
	private String extractCollectionName(String infoJsonFilename) {
	String ret = infoJsonFilename.replace(FilenameUtils.getBaseName(infoJsonFilename), "");
	ret = ret.replace(".json", "").replace("/mnt/nasbox/media/adult/collections/", "").replace("/", "");
	return ret;
}

private boolean isVideoFileExtension(String extension) {
	for (String m : MOVIE_EXTENSIONS) {
		if (m.equalsIgnoreCase(extension))
			return true;
	}
	;
	return false;
}
}
