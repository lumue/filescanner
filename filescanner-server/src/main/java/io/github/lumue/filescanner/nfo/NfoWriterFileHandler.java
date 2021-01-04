package io.github.lumue.filescanner.nfo;

import io.github.lumue.filescanner.discover.FileHandler;
import io.github.lumue.filescanner.util.FileNamingUtils;
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

public class NfoWriterFileHandler implements FileHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(NfoWriterFileHandler.class);

  private final boolean overwriteExistingNfo;

  private final NfoMovieSerializer movieSerializer;

  public NfoWriterFileHandler(final boolean overwriteExistingNfo) throws JAXBException {
    this.overwriteExistingNfo = overwriteExistingNfo;
    JAXBContext jaxbContext = JAXBContext.newInstance(Movie.class, Movie.Actor.class);
    this.movieSerializer = new NfoMovieSerializer(jaxbContext);
  }

  @Override
  public void handleFile(final File file) {
    try {
      String filename = file.toString();
      String extension = FilenameUtils.getExtension(filename);
      if (FileNamingUtils.isVideoFileExtension(file)) {

        if (!overwriteExistingNfo && nfoMetadataFileExists(filename)) {
          LOGGER.warn("nfo file already exists for" + filename);
          return;
        }

        Movie.MovieBuilder movieBuilder=Movie.builder();

        configureFromMediafile(file,movieBuilder);

        File infoJsonLocation = resolveInfoJsonPath(file).toFile();
        configureFromInfoJson(infoJsonLocation, movieBuilder);

        writeNfoFile(movieBuilder.build(), filename);
      }
    } catch (Exception e) {
      LOGGER.error("error processing file " + file, e);
    }
  }

  private Path resolveInfoJsonPath(final File file) {
    String fileName = file.getName();
    return Paths.get(FileNamingUtils.getInfoJsonFilename(fileName));
  }

  private void writeNfoFile( final Movie movie, final String mediaFileName)
      throws JAXBException, IOException {
    String nfoFilename = FilenameUtils.getFullPath(mediaFileName) +FilenameUtils.getBaseName(mediaFileName) + ".nfo";
    OutputStream outputStream = new FileOutputStream(nfoFilename);
    movieSerializer.serialize(movie, outputStream);
    outputStream.close();
    LOGGER.debug(nfoFilename + " created");
  }

  private Movie.MovieBuilder configureFromMediafile(final File file, final Movie.MovieBuilder movieBuilder)
      throws IOException {

    if(!file.exists()) {
      LOGGER.warn("can no longer access " + file);
      return movieBuilder;
    }

    final MediaFileMovieMetadataSource mediaFileMovieMetadataSource=new MediaFileMovieMetadataSource(file);
    return mediaFileMovieMetadataSource.configureNfoMovieBuilder(movieBuilder);
  }

  private Movie.MovieBuilder configureFromInfoJson(File infoJsonPath, Movie.MovieBuilder movieBuilder) {

    if(!infoJsonPath.exists()) {
      LOGGER.warn("no youtube-dl metadatafile found for " + infoJsonPath);
      return movieBuilder;
    }

    final InfoJsonMovieMetadataSource infoJsonMovieMetadataSource = new InfoJsonMovieMetadataSource(infoJsonPath);
    return infoJsonMovieMetadataSource.configureNfoMovieBuilder( movieBuilder);
  }

  private boolean nfoMetadataFileExists(final String filename) {
    return Files.exists(Paths.get(FileNamingUtils.getNfoFilename(filename)));
  }






}
