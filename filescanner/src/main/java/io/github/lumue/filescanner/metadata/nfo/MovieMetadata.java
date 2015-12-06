package io.github.lumue.filescanner.metadata.nfo;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lm on 06.12.15.
 */
@XmlRootElement(name = "movie")
@XmlAccessorType(XmlAccessType.FIELD)
public class MovieMetadata {

    @XmlElement
    private String title;
    @XmlElement
    private String originaltitle;
    @XmlElement
    private String sorttitle;
    @XmlElement
    private String set;
    @XmlElement
    private String rating;
    @XmlElement
    private String year;
    @XmlElement
    private String top250;
    @XmlElement
    private String votes;
    @XmlElement
    private String outline;
    @XmlElement
    private String plot;
    @XmlElement
    private String tagline;
    @XmlElement
    private String runtime;
    @XmlElement
    private String thumb;
    @XmlElement
    private String mpaa;
    @XmlElement
    private String playcount;
    @XmlElement
    private String id;
    @XmlElement
    private String filenameandpath;
    @XmlElement
    private String trailer;
    @XmlElement
    private String genre;
    @XmlElement
    private String credits;
    @XmlElement
    private FileinfoMetadata fileinfo;
    @XmlElement
    private String director;
    @XmlElement
    private List<ActorMetadata> actorList;

    public MovieMetadata(String title, String originaltitle, String sorttitle, String set, String rating, String year, String top250, String votes, String outline, String plot, String tagline, String runtime, String thumb, String mpaa, String playcount, String id, String filenameandpath, String trailer, String genre, String credits, FileinfoMetadata fileinfo, String director, List<ActorMetadata> actorList) {
        this.title = title;
        this.originaltitle = originaltitle;
        this.sorttitle = sorttitle;
        this.set = set;
        this.rating = rating;
        this.year = year;
        this.top250 = top250;
        this.votes = votes;
        this.outline = outline;
        this.plot = plot;
        this.tagline = tagline;
        this.runtime = runtime;
        this.thumb = thumb;
        this.mpaa = mpaa;
        this.playcount = playcount;
        this.id = id;
        this.filenameandpath = filenameandpath;
        this.trailer = trailer;
        this.genre = genre;
        this.credits = credits;
        this.fileinfo = fileinfo;
        this.director = director;
        this.actorList = actorList;
    }

    public static MovieMetadataBuilder builder(){
        return new MovieMetadataBuilder();
    };

    public static class MovieMetadataBuilder {
        private String title;
        private String originaltitle;
        private String sorttitle;
        private String set;
        private String rating;
        private String year;
        private String top250;
        private String votes;
        private String outline;
        private String plot;
        private String tagline;
        private String runtime;
        private String thumb;
        private String mpaa;
        private String playcount;
        private String id;
        private String filenameandpath;
        private String trailer;
        private String genre;
        private String credits;
        private FileinfoMetadata fileinfo;
        private String director;
        private final List<ActorMetadata> actorList=new ArrayList<>();

        public MovieMetadataBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public MovieMetadataBuilder withOriginaltitle(String originaltitle) {
            this.originaltitle = originaltitle;
            return this;
        }

        public MovieMetadataBuilder withSorttitle(String sorttitle) {
            this.sorttitle = sorttitle;
            return this;
        }

        public MovieMetadataBuilder withSet(String set) {
            this.set = set;
            return this;
        }

        public MovieMetadataBuilder withRating(String rating) {
            this.rating = rating;
            return this;
        }

        public MovieMetadataBuilder withYear(String year) {
            this.year = year;
            return this;
        }

        public MovieMetadataBuilder withTop250(String top250) {
            this.top250 = top250;
            return this;
        }

        public MovieMetadataBuilder withVotes(String votes) {
            this.votes = votes;
            return this;
        }

        public MovieMetadataBuilder withOutline(String outline) {
            this.outline = outline;
            return this;
        }

        public MovieMetadataBuilder withPlot(String plot) {
            this.plot = plot;
            return this;
        }

        public MovieMetadataBuilder withTagline(String tagline) {
            this.tagline = tagline;
            return this;
        }

        public MovieMetadataBuilder withRuntime(String runtime) {
            this.runtime = runtime;
            return this;
        }

        public MovieMetadataBuilder withThumb(String thumb) {
            this.thumb = thumb;
            return this;
        }

        public MovieMetadataBuilder withMpaa(String mpaa) {
            this.mpaa = mpaa;
            return this;
        }

        public MovieMetadataBuilder withPlaycount(String playcount) {
            this.playcount = playcount;
            return this;
        }

        public MovieMetadataBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public MovieMetadataBuilder withFilenameandpath(String filenameandpath) {
            this.filenameandpath = filenameandpath;
            return this;
        }

        public MovieMetadataBuilder withTrailer(String trailer) {
            this.trailer = trailer;
            return this;
        }

        public MovieMetadataBuilder withGenre(String genre) {
            this.genre = genre;
            return this;
        }

        public MovieMetadataBuilder withCredits(String credits) {
            this.credits = credits;
            return this;
        }


        public MovieMetadataBuilder withFileinfo(FileinfoMetadata fileinfo) {
            this.fileinfo = fileinfo;
            return this;
        }

        public MovieMetadataBuilder withDirector(String director) {
            this.director = director;
            return this;
        }

        public MovieMetadataBuilder addActor(ActorMetadata actor) {
            this.actorList.add(actor);
            return this;
        }

        public MovieMetadata build() {
            return new MovieMetadata(title, originaltitle, sorttitle, set, rating, year, top250, votes, outline, plot, tagline, runtime, thumb, mpaa, playcount, id, filenameandpath, trailer, genre, credits,  fileinfo, director, actorList);
        }
    }
}
