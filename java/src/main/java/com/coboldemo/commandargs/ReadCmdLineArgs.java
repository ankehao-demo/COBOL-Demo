package com.coboldemo.commandargs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java equivalent of read_command_args/read_cmd_line_args.cbl
 *
 * Demonstrates reading the full command-line argument string and inspecting
 * it for specific patterns. The COBOL INSPECT ... TALLYING is mapped to
 * Pattern/Matcher counting in Java.
 *
 * COBOL Mapping:
 *   ACCEPT ws-cmd-args FROM COMMAND-LINE       → String.join(" ", args)
 *   INSPECT ... TALLYING ... FOR ALL "--test"  → Pattern.compile("--test").matcher(s)
 *   FUNCTION LOWER-CASE(ws-cmd-args)           → s.toLowerCase()
 */
public class ReadCmdLineArgs {

    public static void main(String[] args) {
        System.out.println();
        System.out.println("Pass arg '--test' for special message");

        // ACCEPT ws-cmd-args FROM COMMAND-LINE
        String cmdArgs = String.join(" ", args);
        System.out.println("Full command line args: " + cmdArgs);

        // INSPECT FUNCTION LOWER-CASE(ws-cmd-args) TALLYING ws-test-arg-count FOR ALL "--test"
        String lowerArgs = cmdArgs.toLowerCase();
        Pattern pattern = Pattern.compile("--test");
        Matcher matcher = pattern.matcher(lowerArgs);
        long testArgCount = matcher.results().count();

        if (testArgCount > 0) {
            System.out.println("You entered the '--test' cmd arg!");
        }

        System.out.println();
    }
}
