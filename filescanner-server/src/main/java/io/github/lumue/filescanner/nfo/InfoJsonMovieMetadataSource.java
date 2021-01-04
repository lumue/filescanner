package io.github.lumue.filescanner.nfo;

import io.github.lumue.filescanner.util.FileAttributeUtils;
import io.github.lumue.infojson.DownloadMetadata;
import io.github.lumue.infojson.DownloadMetadataStreamParser;
import io.github.lumue.nfotools.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class InfoJsonMovieMetadataSource implements NfoMovieMetadataUpdater {

  private final static Logger LOGGER= LoggerFactory.getLogger(InfoJsonMovieMetadataSource.class);

  private final File file;

  public InfoJsonMovieMetadataSource(File infoJsonFile) {
    this.file = infoJsonFile;
  }

  @Override
  public Movie.MovieBuilder configureNfoMovieBuilder(Movie.MovieBuilder movieBuilder)
  {
    DownloadMetadata downloadMetadata;
    downloadMetadata = readDownloadMetadata();

    final String uploadDateAsString = downloadMetadata.getUploadDate();
    if (uploadDateAsString != null && !uploadDateAsString.isEmpty()) {
      LocalDateTime uploadDate;
      int year = Integer.parseInt(uploadDateAsString.substring(0, 4));
      int month = Integer.parseInt(uploadDateAsString.substring(4, 6));
      int day = Integer.parseInt(uploadDateAsString.substring(6, 8));
      uploadDate = LocalDateTime.of(year, month, day, 0, 0, 0);
      movieBuilder.withYear(Long.toString(uploadDate.getYear()));
      movieBuilder.withAired(uploadDate);
    }
    try {
      final LocalDateTime downloadDate= FileAttributeUtils.readCreationTime(file);
      movieBuilder.withDateAdded(downloadDate);
    } catch (IOException ioException) {
      LOGGER.error("error getting creation time of "+file,ioException);
    }

    final String title = downloadMetadata.getTitle() != null ? downloadMetadata.getTitle() : "";
    movieBuilder.withTitle(title);
    movieBuilder.withTag(downloadMetadata.getExtractor());
    movieBuilder.withVotes(String.valueOf(downloadMetadata.getLikeCount()));
    final String description = downloadMetadata.getDescription() != null ? downloadMetadata.getDescription() : "";
    movieBuilder.withTagline(description);
    extractTagsFromInfojson(downloadMetadata).forEach(movieBuilder::withTag);
    return movieBuilder;
  }

  private DownloadMetadata readDownloadMetadata() {
    DownloadMetadata downloadMetadata;
    try {
      InputStream inputStream = new FileInputStream(file);
      DownloadMetadataStreamParser parser = new DownloadMetadataStreamParser();
      downloadMetadata = parser.apply(inputStream);
      inputStream.close();
    }
    catch(IOException ioException) {
      throw new MetadataSourceAccessError("error accessing "+file,ioException);
    }
    return downloadMetadata;
  }

  private Set<String> extractTagsFromInfojson(DownloadMetadata downloadMetadata) {
    Set<String> tagset = new HashSet<>();
    Set<String> candidates = new HashSet<>();
    Optional.ofNullable(downloadMetadata.getTags()).ifPresent(candidates::addAll);
    Optional.ofNullable(downloadMetadata.getCategories()).ifPresent(candidates::addAll);
    Optional.ofNullable(downloadMetadata.getUploader()).ifPresent(candidates::add);
    candidates.stream()
        .filter(Objects::nonNull)
        .forEach(tagset::add);
    return tagset;
  }
}