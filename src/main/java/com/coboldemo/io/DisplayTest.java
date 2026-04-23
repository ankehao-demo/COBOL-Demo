package com.coboldemo.io;

/**
 * Migrated from: display_test/display-test.cbl
 *
 * Demonstrates various display statement options. In COBOL, these use
 * screen-mode positioning (line/column), blank line, erase EOL, bell,
 * and foreground/background colors. In Java, we output to standard out
 * with ANSI escape codes for color and bell.
 */
public class DisplayTest {

    private static final String ANSI_RESET = "\033[0m";
    private static final String ANSI_BELL = "\007";

    public static void main(String[] args) {
        // Basic display at position (simulated as labeled output)
        System.out.println("[Line 05, Col 05] hello world");

        // Display with line/column
        System.out.println("[Line 06, Col 05] hello world");

        // Display with blank line
        System.out.println("[Line 07, Col 05] hello world (with blank line)");

        // Display with erase end of line
        System.out.println("[Line 08, Col 05] hello world (with erase EOL)");

        // Display with bell
        System.out.print(ANSI_BELL);
        System.out.println("[Line 09, Col 05] hello world (with bell)");

        // Display with foreground/background color
        // COBOL color 03 = cyan (background), color 06 = yellow (foreground)
        String ansiYellowOnCyan = "\033[33;46m";
        System.out.println(ansiYellowOnCyan + "[Line 10, Col 05] hello world" + ANSI_RESET);
    }
}
