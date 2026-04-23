package com.coboldemo.display;

/**
 * Java port of display_test/display-test.cbl.
 *
 * The original exercises COBOL's screen-mode DISPLAY options: coordinate
 * positioning, BLANK LINE, ERASE EOL, BELL and colour attributes. We emit
 * ANSI escape sequences to approximate those features; terminals without ANSI
 * support will simply see the control characters in the output.
 */
public final class DisplayTest {

    private static final String CSI = "\033[";
    private static final String BELL = "\007";
    private static final String RESET = CSI + "0m";

    private DisplayTest() {
    }

    public static void main(String[] args) {
        // AT 0505 -> move cursor to line 5, column 5.
        System.out.print(moveCursor(5, 5));
        System.out.print("hello world");

        // LINE 06 COLUMN 05
        System.out.print(moveCursor(6, 5));
        System.out.print("hello world");

        // BLANK LINE -> clear the entire line before printing.
        System.out.print(moveCursor(7, 1));
        System.out.print(CSI + "2K");
        System.out.print(moveCursor(7, 5));
        System.out.print("hello world");

        // ERASE EOL -> clear from cursor to end of line.
        System.out.print(moveCursor(8, 5));
        System.out.print("hello world" + CSI + "K");

        // BELL
        System.out.print(moveCursor(9, 5));
        System.out.print("hello world" + BELL);

        // BACKGROUND-COLOR 03 + FOREGROUND-COLOR 06 -> ANSI colour pair.
        System.out.print(moveCursor(10, 5));
        System.out.print(CSI + "43;36m" + "hello world" + RESET);

        System.out.println();
    }

    /** Builds an ANSI cursor-position sequence for the given 1-indexed row/col. */
    static String moveCursor(int line, int column) {
        return CSI + line + ";" + column + "H";
    }
}
