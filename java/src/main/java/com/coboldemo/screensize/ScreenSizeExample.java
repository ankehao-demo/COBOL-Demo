package com.coboldemo.screensize;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.Scanner;

/**
 * Java equivalent of screen_size/get_screen_size.cbl
 *
 * Demonstrates getting terminal screen dimensions. The COBOL program uses
 * ACCEPT ... FROM LINES/COLUMNS and CBL_GET_SCR_SIZE. In Java, the Lanterna
 * library provides terminal.getTerminalSize() for the same purpose.
 *
 * COBOL Mapping:
 *   ACCEPT ws-scr-lines FROM LINES     → terminal.getTerminalSize().getRows()
 *   ACCEPT ws-scr-cols FROM COLUMNS    → terminal.getTerminalSize().getColumns()
 *   CALL "CBL_GET_SCR_SIZE"            → same Lanterna API (single call)
 *
 * Note: This program requires a real terminal to function properly.
 * When run without a terminal (e.g., in an IDE), it falls back to
 * environment variables or default values.
 */
public class ScreenSizeExample {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Try Lanterna for real terminal detection
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();

            // Method 1: Using ACCEPT ... FROM LINES / COLUMNS equivalent
            System.out.println("Using Lanterna terminal.getTerminalSize() to get screen size:");

            for (int i = 0; i < 3; i++) {
                TerminalSize size = terminal.getTerminalSize();
                displayScreenSize(size.getColumns(), size.getRows());
                System.out.println("Resize and press enter to continue");
                scanner.nextLine();
            }

            // Method 2: Same API (CBL_GET_SCR_SIZE equivalent)
            System.out.println("Using same API (equivalent of CBL_GET_SCR_SIZE):");

            for (int i = 0; i < 3; i++) {
                TerminalSize size = terminal.getTerminalSize();
                displayScreenSize(size.getColumns(), size.getRows());
                System.out.println("Resize and press enter to continue");
                scanner.nextLine();
            }

            terminal.close();
        } catch (IOException e) {
            // Fallback: use environment variables or defaults
            System.out.println("Terminal not available. Using environment variables:");
            String lines = System.getenv("LINES");
            String cols = System.getenv("COLUMNS");
            int numLines = lines != null ? Integer.parseInt(lines) : 24;
            int numCols = cols != null ? Integer.parseInt(cols) : 80;
            displayScreenSize(numCols, numLines);
        }

        System.out.println("Done.");
        scanner.close();
    }

    private static void displayScreenSize(int cols, int lines) {
        System.out.println("-------------------------------------------------------------");
        System.out.println("Current screen size: ");
        System.out.println("Columns: " + String.format("%3d", cols));
        System.out.println("  Lines: " + String.format("%3d", lines));
    }
}
