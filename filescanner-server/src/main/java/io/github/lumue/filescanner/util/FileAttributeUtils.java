package io.github.lumue.filescanner.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;

public class FileAttributeUtils {
  public static LocalDateTime readCreationTime(File file) throws IOException {
    BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
    final long lastModified = attr.creationTime().toInstant().getEpochSecond();
    final ZoneOffset offset = ZoneOffset
        .ofTotalSeconds(TimeZone.getDefault().getOffset(lastModified * 1000) / 1000);
    return LocalDateTime.ofEpochSecond(lastModified, 0, offset);
  }
}
