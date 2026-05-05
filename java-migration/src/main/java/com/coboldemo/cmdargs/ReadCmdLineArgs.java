package com.coboldemo.cmdargs;

/**
 * Java port of {@code read_command_args/read_cmd_line_args.cbl}.
 *
 * Joins all command line arguments into a single string and looks for the
 * literal {@code --test} flag (case-insensitive), printing a special
 * message when it is present.
 */
public class ReadCmdLineArgs {

    public static void main(String[] args) {
        System.out.println();
        System.out.println("Pass arg '--test' for special message");

        String joined = String.join(" ", args);
        System.out.println("Full command line args: " + joined);

        if (joined.toLowerCase().contains("--test")) {
            System.out.println("You entered the '--test' cmd arg!");
        }

        System.out.println();
    }
}
