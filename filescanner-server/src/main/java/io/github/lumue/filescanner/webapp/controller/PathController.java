package io.github.lumue.filescanner.webapp.controller;

import io.github.lumue.filescanner.discover.ManagedPath;
import io.github.lumue.filescanner.discover.PathManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

  
    @RequestMapping(method = RequestMethod.POST,
            consumes = {"text/plain", "application/json"} ,
            produces = {"application/json"})
    public Mono<ManagedPath> add(
            @RequestBody ManagedPath path){
            return pathManager.addPath(path.getPath(),path.getName());
    }

    @RequestMapping(method = RequestMethod.GET)
    public Flux<ManagedPath> list(){
        return  pathManager.getList();
    }
	
	@GetMapping("/{pathname}")
	public Mono<ManagedPath> get(@PathVariable String pathname){
		return  pathManager.get(pathname);
	}
	
	@GetMapping("/{pathname}/scanning")
	public Mono<Boolean> getScanning(@PathVariable String pathname){
		return  pathManager.get(pathname)
				.map(pathManager::isScanning);
	}
	
	@DeleteMapping("/{pathname}/scanning")
	public Mono<Void> stopScanning(@PathVariable String pathname){
		return  pathManager.get(pathname)
				.flatMap(pathManager::stopScanning);
				
	}

    @PostMapping("/{pathname}/scanning")
    public Mono<Void> delete(@PathVariable String pathname){
        return pathManager.get(pathname).flatMap(pathManager::startScanning);
    }
}
