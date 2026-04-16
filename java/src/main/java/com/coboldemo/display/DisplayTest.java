package com.coboldemo.display;

/**
 * Migrated from: display_test/display-test.cbl
 * Original author: Erik Eriksen (2021-08-26)
 * Purpose: Testing different display statement options.
 *
 * Notes:
 * - Screen-mode positioning (at RRCC, line X column Y) has no direct Java console equivalent.
 * - ANSI escape codes are used to approximate positioning: \033[row;colH
 * - Features like blank line, erase eol, bell, and colors are approximated with ANSI codes.
 */
public class DisplayTest {

    public static void main(String[] args) {
        // Clear screen
        System.out.print("\033[2J");

        // display "hello world" at 0505 (row 5, col 5)
        displayAt(5, 5, "hello world");

        // display "hello world" line 06 column 05
        displayAt(6, 5, "hello world");

        // display "hello world" line 07 column 05 with blank line
        // (blank line clears the line before writing)
        System.out.print("\033[7;1H\033[2K"); // move to line 7, clear entire line
        displayAt(7, 5, "hello world");

        // display "hello world" line 08 column 05 with erase eol
        displayAt(8, 5, "hello world");
        System.out.print("\033[K"); // erase to end of line

        // display "hello world" line 09 column 05 with bell
        displayAt(9, 5, "hello world");
        System.out.print("\007"); // bell character

        // display "hello world" background-color 03 foreground-color 06 at 1005
        // COBOL color 03 = cyan (background), 06 = yellow (foreground)
        System.out.print("\033[10;5H");
        System.out.print("\033[33;46m"); // yellow fg, cyan bg
        System.out.print("hello world");
        System.out.print("\033[0m");     // reset colors
        System.out.println();
    }

    private static void displayAt(int row, int col, String text) {
        System.out.printf("\033[%d;%dH%s%n", row, col, text);
    }
}
