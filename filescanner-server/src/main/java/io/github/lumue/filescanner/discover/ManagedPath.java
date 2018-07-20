package io.github.lumue.filescanner.discover;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 *
 * Represents a manged Path
 *
 * Created by lm on 09.12.15.
 */
public class ManagedPath {

    @Id
    @JsonProperty("name")
    private String name;

    @JsonProperty("path")
    private String path;

    @JsonProperty("startScanOnConnect")
    private Boolean startScanOnConnect=true;

    @JsonProperty("connectOnStartup")
    private Boolean connectOnStartup=true;

    @JsonProperty("lastScanned")
    private LocalDateTime lastScanned;

    @JsonProperty("connected")
    private Boolean  connected=false;



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


    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }
}
