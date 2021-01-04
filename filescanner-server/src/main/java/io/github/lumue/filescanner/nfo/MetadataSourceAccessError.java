package io.github.lumue.filescanner.nfo;

import java.io.IOException;

public class MetadataSourceAccessError extends RuntimeException {
  public MetadataSourceAccessError(final String s, final IOException ioException) {
    super(s,ioException);
  }
}
