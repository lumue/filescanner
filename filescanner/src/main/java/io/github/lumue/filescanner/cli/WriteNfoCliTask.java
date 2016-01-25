package io.github.lumue.filescanner.cli;

import io.github.lumue.filescanner.path.core.FilesystemScanTask;
import io.github.lumue.nfotools.Movie;
import io.github.lumue.nfotools.NfoMovieSerializer;
import org.apache.commons.io.FilenameUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by lm on 22.01.16.
 */
public class WriteNfoCliTask implements CliTask{

    private final String path;
    private final NfoMovieSerializer movieSerializer;

    private final static List<String> MOVIE_EXTENSIONS=Arrays.asList("flv","mp4","avi");

    public WriteNfoCliTask(String path) throws JAXBException {
        this.path= Objects.requireNonNull(path);
        JAXBContext jaxbContext=JAXBContext.newInstance(Movie.class,Movie.Actor.class);
        this.movieSerializer =new NfoMovieSerializer(jaxbContext);
    }

    @Override
    public void execute() throws Exception {
        FilesystemScanTask filesystemScanTask = new FilesystemScanTask(path, file -> {
            try {
                String filename = file.toString();
                String extension = FilenameUtils.getExtension(filename);
                if (isVideoFileExtension(extension)) {
                    String baseName = FilenameUtils.getBaseName(filename);
                    Movie movie = Movie.builder().withTitle(baseName).build();
                    String nfoFilename = FilenameUtils.getFullPath(filename) + baseName + ".nfo";
                    OutputStream outputStream = new FileOutputStream(nfoFilename);
                    movieSerializer.serialize(movie, outputStream);
                    outputStream.close();
                    System.out.println(nfoFilename+" created");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        filesystemScanTask.run();
    }

    private boolean isVideoFileExtension(String extension) {
        for(String m:MOVIE_EXTENSIONS){
            if(m.equalsIgnoreCase(extension))
                return true;
        };
        return false;
    }
}
