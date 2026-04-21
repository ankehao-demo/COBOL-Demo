package com.coboldemo.cmdargs;

/**
 * Port of {@code read_command_args/read_cmd_line_args.cbl} — joins all CLI
 * arguments into a single string and checks for the {@code --test} flag.
 */
public final class ReadCmdLineArgs {

    private ReadCmdLineArgs() {
    }

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
