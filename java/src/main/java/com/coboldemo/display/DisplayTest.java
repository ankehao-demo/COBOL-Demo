package com.coboldemo.display;

/**
 * Migrated from display_test/display-test.cbl
 * Demonstrates different display statement options using ANSI escape codes.
 */
public class DisplayTest {

    private static final String ESC = "\033[";

    public static void main(String[] args) {
        // Clear screen
        System.out.print(ESC + "2J");

        // display "hello world" at row 5, col 5
        moveCursor(5, 5);
        System.out.print("hello world");

        // display "hello world" at row 6, col 5
        moveCursor(6, 5);
        System.out.print("hello world");

        // display "hello world" at row 7, col 5 with blank line
        moveCursor(7, 5);
        System.out.print(ESC + "2K"); // blank entire line
        System.out.print("hello world");

        // display "hello world" at row 8, col 5 with erase to end of line
        moveCursor(8, 5);
        System.out.print("hello world");
        System.out.print(ESC + "K"); // erase to end of line

        // display "hello world" at row 9, col 5 with bell
        moveCursor(9, 5);
        System.out.print("hello world" + "\007"); // bell character

        // display "hello world" at row 10, col 5 with colors
        // foreground-color 06 = cyan, background-color 03 = yellow
        moveCursor(10, 5);
        System.out.print(ESC + "36;43m"); // cyan fg, yellow bg
        System.out.print("hello world");
        System.out.print(ESC + "0m"); // reset colors

        // Move cursor below output
        moveCursor(12, 1);
        System.out.println();
    }

    private static void moveCursor(int row, int col) {
        System.out.print(ESC + row + ";" + col + "H");
    }
}
