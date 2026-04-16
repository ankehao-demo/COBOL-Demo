package com.coboldemo.screensize;

import java.util.Scanner;

/**
 * Migrated from: screen_size/get_screen_size.cbl
 * Original author: Erik Eriksen (2021-09-07, updated 2022-05-02)
 * Purpose: Example on getting the row and column count of the current terminal.
 *
 * Notes:
 * - Terminal size detection is platform-specific in Java.
 * - Uses ProcessBuilder to call 'tput lines' / 'tput cols' on Linux/macOS.
 * - On systems without tput, returns -1.
 */
public class ScreenSizeTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\033[2J"); // clear screen

        // Method 1: Using ACCEPT FROM LINES / COLUMNS equivalent
        System.out.println("Using 'ACCEPT ... FROM LINES' and 'ACCEPT ... FROM COLUMNS' to get screen size:");

        for (int i = 0; i < 3; i++) {
            int lines = getTerminalDimension("lines");
            int cols = getTerminalDimension("cols");
            displayScreenSize(lines, cols);
            System.out.println("Resize and press enter to continue");
            scanner.nextLine();
        }

        System.out.print("\033[2J"); // clear screen

        // Method 2: Using CBL_GET_SCR_SIZE equivalent (same approach in Java)
        System.out.println("Using 'CBL_GET_SCR_SIZE' to get screen size:");

        for (int i = 0; i < 3; i++) {
            int lines = getTerminalDimension("lines");
            int cols = getTerminalDimension("cols");
            displayScreenSize(lines, cols);
            System.out.println("Resize and press enter to continue");
            scanner.nextLine();
        }

        System.out.println("Done.");
    }

    private static void displayScreenSize(int lines, int cols) {
        System.out.println("-------------------------------------------------------------");
        System.out.println("Current screen size: ");
        System.out.println("Columns: " + cols);
        System.out.println("  Lines: " + lines);
    }

    /**
     * Get terminal dimension using tput command (Linux/macOS).
     * Returns -1 if unable to determine.
     */
    private static int getTerminalDimension(String dimension) {
        try {
            ProcessBuilder pb = new ProcessBuilder("tput", dimension);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            String output = new String(process.getInputStream().readAllBytes()).trim();
            process.waitFor();
            return Integer.parseInt(output);
        } catch (Exception e) {
            return -1;
        }
    }
}
