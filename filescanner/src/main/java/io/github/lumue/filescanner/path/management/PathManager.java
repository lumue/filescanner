package io.github.lumue.filescanner.path.management;

import io.github.lumue.filescanner.path.repository.ManagedPathRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lm on 09.12.15.
 */
public class PathManager {

    private ManagedPathRepository managedPathRepository;

    public ManagedPath addPath(String path, String name) throws PathAlreadyManagedException{
        ManagedPath newPath=new ManagedPath(path,name);
        managedPathRepository.save(newPath);
        return newPath;
    }


    public List<ManagedPath> getList() {
        ArrayList<ManagedPath> managedPaths = new ArrayList<>();
        managedPaths.addAll(managedPaths);
        return managedPaths;
    }

}
