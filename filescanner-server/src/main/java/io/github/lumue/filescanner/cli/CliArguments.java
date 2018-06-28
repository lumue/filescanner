package io.github.lumue.filescanner.cli;

/**
 * Created by lm on 22.01.16.
 */
public class CliArguments {

    private final String path;

    private final String command;

    public CliArguments(String command, String path) {
        this.path = path;
        this.command = command;
    }


    public static CliArguments from(String[] args) {
        return new CliArguments(args[0],args[1]);
    }

    public String getPath() {
        return path;
    }

    public String getCommand() {
        return command;
    }
}
