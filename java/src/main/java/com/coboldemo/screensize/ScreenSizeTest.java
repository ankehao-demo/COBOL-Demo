package com.coboldemo.screensize;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Migrated from: screen_size/get_screen_size.cbl
 * Original author: Erik Eriksen (2021-09-07, updated 2022-05-02)
 * Purpose: Example of getting the row and column count of the current terminal.
 *
 * COBOL-to-Java mapping:
 *   ACCEPT ws-scr-lines FROM LINES         -> tput lines (via ProcessBuilder)
 *   ACCEPT ws-scr-cols FROM COLS            -> tput cols (via ProcessBuilder)
 *   CALL "CBL_GET_SCR_SIZE" USING l c       -> tput lines + tput cols
 *   DISPLAY ... AT RRCC                     -> ANSI escape codes
 *   ACCEPT OMITTED                          -> Scanner.nextLine() (wait for Enter)
 *   DISPLAY SPACE BLANK SCREEN              -> \033[2J (clear screen)
 *
 * Note: Terminal size detection is platform-specific. This implementation
 * uses ProcessBuilder to call 'tput' commands on Linux/macOS. On Windows
 * or environments without 'tput', values will show as "N/A".
 *
 * The original COBOL program gets screen size using two methods:
 *   1. ACCEPT ... FROM LINES/COLUMNS (3 times with resize prompts)
 *   2. CALL "CBL_GET_SCR_SIZE" (3 times with resize prompts)
 */
public class ScreenSizeTest {

    /**
     * Moves the cursor to the specified row and column using ANSI escape codes.
     */
    private static void moveCursor(int row, int col) {
        System.out.printf("\033[%d;%dH", row, col);
    }

    /**
     * Attempts to get a terminal dimension using the 'tput' command.
     * This maps to COBOL's ACCEPT FROM LINES/COLUMNS and CBL_GET_SCR_SIZE.
     *
     * @param tputArg either "lines" or "cols"
     * @return the terminal dimension as a string, or "N/A" if unavailable
     */
    private static String getTerminalDimension(String tputArg) {
        try {
            ProcessBuilder pb = new ProcessBuilder("tput", tputArg);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                process.waitFor();
                return line != null ? line.trim() : "N/A";
            }
        } catch (Exception e) {
            return "N/A (terminal query not supported)";
        }
    }

    /**
     * Displays the current screen size and waits for user to press Enter.
     * Mimics the COBOL 'display-screens-size' paragraph.
     *
     * @param scanner Scanner for reading user input
     * @param lines   terminal lines count as string
     * @param cols    terminal columns count as string
     */
    private static void displayScreenSize(Scanner scanner, String lines, String cols) {
        moveCursor(2, 1);
        System.out.print("-------------------------------------------------------------");
        moveCursor(3, 1);
        System.out.print("Current screen size: ");
        moveCursor(4, 1);
        // COBOL: display concat("Columns: " ws-scr-cols-disp) at 0401
        System.out.print("Columns: " + cols);
        moveCursor(5, 1);
        // COBOL: display concat("  Lines: " ws-scr-lines-disp) at 0501
        System.out.print("  Lines: " + lines);
        moveCursor(7, 1);
        System.out.print("Resize and press enter to continue");
        // COBOL: accept omitted
        scanner.nextLine();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // ---------------------------------------------------------------
        // COBOL: display space blank screen
        // Clear the screen.
        // ---------------------------------------------------------------
        System.out.print("\033[2J");

        // ---------------------------------------------------------------
        // Method 1: Using ACCEPT ... FROM LINES/COLUMNS
        // COBOL performs this 3 times, prompting the user to resize between.
        // Java: Uses 'tput lines' and 'tput cols' via ProcessBuilder.
        // ---------------------------------------------------------------
        moveCursor(1, 1);
        System.out.print("Using 'ACCEPT ... FROM LINES' and 'ACCEPT ... FROM COLUMNS' to get screen size:");

        for (int i = 0; i < 3; i++) {
            String lines = getTerminalDimension("lines");
            String cols = getTerminalDimension("cols");
            displayScreenSize(scanner, lines, cols);
        }

        // ---------------------------------------------------------------
        // Method 2: Using CBL_GET_SCR_SIZE
        // COBOL: call "CBL_GET_SCR_SIZE" using ws-scr-lines ws-scr-cols
        // In Java, we use the same tput approach since there is no
        // CBL_GET_SCR_SIZE equivalent. Both methods produce the same result.
        // ---------------------------------------------------------------
        System.out.print("\033[2J");
        moveCursor(1, 1);
        System.out.print("Using 'CBL_GET_SCR_SIZE' to get screen size:");

        for (int i = 0; i < 3; i++) {
            // CBL_GET_SCR_SIZE gets both values in one call; in Java we
            // still use two tput calls but group them together.
            String lines = getTerminalDimension("lines");
            String cols = getTerminalDimension("cols");
            displayScreenSize(scanner, lines, cols);
        }

        moveCursor(9, 1);
        System.out.println("Done.");

        scanner.close();
    }
}
