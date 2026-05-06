package com.example.cobol.readcommandargs;

/**
 * Java port of {@code read_command_args/read_cmd_line_args.cbl}.
 *
 * <p>Demonstrates reading the full command line and counting the
 * occurrences of a specific flag (case-insensitive).
 */
public final class ReadCmdLineArgs {

    private ReadCmdLineArgs() {}

    public static void main(String[] args) {
        System.out.println();
        System.out.println("Pass arg '--test' for special message");

        String full = String.join(" ", args);
        System.out.println("Full command line args: " + full);

        long testArgCount = full.toLowerCase().split("--test", -1).length - 1;
        if (testArgCount > 0) {
            System.out.println("You entered the '--test' cmd arg!");
        }
        System.out.println();
    }
}
