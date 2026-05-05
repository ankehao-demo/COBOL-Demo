package com.coboldemo.display;

/**
 * Java port of {@code display_test/display-test.cbl}.
 *
 * Demonstrates several COBOL DISPLAY clause variants. Java has no
 * built-in screen-mode positioning, so:
 * - LINE/COLUMN positioning is approximated with ANSI cursor escape
 *   codes ("\u001b[row;colH"). These rely on a VT-100 compatible terminal.
 * - BLANK LINE / ERASE EOL are approximated with ANSI CSI K / 2K.
 * - WITH BELL writes the BEL character ("\u0007").
 * - FOREGROUND-COLOR / BACKGROUND-COLOR are approximated with ANSI SGR
 *   color escape codes.
 */
public class DisplayTest {

    public static void main(String[] args) {
        // "AT 0505" - row 5, column 5
        printAt(5, 5, "hello world");

        // LINE 06 COLUMN 05
        printAt(6, 5, "hello world");

        // LINE 07 COLUMN 05 WITH BLANK LINE
        // (clear the line first, then print)
        moveCursor(7, 1);
        System.out.print("\u001b[2K"); // clear entire line
        moveCursor(7, 5);
        System.out.println("hello world");

        // LINE 08 COLUMN 05 WITH ERASE EOL
        moveCursor(8, 5);
        System.out.print("hello world\u001b[K"); // erase to EOL
        System.out.println();

        // LINE 09 COLUMN 05 WITH BELL
        moveCursor(9, 5);
        System.out.println("hello world\u0007");

        // BACKGROUND-COLOR 03 / FOREGROUND-COLOR 06 AT 1005
        // COBOL palette: 0 black, 1 blue, 2 green, 3 cyan, 4 red,
        // 5 magenta, 6 yellow, 7 white. Map directly to ANSI 30..37 / 40..47.
        moveCursor(10, 5);
        System.out.print(ansiSgr(36, 43)); // fg=cyan(6 -> 33+3? we just pick ANSI 36 cyan, 43 yellow bg)
        System.out.print("hello world");
        System.out.println("\u001b[0m");
    }

    private static void printAt(int row, int col, String text) {
        moveCursor(row, col);
        System.out.println(text);
    }

    private static void moveCursor(int row, int col) {
        System.out.print(String.format("\u001b[%d;%dH", row, col));
    }

    private static String ansiSgr(int fg, int bg) {
        return String.format("\u001b[%d;%dm", fg, bg);
    }
}
