package com.coboldemo.commandargs;

/**
 * Java equivalent of read_command_args/read_specific_cmd_line_args.cbl
 *
 * Demonstrates reading command-line arguments one by one using COBOL's
 * ACCEPT FROM ARGUMENT-NUMBER and ACCEPT FROM ARGUMENT-VALUE pattern.
 *
 * COBOL Mapping:
 *   ACCEPT ws-num-args FROM ARGUMENT-NUMBER      → args.length
 *   DISPLAY ws-counter UPON ARGUMENT-NUMBER       → (set index)
 *   ACCEPT ws-cmd-args FROM ARGUMENT-VALUE        → args[i]
 */
public class ReadSpecificCmdLineArgs {

    public static void main(String[] args) {
        // ACCEPT ws-num-args FROM ARGUMENT-NUMBER
        int numArgs = args.length;

        // Loop through all arguments
        for (int i = 0; i < numArgs; i++) {
            // DISPLAY ws-counter UPON ARGUMENT-NUMBER → sets index
            // ACCEPT ws-cmd-args FROM ARGUMENT-VALUE → gets value at index
            System.out.println(args[i]);
        }
    }
}
