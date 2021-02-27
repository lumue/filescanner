package io.github.lumue.filescanner.metadata.nfo

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.github.lumue.nfotools.Movie
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.time.Duration
import java.time.LocalDateTime


class MetaJsonMovieMetadataSource(val file:File) : NfoMovieMetadataUpdater {
    private val LOGGER = LoggerFactory.getLogger(MetaJsonMovieMetadataSource::class.java)


    override fun configureNfoMovieBuilder(movieBuilder: Movie.MovieBuilder): Movie.MovieBuilder {
        val reader = LocationMetadataReader()
        return try {
            val locationMetadata = reader.read(FileInputStream(file))
            locationMetadata.configureMovieBuilderWithLocationMetadata(movieBuilder)
        } catch (e: FileNotFoundException) {
            LOGGER.error("error", e)
            movieBuilder
        }
    }
}

fun LocationMetadata.configureMovieBuilderWithLocationMetadata(movieBuilder: Movie.MovieBuilder) : Movie.MovieBuilder{
    movieBuilder
            .withAired(contentMetadata.uploaded)
            .withDateAdded(contentMetadata.downloaded)
            .withTitle(contentMetadata.title)
            .withRuntime(contentMetadata.duration.toMinutes().toString())
            .withTag(this.contentMetadata.hoster)
            .withTag(this.contentMetadata.uploaded?.year.toString())
            .withTag(this.contentMetadata.uploaded?.month.toString().toLowerCase())
            .withVotes(this.contentMetadata.votes.toString())
            .withTagline(this.contentMetadata.description)

    this.contentMetadata.tags
            .map { tag ->  tag.name}
            .map{tagstring->tagstring.replace("/tags/","") }
            .map { tagstring->tagstring.replace("/categories/","") }
            .map { tagstring->tagstring.replace("/channels/","") }
            .map { tagstring->tagstring.replace("/","") }
            .forEach{movieBuilder.withTag(it)}
    this.contentMetadata.actors
            .map { actor -> Movie.Actor(actor.name,"") }
            .forEach { actor->movieBuilder.addActor(actor) }

    return movieBuilder
}


const val locationMetadataFileSuffix = ".meta.json"



class LocationMetadataReader(
        val mapper: ObjectMapper = ObjectMapper().registerModule(JavaTimeModule()))
{

    fun read(instream: InputStream): LocationMetadata {
        try {
            return mapper.readValue<LocationMetadata>(instream, LocationMetadata::class.java)
        } catch (t: Throwable) {
            throw RuntimeException(t)
        }
    }
}


val File.isMetadataJson: Boolean
    get() {
        return this.exists()
                && !this.isDirectory
                && this.name.endsWith(locationMetadataFileSuffix)
    }


data class LocationMetadata(@JsonProperty("url") val url: String,
                            @JsonProperty("contentMetadata") val contentMetadata: ContentMetadata,
                            @JsonProperty("downloadMetadata") val downloadMetadata: DownloadMetadata) {

    data class DownloadMetadata(@JsonProperty("selectedStreams") val selectedStreams: List<MediaStreamMetadata>,
                                @JsonProperty("additionalStreams") val additionalStreams: List<MediaStreamMetadata>)


    data class MediaStreamMetadata(@JsonProperty("id") val id: String,
                                   @JsonProperty("url") val url: String,
                                   @JsonProperty("headers") val headers: Map<String, String>,
                                   @JsonProperty("contentType") val contentType: ContentType,
                                   @JsonProperty("codec") val codec: String,
                                   @JsonProperty("filenameExtension") val filenameExtension: String,
                                   @JsonProperty("expectedSize") val expectedSize: Long)


    enum class ContentType { AUDIO, VIDEO, CONTAINER }

    data class ContentMetadata(@JsonProperty("title") val title: String,
                               @JsonProperty("description") val description: String = "",
                               @JsonProperty("tags") val tags: Set<Tag> = setOf(),
                               @JsonProperty("actors") val actors: Set<Actor> = setOf(),
                               @JsonProperty("duration") val duration: Duration = Duration.ZERO,
                               @JsonProperty("views") val views: Int = 0,
                               @JsonProperty("downloaded") val downloaded: LocalDateTime? = LocalDateTime.now(),
                               @JsonProperty("uploaded") val uploaded: LocalDateTime? = LocalDateTime.MIN,
                               @JsonProperty("hoster") val hoster: String? = "unkonown",
                               @JsonProperty("votes") val votes: Int? = 0

    ) {


        data class Tag(@JsonProperty("id") val id: String, @JsonProperty("name") val name: String)
        data class Actor(@JsonProperty("id") val id: String, @JsonProperty("name") val name: String)
    }




}
