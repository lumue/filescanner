package io.github.lumue.filescanner.metadata.nfo;

import io.github.lumue.nfotools.Movie;

public interface NfoMovieMetadataUpdater {
  Movie.MovieBuilder configureNfoMovieBuilder(Movie.MovieBuilder movieBuilder);
}
