package com.coboldemo.io;

import java.util.Scanner;

/**
 * Migrated from: screen_size/get_screen_size.cbl
 *
 * Demonstrates getting the terminal row and column count.
 * Uses Lanterna library as the Java equivalent of CBL_GET_SCR_SIZE
 * and ACCEPT FROM LINES/COLUMNS.
 */
public class GetScreenSize {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Using Lanterna to get screen size:");

        // Method 1: ACCEPT FROM LINES / ACCEPT FROM COLUMNS equivalent
        System.out.println("Using 'ACCEPT ... FROM LINES' and 'ACCEPT ... FROM COLUMNS' equivalent:");

        for (int i = 0; i < 3; i++) {
            int[] size = getTerminalSize();
            displayScreenSize(size[0], size[1]);
            System.out.println("Resize and press Enter to continue");
            scanner.nextLine();
        }

        // Method 2: CBL_GET_SCR_SIZE equivalent (same Lanterna call)
        System.out.println("Using 'CBL_GET_SCR_SIZE' equivalent:");

        for (int i = 0; i < 3; i++) {
            int[] size = getTerminalSize();
            displayScreenSize(size[0], size[1]);
            System.out.println("Resize and press Enter to continue");
            scanner.nextLine();
        }

        System.out.println("Done.");
        scanner.close();
    }

    private static int[] getTerminalSize() {
        try {
            com.googlecode.lanterna.terminal.DefaultTerminalFactory factory =
                    new com.googlecode.lanterna.terminal.DefaultTerminalFactory();
            com.googlecode.lanterna.terminal.Terminal terminal = factory.createTerminal();
            com.googlecode.lanterna.TerminalSize size = terminal.getTerminalSize();
            int rows = size.getRows();
            int cols = size.getColumns();
            terminal.close();
            return new int[]{rows, cols};
        } catch (Exception e) {
            // Fallback defaults
            return new int[]{24, 80};
        }
    }

    private static void displayScreenSize(int lines, int cols) {
        System.out.println("-------------------------------------------------------------");
        System.out.println("Current screen size:");
        System.out.printf("Columns: %3d%n", cols);
        System.out.printf("  Lines: %3d%n", lines);
    }
}
