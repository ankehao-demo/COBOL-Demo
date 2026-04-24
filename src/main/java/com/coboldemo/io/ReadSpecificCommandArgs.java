package com.coboldemo.io;

/**
 * Migrated from: read_command_args/read_specific_cmd_line_args.cbl
 *
 * Reads command line arguments one by one and displays each.
 * Demonstrates ACCEPT FROM ARGUMENT-NUMBER/ARGUMENT-VALUE.
 */
public class ReadSpecificCommandArgs {

    public static void main(String[] args) {
        // Get total number of command line args
        int numArgs = args.length;

        // Loop through all of them and display each
        for (int i = 0; i < numArgs; i++) {
            System.out.println(args[i]);
        }
    }
}
