package io.github.lumue.filescanner.path.management;

import io.github.lumue.filescanner.path.core.FilesystemMonitorTask;
import io.github.lumue.filescanner.path.core.Pathmonitor;
import io.github.lumue.filescanner.path.core.Pathscanner;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.requireNonNull;

/**
 * Created by lm on 10.12.15.
 */
class FilesystemSession implements AutoCloseable{


    private final AtomicBoolean open=new AtomicBoolean(false);

    @Override
    public void close() throws Exception {
        if(monitorTaskReference.get()!=null)
            monitorTaskReference.get().stop();
        monitorTaskReference.set(null);
        managedPath.setConnected(false);
        sessionClosedCallback.afterSessionClosed(this);
    }

    public String pathName() {
        return this.managedPath.getName();
    }

    public void open() {
        managedPath.setConnected(true);
        if(Boolean.TRUE.equals(managedPath.getStartScanOnConnect())){
            pathscanner.startScan(managedPath.getPath());
        }
        try {
            monitorTaskReference.set(pathmonitor.newMonitor(Paths.get(managedPath.getPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.open.compareAndSet(false,true);
    }

    public boolean isOpen() {
        return open.get();
    }


    interface SessionClosedCallback{
        void afterSessionClosed(FilesystemSession session);
    }

    private final ManagedPath managedPath;
    private final Pathmonitor pathmonitor;
    private final Pathscanner pathscanner;
    private final AtomicReference<FilesystemMonitorTask> monitorTaskReference=new AtomicReference<>();
    private final SessionClosedCallback sessionClosedCallback;

    FilesystemSession(ManagedPath managedPath, Pathmonitor pathmonitor, Pathscanner pathscanner, SessionClosedCallback sessionClosedCallback) {
        this.sessionClosedCallback = requireNonNull(sessionClosedCallback);
        this.managedPath = requireNonNull(managedPath);
        this.pathmonitor = requireNonNull(pathmonitor);
        this.pathscanner = requireNonNull(pathscanner);
    }
}
