package com.coboldemo.commandargs;

/**
 * Migrated from read_command_args/read_specific_cmd_line_args.cbl
 * Reads command line args one by one.
 */
public class ReadSpecificCmdLineArgs {

    public static void main(String[] args) {
        // ACCEPT ws-num-args FROM ARGUMENT-NUMBER equivalent
        int numArgs = args.length;

        // Loop through all arguments and display each
        for (int i = 0; i < numArgs; i++) {
            System.out.println(args[i]);
        }
    }
}
