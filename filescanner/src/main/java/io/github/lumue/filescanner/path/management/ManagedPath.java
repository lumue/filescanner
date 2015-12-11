package io.github.lumue.filescanner.path.management;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 *
 * Represents a manged Path
 *
 * Created by lm on 09.12.15.
 */
@Document(indexName = "filescanner.path", type = "managedPath")
public class ManagedPath {

    @Id
    @JsonProperty("name")
    @Field(type = FieldType.String, store = true)
    private String name;

    @JsonProperty("path")
    @Field(type = FieldType.String, store = true)
    private String path;

    @JsonProperty("startScanOnConnect")
    @Field(type = FieldType.Boolean, store = true)
    private Boolean startScanOnConnect=true;

    @JsonProperty("startScanOnConnect")
    @Field(type = FieldType.Boolean, store = true)
    private Boolean connectOnStartup=true;

    @JsonProperty("lastScanned")
    @Field(type = FieldType.Boolean, store = true)
    private LocalDateTime lastScanned;


    private transient FilesystemSession  session;


    public ManagedPath() {
    }

    public ManagedPath(String path, String name) {
        this.path= requireNonNull(path);
        this.name= requireNonNull(name);
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManagedPath that = (ManagedPath) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path);
    }



    public Boolean getStartScanOnConnect() {
        return startScanOnConnect;
    }

    public void setStartScanOnConnect(Boolean startScanOnConnect) {
        this.startScanOnConnect = startScanOnConnect;
    }

    public Boolean getConnectOnStartup() {
        return connectOnStartup;
    }

    public void setConnectOnStartup(Boolean connectOnStartup) {
        this.connectOnStartup = connectOnStartup;
    }

    public LocalDateTime getLastScanned() {
        return lastScanned;
    }

    public void setLastScanned(LocalDateTime lastScanned) {
        this.lastScanned = lastScanned;
    }

    public void setSession(FilesystemSession session) {
        this.session = session;
    }

    public FilesystemSession getSession() {
        return session;
    }
}
