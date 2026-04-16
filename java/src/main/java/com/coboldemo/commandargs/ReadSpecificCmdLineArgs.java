package com.coboldemo.commandargs;

/**
 * Migrated from: read_command_args/read_specific_cmd_line_args.cbl
 * Original author: Erik Eriksen (2021-02-02)
 * Purpose: Reading command line args into variable one by one.
 *
 * COBOL-to-Java mapping:
 *   ACCEPT ws-num-args FROM ARGUMENT-NUMBER
 *       -> args.length
 *          COBOL's ARGUMENT-NUMBER returns the count of command line arguments.
 *
 *   DISPLAY ws-counter UPON ARGUMENT-NUMBER
 *       -> (sets the current argument index pointer in COBOL; no Java equivalent
 *          needed since we access args[] directly by index)
 *
 *   ACCEPT ws-cmd-args FROM ARGUMENT-VALUE
 *       -> args[i]
 *          COBOL's ARGUMENT-VALUE returns the argument at the index previously
 *          set by DISPLAY ... UPON ARGUMENT-NUMBER. In Java, we simply index
 *          into the args[] array.
 *
 *   PERFORM VARYING ws-counter FROM 1 BY 1 UNTIL ws-counter > ws-num-args
 *       -> for (int i = 0; i < args.length; i++)
 *          Note: COBOL uses 1-based indexing; Java uses 0-based.
 */
public class ReadSpecificCmdLineArgs {

    public static void main(String[] args) {
        // COBOL: accept ws-num-args from argument-number
        // Gets the total number of command line arguments.
        int numArgs = args.length;

        // COBOL: perform varying ws-counter from 1 by 1 until ws-counter > ws-num-args
        for (int i = 0; i < numArgs; i++) {
            // COBOL: display ws-counter upon argument-number
            // (Sets the argument pointer - not needed in Java since we use array indexing)

            // COBOL: accept ws-cmd-args from argument-value
            // Gets the value of the current argument.
            String cmdArg = args[i];

            // COBOL: display ws-cmd-args
            System.out.println(cmdArg);
        }
    }
}
