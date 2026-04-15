package com.coboldemo.display;

/**
 * Java equivalent of display_test/display-test.cbl
 *
 * Demonstrates various DISPLAY statement options in COBOL. Screen-mode
 * positioning (AT LLCC, LINE/COLUMN) is replaced with plain console output.
 * For positioned terminal output, use Lanterna or ANSI escape codes.
 */
public class DisplayTest {

    public static void main(String[] args) {
        // DISPLAY "hello world" AT 0505 → plain println (no positioning)
        System.out.println("hello world");

        // DISPLAY "hello world" LINE 06 COLUMN 05
        System.out.println("hello world");

        // DISPLAY "hello world" WITH BLANK LINE → clear the line with spaces first
        System.out.println("                                                            ");
        System.out.println("hello world");

        // DISPLAY "hello world" WITH ERASE EOL → erase to end of line
        // In ANSI terminals: \033[K clears to end of line
        System.out.println("hello world\033[K");

        // DISPLAY "hello world" WITH BELL → audible bell character
        System.out.println("hello world\007");

        // DISPLAY "hello world" BACKGROUND-COLOR 03 FOREGROUND-COLOR 06
        // Using ANSI escape codes: 03=cyan background(46), 06=cyan foreground(36)
        // COBOL color 03 = cyan, COBOL color 06 = cyan
        System.out.println("\033[46;36mhello world\033[0m");
    }
}
