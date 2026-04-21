package com.coboldemo.display;

/**
 * Port of {@code display_test/display-test.cbl} — demonstrates the COBOL
 * {@code DISPLAY} statement with screen-mode modifiers.
 *
 * <p>COBOL's {@code DISPLAY ... AT line/column}, {@code WITH BLANK LINE},
 * {@code WITH ERASE EOL}, {@code WITH BELL} and {@code BACKGROUND-COLOR} /
 * {@code FOREGROUND-COLOR} clauses drive the terminal directly. Java's
 * {@link System#out} is a stream and has no equivalent. The notes below
 * preserve the intent of each original line.
 */
public final class DisplayTest {

    private DisplayTest() {
    }

    public static void main(String[] args) {
        // display "hello world" at 0505
        System.out.println("hello world  (COBOL: AT 0505)");

        // display "hello world" line 06 column 05
        System.out.println("hello world  (COBOL: LINE 06 COLUMN 05)");

        // display "hello world" line 07 column 05 with blank line
        System.out.println();
        System.out.println("hello world  (COBOL: LINE 07 COLUMN 05 WITH BLANK LINE)");

        // display "hello world" line 08 column 05 with erase eol
        System.out.println("hello world  (COBOL: LINE 08 COLUMN 05 WITH ERASE EOL)");

        // display "hello world" line 09 column 05 with bell
        // The ASCII bell (0x07) is printed to approximate the BELL clause.
        System.out.println("\u0007hello world  (COBOL: LINE 09 COLUMN 05 WITH BELL)");

        // display "hello world" background-color 03 foreground-color 06 at 1005
        // Approximate with ANSI escape codes where COBOL-03 ~ yellow background
        // and COBOL-06 ~ cyan foreground. Viewer's terminal must support ANSI.
        System.out.println("\u001B[43m\u001B[36mhello world\u001B[0m  (COBOL: bg 03 fg 06 AT 1005)");
    }
}
