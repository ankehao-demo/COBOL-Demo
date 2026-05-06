package com.example.cobol.displaytest;

import com.example.cobol.AnsiTerm;

/**
 * Java port of {@code display_test/display-test.cbl}.
 *
 * <p>Demonstrates positioning, blank-line, erase-EOL, bell, and color
 * variations of the COBOL {@code DISPLAY} verb. We approximate them with
 * ANSI escape sequences. When stdout is not a TTY the escape codes simply
 * become inert text.
 */
public final class DisplayTest {

    private DisplayTest() {}

    public static void main(String[] args) {
        // DISPLAY "hello world" AT 0505
        System.out.print(AnsiTerm.moveTo(5, 5) + "hello world");

        // DISPLAY "hello world" LINE 06 COLUMN 05
        System.out.print(AnsiTerm.moveTo(6, 5) + "hello world");

        // DISPLAY ... LINE 07 COLUMN 05 WITH BLANK LINE
        // BLANK LINE clears the line first; we erase to EOL after positioning.
        System.out.print(AnsiTerm.moveTo(7, 5) + AnsiTerm.eraseEol() + "hello world");

        // DISPLAY ... LINE 08 COLUMN 05 WITH ERASE EOL
        System.out.print(AnsiTerm.moveTo(8, 5) + "hello world" + AnsiTerm.eraseEol());

        // DISPLAY ... LINE 09 COLUMN 05 WITH BELL
        System.out.print(AnsiTerm.moveTo(9, 5) + "hello world" + AnsiTerm.bell());

        // DISPLAY ... AT 1005 BACKGROUND-COLOR 03 FOREGROUND-COLOR 06
        System.out.print(AnsiTerm.moveTo(10, 5)
                + AnsiTerm.bg(3) + AnsiTerm.fg(6) + "hello world" + AnsiTerm.reset());

        // Final newline so that the next prompt (if any) doesn't overlap our output.
        System.out.println();
    }
}
