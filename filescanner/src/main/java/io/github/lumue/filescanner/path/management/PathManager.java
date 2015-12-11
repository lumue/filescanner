package io.github.lumue.filescanner.path.management;

import io.github.lumue.filescanner.path.repository.ManagedPathRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.Objects.*;

/**
 * manage the configuration and connection of ManagedPath s
 *
 * Created by lm on 09.12.15.
 */
@Component
public class PathManager {

    private final ManagedPathRepository managedPathRepository;

    private final FilesystemSessionManager sessionManager;

    /**
     * map of managed path instances
     */
    private final ConcurrentMap<String,ManagedPath> pathMap=new ConcurrentHashMap<>();

    @Autowired
    public PathManager(ManagedPathRepository managedPathRepository, FilesystemSessionManager sessionManager) {
        this.managedPathRepository = requireNonNull(managedPathRepository);
        this.sessionManager = requireNonNull(sessionManager);
    }


    @PostConstruct
    public void init(){
        refreshPathMap();
        getList().stream()
                .filter(p-> Boolean.TRUE.equals(p.getConnectOnStartup()))
                .parallel()
                .forEach(p -> {
                    try {
                        sessionManager.connect(p);
                    } catch (ExistingSessionException e) {
                        e.printStackTrace();
                    }
                });
    }

    private synchronized void refreshPathMap() {
        managedPathRepository.findAll().forEach(p->
            pathMap.putIfAbsent(p.getName(),p)
        );
    }

    public ManagedPath addPath(String path, String name) throws PathAlreadyManagedException, ExistingSessionException {
        ManagedPath newPath=new ManagedPath(path,name);
        managedPathRepository.save(newPath);
        pathMap.put(newPath.getName(),newPath);
        if(Boolean.TRUE.equals(newPath.getConnectOnStartup())){
            this.sessionManager.connect(newPath);
        }
        return newPath;
    }


    public List<ManagedPath> getList() {
        ArrayList<ManagedPath> managedPaths = new ArrayList<>();
        managedPaths.addAll(pathMap.values());
        return managedPaths;
    }

}
