package io.github.lumue.filescanner.nfo;

import io.github.lumue.nfotools.Movie;

public interface NfoMovieMetadataUpdater {
  Movie.MovieBuilder configureNfoMovieBuilder(Movie.MovieBuilder movieBuilder);
}
