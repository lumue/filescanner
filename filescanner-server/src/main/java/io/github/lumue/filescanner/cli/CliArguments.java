package io.github.lumue.filescanner.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lm on 22.01.16.
 */
public class CliArguments {

    private final String path;

    private final String command;
    private final List<String> customArgs;

    public CliArguments(String command, String path, final List<String> moreArgs) {
        this.path = path;
        this.command = command;
        this.customArgs=moreArgs;
    }


    public static CliArguments from(String[] args) {
        List<String> moreArgs=new ArrayList<>();
        if(args.length>2){
            moreArgs.addAll(Arrays.asList(args).subList(2, args.length));
        }
        return new CliArguments(args[0],args[1],moreArgs);
    }

    public String getPath() {
        return path;
    }

    public String getCommand() {
        return command;
    }

    public List<String> getCustomArgs() {
        return customArgs;
    }
}
