package com.coboldemo.commandargs;

/**
 * Migrated from: read_command_args/read_cmd_line_args.cbl
 * Original author: Erik Eriksen (2022-04-13)
 * Purpose: Simple example of reading full command line args.
 *
 * COBOL-to-Java mapping:
 *   ACCEPT ws-cmd-args FROM COMMAND-LINE
 *       -> String.join(" ", args)
 *          COBOL's COMMAND-LINE returns all arguments as a single string.
 *          Java's args[] array is joined with spaces to replicate this.
 *
 *   INSPECT FUNCTION LOWER-CASE(ws-cmd-args)
 *       TALLYING ws-test-arg-count FOR ALL "--test"
 *       -> String.toLowerCase().contains("--test")
 *          COBOL's INSPECT TALLYING counts occurrences of a substring.
 *          We use contains() for the boolean check (matching the > 0 test),
 *          but also count occurrences to preserve the original logic.
 *
 *   DISPLAY SPACE -> System.out.println() (blank line)
 *   STOP RUN     -> System.exit(0) or simply return from main
 */
public class ReadCmdLineArgs {

    public static void main(String[] args) {
        // COBOL: display space
        System.out.println();

        // COBOL: display "Pass arg '--test' for special message".
        System.out.println("Pass arg '--test' for special message");

        // COBOL: accept ws-cmd-args from command-line
        // COMMAND-LINE returns the full argument string.
        // Java: join all args with spaces to replicate COBOL behavior.
        String cmdArgs = String.join(" ", args);

        // COBOL: display "Full command line args: " ws-cmd-args
        System.out.println("Full command line args: " + cmdArgs);

        // COBOL: inspect function lower-case(ws-cmd-args)
        //            tallying ws-test-arg-count for all "--test"
        // Count occurrences of "--test" in the lowercased command line.
        String lowerCmdArgs = cmdArgs.toLowerCase();
        int testArgCount = 0;
        int searchIndex = 0;
        while ((searchIndex = lowerCmdArgs.indexOf("--test", searchIndex)) != -1) {
            testArgCount++;
            searchIndex += "--test".length();
        }

        // COBOL: if ws-test-arg-count > 0 then
        //            display "You entered the '--test' cmd arg!"
        //        end-if
        if (testArgCount > 0) {
            System.out.println("You entered the '--test' cmd arg!");
        }

        // COBOL: display space
        System.out.println();

        // COBOL: stop run.
    }
}
