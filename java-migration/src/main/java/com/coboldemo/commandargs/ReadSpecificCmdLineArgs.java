package com.coboldemo.commandargs;

/**
 * Java port of read_command_args/read_specific_cmd_line_args.cbl.
 *
 * Iterates each individual command-line argument and prints it. The original
 * used {@code ACCEPT FROM ARGUMENT-NUMBER} to get the count and
 * {@code ACCEPT FROM ARGUMENT-VALUE} inside a loop; Java provides both via
 * the {@code String[] args} parameter on {@code main}.
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
