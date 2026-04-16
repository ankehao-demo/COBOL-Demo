package com.coboldemo.commandargs;

/**
 * Migrated from: read_command_args/read_cmd_line_args.cbl
 * Original author: Erik Eriksen (2022-04-28)
 * Purpose: Example of reading the command line and counting occurrences of "--test".
 */
public class ReadCmdLineArgs {

    public static void main(String[] args) {
        // ACCEPT FROM COMMAND-LINE equivalent
        String commandLine = String.join(" ", args);
        System.out.println("Full command line: " + commandLine);

        // INSPECT TALLYING: count occurrences of "--test"
        int count = 0;
        String search = "--test";
        int idx = 0;
        String lower = commandLine.toLowerCase();
        while ((idx = lower.indexOf(search, idx)) != -1) {
            count++;
            idx += search.length();
        }

        System.out.println("Number of '--test' occurrences: " + count);
    }
}
