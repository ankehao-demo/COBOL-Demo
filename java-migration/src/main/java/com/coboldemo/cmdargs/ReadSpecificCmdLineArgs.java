package com.coboldemo.cmdargs;

/**
 * Port of {@code read_command_args/read_specific_cmd_line_args.cbl} —
 * iterates through each command line argument one at a time.
 */
public final class ReadSpecificCmdLineArgs {

    private ReadSpecificCmdLineArgs() {
    }

    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println(arg);
        }
    }
}
