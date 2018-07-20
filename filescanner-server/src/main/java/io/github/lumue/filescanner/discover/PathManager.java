package io.github.lumue.filescanner.discover;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.Objects.*;

/**
 * manage the configuration and connection of ManagedPath s
 * <p>
 * Created by lm on 09.12.15.
 */
@Component
public class PathManager {
	
	private final ManagedPathRepository managedPathRepository;
	
	private final Pathscanner pathscanner;
	
	@Autowired
	public PathManager(ManagedPathRepository managedPathRepository, Pathscanner pathscanner) {
		this.managedPathRepository = requireNonNull(managedPathRepository);
		this.pathscanner = pathscanner;
	}
	
	
	public Mono<ManagedPath> addPath(String path, String name) {
		ManagedPath newPath = new ManagedPath(path, name);
		return managedPathRepository.save(newPath);
	}
	
	
	public Flux<ManagedPath> getList() {
		return managedPathRepository.findAll();
	}
	
	public Mono<Void> delete(String pathname) {
		return managedPathRepository.deleteById(pathname);
	}
	
	public Mono<ManagedPath> get(String pathname) {
		return managedPathRepository.findById(pathname);
	}
	
	public Boolean isScanning(ManagedPath p) {
		return pathscanner.isScanning(p.getPath());
	}
	
	public Mono<Void> stopScanning(ManagedPath managedPath) {
		if(!isScanning(managedPath))
			return Mono.<Void>empty();
		return pathscanner.stopScan(managedPath.getPath());
	}
	
	public Mono<Void> startScanning(ManagedPath managedPath) {
		if(isScanning(managedPath))
			return Mono.empty();
		pathscanner.startScan(managedPath.getPath());
		return Mono.empty();
	}
}
