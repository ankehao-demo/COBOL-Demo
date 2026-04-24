package com.coboldemo.io;

/**
 * Migrated from: read_command_args/read_cmd_line_args.cbl
 *
 * Reads the full command line arguments and checks for a specific
 * argument '--test'. Demonstrates ACCEPT FROM COMMAND-LINE and
 * INSPECT TALLYING.
 */
public class ReadCommandArgs {

    public static void main(String[] args) {
        System.out.println();
        System.out.println("Pass arg '--test' for special message.");

        String cmdArgs = String.join(" ", args);
        System.out.println("Full command line args: " + cmdArgs);

        // INSPECT ... TALLYING ... FOR ALL "--test"
        long testArgCount = countOccurrences(cmdArgs.toLowerCase(), "--test");

        if (testArgCount > 0) {
            System.out.println("You entered the '--test' cmd arg!");
        }

        System.out.println();
    }

    static long countOccurrences(String text, String target) {
        int count = 0;
        int idx = 0;
        while ((idx = text.indexOf(target, idx)) != -1) {
            count++;
            idx += target.length();
        }
        return count;
    }
}
