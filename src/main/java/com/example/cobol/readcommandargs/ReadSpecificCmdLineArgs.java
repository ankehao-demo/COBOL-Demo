package com.example.cobol.readcommandargs;

/**
 * Java port of {@code read_command_args/read_specific_cmd_line_args.cbl}.
 *
 * <p>Iterates over the command-line arguments one at a time and prints each
 * one. The COBOL version sets an "argument number" pointer and then accepts
 * "argument value"; in Java we just index into {@code args}.
 */
public final class ReadSpecificCmdLineArgs {

    private ReadSpecificCmdLineArgs() {}

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }
    }
}
