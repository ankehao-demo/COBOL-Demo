package com.example.cobol;

/**
 * Tiny helper for the COBOL screen-position style {@code DISPLAY ... AT yyxx}
 * statements. The original COBOL examples use ncurses-style positioning;
 * we approximate it with ANSI escape codes so the demos still feel similar
 * when run in a real terminal. When stdout is not a TTY (e.g. when running
 * tests or piping output) the positioning sequences become harmless escape
 * strings.
 */
public final class AnsiTerm {

    /** ANSI Control Sequence Introducer. */
    public static final String CSI = "\u001B[";

    private AnsiTerm() {}

    /** Move cursor to (row, col) — 1-indexed, mirroring COBOL "AT yyxx". */
    public static String moveTo(int row, int col) {
        return CSI + row + ";" + col + "H";
    }

    /** Clear the screen and move the cursor to home — equivalent of "BLANK SCREEN". */
    public static String clearScreen() {
        return CSI + "2J" + CSI + "H";
    }

    /** Erase from cursor to end of line — equivalent of "ERASE EOL". */
    public static String eraseEol() {
        return CSI + "K";
    }

    /** Bell — equivalent of "WITH BELL". */
    public static String bell() {
        return "\u0007";
    }

    /** Set foreground color (0-7 standard ANSI). */
    public static String fg(int code) {
        return CSI + (30 + code) + "m";
    }

    /** Set background color (0-7 standard ANSI). */
    public static String bg(int code) {
        return CSI + (40 + code) + "m";
    }

    /** Reset all colors / attributes. */
    public static String reset() {
        return CSI + "0m";
    }
}
