package io.github.lumue.filescanner.path.controller;

import io.github.lumue.filescanner.path.management.ExistingSessionException;
import io.github.lumue.filescanner.path.management.ManagedPath;
import io.github.lumue.filescanner.path.management.PathManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by lm on 09.12.15.
 */
@RestController
@RequestMapping("/paths")
@CrossOrigin
public class PathController {

    private final static Logger LOGGER= LoggerFactory.getLogger(PathController.class);

    private final PathManager pathManager;

    @Autowired
    public PathController(PathManager pathManager) {
        this.pathManager = pathManager;
    }

    @ResponseStatus(value = HttpStatus.CONFLICT,reason = "path already managed")
    public static class HttpConflictStatusException extends RuntimeException{
        public HttpConflictStatusException(Exception e) {
            super(e);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ManagedPath add(
            @RequestBody ManagedPath path){
        try {
            return pathManager.addPath(path.getPath(),path.getName());
        } catch (io.github.lumue.filescanner.path.management.PathAlreadyManagedException e) {
            LOGGER.error("path already managed",e);
            throw new HttpConflictStatusException(e);
        } catch (ExistingSessionException e2) {
            LOGGER.error("error starting session for path. already exists",e2);
            throw new HttpConflictStatusException(e2);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ManagedPath> list(){
        return  pathManager.getList();
    }

}
