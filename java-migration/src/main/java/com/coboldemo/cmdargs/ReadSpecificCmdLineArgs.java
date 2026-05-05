package com.coboldemo.cmdargs;

/**
 * Java port of {@code read_command_args/read_specific_cmd_line_args.cbl}.
 *
 * Iterates each command line argument and prints it on its own line.
 */
public class ReadSpecificCmdLineArgs {

    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println(arg);
        }
    }
}
