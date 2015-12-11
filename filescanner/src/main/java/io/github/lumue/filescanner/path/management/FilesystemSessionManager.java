package io.github.lumue.filescanner.path.management;

import io.github.lumue.filescanner.path.core.Pathmonitor;
import io.github.lumue.filescanner.path.core.Pathscanner;
import org.elasticsearch.common.netty.util.internal.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lm on 10.12.15.
 */
@Service
class FilesystemSessionManager {

    private final Pathmonitor pathmonitor;

    private final Pathscanner pathscanner;



    private final ConcurrentHashMap<String,FilesystemSession> sessions=new ConcurrentHashMap<>();

    @Autowired
    public FilesystemSessionManager(Pathmonitor pathmonitor, Pathscanner pathscanner) {
        this.pathmonitor = pathmonitor;
        this.pathscanner = pathscanner;
    }

    public FilesystemSession connect(ManagedPath path) throws ExistingSessionException {
        if(sessions.containsKey(path.getName()))
            throw new ExistingSessionException("Path "+path.getName()+" already has a session");

        FilesystemSession filesystemSession = new FilesystemSession(path, pathmonitor, pathscanner, (session) -> sessions.remove(session.pathName()));
        filesystemSession.open();


        return filesystemSession;
    }


}
