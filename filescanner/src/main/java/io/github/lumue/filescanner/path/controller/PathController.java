package io.github.lumue.filescanner.path.controller;

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
public class PathController {

    private final static Logger LOGGER= LoggerFactory.getLogger(PathController.class);

    private final PathManager pathManager;

    @Autowired
    public PathController(PathManager pathManager) {
        this.pathManager = pathManager;
    }

    @ResponseStatus(value = HttpStatus.CONFLICT,reason = "path already managed")
    public static class HttpConflictStatusException extends RuntimeException{
        public HttpConflictStatusException(io.github.lumue.filescanner.path.management.PathAlreadyManagedException e) {
            super(e);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ManagedPath add(
            @RequestParam("path") String path,
            @RequestParam("name") String name){
        try {
            return pathManager.addPath(path,name);
        } catch (io.github.lumue.filescanner.path.management.PathAlreadyManagedException e) {
            LOGGER.error("path already managed",e);
            throw new HttpConflictStatusException(e);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ManagedPath> list(){
        return  pathManager.getList();
    }

}
