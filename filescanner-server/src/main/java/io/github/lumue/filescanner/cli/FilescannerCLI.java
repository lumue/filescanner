package io.github.lumue.filescanner.cli;

/**
 *
 * Created by lm on 22.01.16.
 */
public class FilescannerCLI {

    public static void main(String[] args) {
        try {
            CliArguments cliArguments = CliArguments.from(args);
            CliTask cliTask = CliTaskFactory.createTaskForArguments(cliArguments);
            cliTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
