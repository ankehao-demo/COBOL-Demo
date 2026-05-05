package com.coboldemo.display;

/**
 * Java equivalent of {@code display_test/display-test.cbl}.
 *
 * <p>The original COBOL program shows several variations of the
 * {@code DISPLAY} statement, primarily aimed at GnuCOBOL's screen-mode
 * positioning ({@code AT 0505}, {@code LINE / COLUMN}, {@code WITH BLANK
 * LINE / ERASE EOL / BELL}, foreground/background color attributes).
 * Most of those features rely on a curses-style terminal driver which
 * has no portable Java standard-library counterpart.</p>
 *
 * <p>This port reproduces the spirit of the program by demonstrating
 * the everyday Java equivalents:
 * <ul>
 *   <li>{@code System.out.println} for plain newline-terminated output.</li>
 *   <li>{@code System.out.printf} / {@link String#format} for numeric
 *       formatting and field padding (the closest you get to COBOL
 *       picture clauses).</li>
 *   <li>ANSI escape sequences for cursor positioning, foreground /
 *       background color, the bell character, and clear-to-end-of-line
 *       &mdash; the same building blocks ncurses uses under the hood.</li>
 * </ul>
 * The ANSI block is wrapped in a check so it only fires when stdout is
 * an actual terminal; otherwise we fall back to a textual description.
 */
public class DisplayTest {

    /** ANSI escape introducer (CSI). */
    private static final String CSI = "\u001b[";

    public static void main(String[] args) {
        // 1. Plain System.out.println - the analogue of `display "..."`.
        System.out.println("hello world");

        // 2. printf for formatted output. COBOL's PIC clauses (e.g.
        //    PIC 9(5), PIC ZZ9.99) translate naturally to printf format
        //    specifiers.
        System.out.printf("integer:        %5d%n", 42);
        System.out.printf("zero-padded:    %05d%n", 42);
        System.out.printf("decimal:        %8.2f%n", 3.14159);
        System.out.printf("string padded:  [%-10s]%n", "abc");
        System.out.printf("hex:            %08x%n", 0xCAFE);

        // 3. Concatenation: COBOL also uses DISPLAY with multiple
        //    operands. In Java that's just `+` on Strings.
        String name = "world";
        System.out.println("hello, " + name + "!");

        // 4. Printing without a trailing newline (`WITH NO ADVANCING`).
        System.out.print("no advancing -> ");
        System.out.println("continued on the same line");

        // 5. The screen-mode features of the original program use ANSI
        //    escapes here. We only emit them when stdout is a real
        //    terminal so logs stay clean.
        boolean isInteractive = System.console() != null;
        if (isInteractive) {
            // `display "hello world" at 0505` -> move cursor to row 5,
            // column 5 then print.
            System.out.print(moveTo(5, 5) + "hello world");

            // `with blank line` -> CSI 2K clears the entire line.
            System.out.print(moveTo(7, 5) + CSI + "2K" + "hello world");

            // `with erase eol` -> CSI K clears from cursor to end of line.
            System.out.print(moveTo(8, 5) + "hello world" + CSI + "K");

            // `with bell` -> emit the BEL control character (\u0007).
            System.out.print(moveTo(9, 5) + "hello world\u0007");

            // foreground-color 06 (cyan), background-color 03 (yellow) at row 10, col 5.
            System.out.print(moveTo(10, 5)
                    + CSI + "36;43m"
                    + "hello world"
                    + CSI + "0m");

            // Reset cursor below our scratch area so the shell prompt
            // does not overprint our output.
            System.out.println(moveTo(15, 1));
        } else {
            System.out.println("[ANSI positioning, color, and bell demos skipped: "
                    + "stdout is not an interactive TTY.]");
        }
    }

    /**
     * Returns an ANSI escape sequence that moves the cursor to
     * {@code (row, col)} (1-based, matching COBOL's screen-position
     * conventions).
     */
    private static String moveTo(int row, int col) {
        return CSI + row + ";" + col + "H";
    }
}
