package io.github.lumue.filescanner.metadata.nfo;

import io.github.lumue.filescanner.util.FileAttributeUtils;
import io.github.lumue.nfotools.Movie;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class MediaFileMovieMetadataSource implements NfoMovieMetadataUpdater {
  private final File file;

  public MediaFileMovieMetadataSource(File file) {
    this.file = file;
  }

  @Override
  public Movie.MovieBuilder configureNfoMovieBuilder(final Movie.MovieBuilder movieBuilder) {

    try {

      if (!file.exists())
        return movieBuilder;

      LocalDateTime uploadDate = FileAttributeUtils.readCreationTime(file);
      movieBuilder.withYear(Long.toString(uploadDate.getYear()));
      movieBuilder.withAired(uploadDate);
      if (file.getAbsolutePath().contains("adult")) {
        movieBuilder.withGenre("Porn");
      }
      extractTagsFromPath().forEach(movieBuilder::withTag);
      movieBuilder.withTitle(FilenameUtils.getBaseName(file.getName()));
      return movieBuilder;
    }
    catch(IOException ioException){
      throw new MetadataSourceAccessError("error getting metadata from "+file,ioException);
    }
  }

  private Collection<String> extractTagsFromPath() {
    Collection<String> ret = new ArrayList<>();
    final Path parent = file.toPath().getParent();
    int namesCount = parent.getNameCount();
    for (int i = namesCount - 1; i > 0; i--) {
      final String name = parent.getName(i).toString();
      if ("media".equals(name))
        break;
      ret.add(name.toLowerCase());
    }
    return ret;
  }
}