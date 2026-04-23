package com.coboldemo.commandargs;

/**
 * Java port of read_command_args/read_cmd_line_args.cbl.
 *
 * Counts occurrences of "--test" in the command-line arguments. The COBOL
 * version uses {@code INSPECT ... TALLYING FOR ALL} which scans substrings;
 * we replicate the same behaviour with string matching so embedded
 * occurrences (e.g. "--testing") are also counted, matching the original.
 */
public final class ReadCmdLineArgs {

    private ReadCmdLineArgs() {
    }

    public static void main(String[] args) {
        System.out.println();
        System.out.println("Pass arg '--test' for special message.");

        String commandLine = String.join(" ", args);
        System.out.println("Full command line args: " + commandLine);

        int count = countSubstring(commandLine.toLowerCase(), "--test");
        if (count > 0) {
            System.out.println("You entered the '--test' cmd arg!");
        }

        System.out.println();
    }

    /** Mirrors {@code INSPECT ... TALLYING FOR ALL "--test"}. */
    private static int countSubstring(String haystack, String needle) {
        if (needle.isEmpty()) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = haystack.indexOf(needle, idx)) != -1) {
            count++;
            idx += needle.length();
        }
        return count;
    }
}
