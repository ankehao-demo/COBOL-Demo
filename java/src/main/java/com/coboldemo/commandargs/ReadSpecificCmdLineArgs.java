package com.coboldemo.commandargs;

/**
 * Migrated from: read_command_args/read_specific_cmd_line_args.cbl
 * Original author: Erik Eriksen (2022-04-28)
 * Purpose: Example of iterating through individual command line arguments.
 */
public class ReadSpecificCmdLineArgs {

    public static void main(String[] args) {
        // ACCEPT FROM ARGUMENT-NUMBER equivalent
        int argCount = args.length;
        System.out.println("Number of arguments: " + argCount);

        // Iterate through arguments
        for (int i = 0; i < argCount; i++) {
            System.out.println("Argument " + (i + 1) + ": " + args[i]);
        }
    }
}
