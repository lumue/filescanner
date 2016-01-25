package io.github.lumue.filescanner.cli;

/**
 * Created by lm on 22.01.16.
 */
public class CliTaskFactory {

    public static CliTask createTaskForArguments(CliArguments args) throws Exception{
        return new WriteNfoCliTask(args.getPath());
    }

}
