package io.github.lumue.filescanner.cli;

/**
 * Created by lm on 22.01.16.
 */
public class CliTaskFactory {

    public static CliTask createTaskForArguments(CliArguments args) throws Exception{
        final boolean overrideExistingNfo=!args.getCustomArgs().isEmpty()&&Boolean.parseBoolean(args.getCustomArgs().get(0));
        return new WriteNfoCliTask(args.getPath(), overrideExistingNfo);
    }

}
