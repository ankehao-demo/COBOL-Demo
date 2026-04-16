package com.coboldemo.display;

/**
 * Migrated from: display_test/display-test.cbl
 * Original author: Erik Eriksen (2021-08-26)
 * Purpose: Testing different display statement options.
 *
 * COBOL-to-Java mapping:
 *   DISPLAY ... AT RRCC             -> ANSI escape: \033[row;colH
 *   DISPLAY ... LINE n COLUMN m     -> ANSI escape: \033[n;mH
 *   WITH BLANK LINE                 -> ANSI escape: \033[2K (erase entire line)
 *   WITH ERASE EOL                  -> ANSI escape: \033[K  (erase to end of line)
 *   WITH BELL                       -> \007 (BEL character)
 *   BACKGROUND-COLOR n              -> ANSI escape: \033[4nm (background color)
 *   FOREGROUND-COLOR n              -> ANSI escape: \033[3nm (foreground color)
 *
 * COBOL color codes to ANSI mapping:
 *   0 = Black   -> 30/40
 *   1 = Blue    -> 34/44
 *   2 = Green   -> 32/42
 *   3 = Cyan    -> 36/46
 *   4 = Red     -> 31/41
 *   5 = Magenta -> 35/45
 *   6 = Yellow  -> 33/43
 *   7 = White   -> 37/47
 */
public class DisplayTest {

    // COBOL color codes mapped to ANSI foreground/background codes
    private static final int[] COBOL_TO_ANSI_FG = {30, 34, 32, 36, 31, 35, 33, 37};
    private static final int[] COBOL_TO_ANSI_BG = {40, 44, 42, 46, 41, 45, 43, 47};

    /**
     * Moves the cursor to the specified row and column using ANSI escape codes.
     */
    private static void moveCursor(int row, int col) {
        System.out.printf("\033[%d;%dH", row, col);
    }

    public static void main(String[] args) {
        // Clear screen to approximate COBOL screen mode
        System.out.print("\033[2J");

        // ---------------------------------------------------------------
        // COBOL: display "hello world" at 0505
        // AT RRCC: row=05, col=05
        // ---------------------------------------------------------------
        moveCursor(5, 5);
        System.out.print("hello world");

        // ---------------------------------------------------------------
        // COBOL: display "hello world" line 06 column 05
        // LINE/COLUMN syntax is equivalent to AT RRCC.
        // ---------------------------------------------------------------
        moveCursor(6, 5);
        System.out.print("hello world");

        // ---------------------------------------------------------------
        // COBOL: display "hello world" line 07 column 05 with blank line
        // BLANK LINE: clears the entire line before displaying.
        // ANSI: \033[2K erases the entire current line.
        // ---------------------------------------------------------------
        moveCursor(7, 1);
        System.out.print("\033[2K"); // Erase entire line (COBOL BLANK LINE)
        moveCursor(7, 5);
        System.out.print("hello world");

        // ---------------------------------------------------------------
        // COBOL: display "hello world" line 08 column 05 with erase eol
        // ERASE EOL: erases from current position to end of line.
        // ANSI: \033[K erases from cursor to end of line.
        // ---------------------------------------------------------------
        moveCursor(8, 5);
        System.out.print("hello world");
        System.out.print("\033[K"); // Erase to end of line (COBOL ERASE EOL)

        // ---------------------------------------------------------------
        // COBOL: display "hello world" line 09 column 05 with bell
        // BELL: produces an audible beep.
        // ANSI: \007 (BEL character)
        // ---------------------------------------------------------------
        moveCursor(9, 5);
        System.out.print("hello world");
        System.out.print("\007"); // Bell character (COBOL WITH BELL)

        // ---------------------------------------------------------------
        // COBOL: display "hello world"
        //            background-color 03
        //            foreground-color 06
        //            at 1005
        //
        // COBOL color 03 = Cyan (ANSI BG 46)
        // COBOL color 06 = Yellow (ANSI FG 33)
        // ---------------------------------------------------------------
        int bgColor = 3; // COBOL Cyan
        int fgColor = 6; // COBOL Yellow
        moveCursor(10, 5);
        System.out.printf("\033[%d;%dm", COBOL_TO_ANSI_FG[fgColor], COBOL_TO_ANSI_BG[bgColor]);
        System.out.print("hello world");
        System.out.print("\033[0m"); // Reset colors to default

        // Move cursor below output so the prompt doesn't overwrite it
        moveCursor(12, 1);
        System.out.println();
    }
}
