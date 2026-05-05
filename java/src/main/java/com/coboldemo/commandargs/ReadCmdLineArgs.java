package com.coboldemo.commandargs;

/**
 * Migrated from read_command_args/read_cmd_line_args.cbl
 * Demonstrates reading the full command line args.
 */
public class ReadCmdLineArgs {

    public static void main(String[] args) {
        System.out.println();
        System.out.println("Pass arg '--test' for special message");

        String cmdArgs = String.join(" ", args);
        System.out.println("Full command line args: " + cmdArgs);

        // INSPECT TALLYING equivalent: count occurrences of "--test" (case-insensitive)
        int testArgCount = 0;
        String lower = cmdArgs.toLowerCase();
        int idx = 0;
        while ((idx = lower.indexOf("--test", idx)) != -1) {
            testArgCount++;
            idx += 6;
        }

        if (testArgCount > 0) {
            System.out.println("You entered the '--test' cmd arg!");
        }

        System.out.println();
    }
}
