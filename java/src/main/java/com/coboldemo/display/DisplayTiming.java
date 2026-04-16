package com.coboldemo.display;

import java.util.Scanner;

/**
 * Migrated from: display_timing/display_timing.cbl
 * Original author: Erik Eriksen (2021-08-29)
 * Purpose: Testing screen writing speed between two different display position syntaxes.
 *
 * In Java, we benchmark two approaches to writing characters to the console:
 * 1) Using ANSI escape codes with formatted position strings
 * 2) Using System.out.printf with position formatting
 *
 * Both use ANSI escape codes since Java has no native screen-mode positioning.
 */
public class DisplayTiming {

    private static final int MAX_ROWS = 20;
    private static final int MAX_COLS = 80;
    private static final int MAX_TIMES_TO_RUN = 10;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Press enter to start...");
        scanner.nextLine();

        // Clear screen
        System.out.print("\033[2J");

        // --- Test 1: Using String.format for position ---
        long[] diffs1 = new long[MAX_TIMES_TO_RUN];

        for (int run = 0; run < MAX_TIMES_TO_RUN; run++) {
            long startTime = System.nanoTime();

            for (int refresh = 0; refresh < 100; refresh++) {
                for (int row = 1; row <= MAX_ROWS; row++) {
                    for (int col = 1; col <= MAX_COLS; col++) {
                        System.out.print(String.format("\033[%d;%dH@", row, col));
                    }
                }
            }

            long endTime = System.nanoTime();
            diffs1[run] = endTime - startTime;

            displayAt(25, 1, "Run " + (run + 1) + ": " + formatNanos(diffs1[run]));
        }

        System.out.print("\033[2J"); // clear screen
        displayAverageResults("Method 1 (String.format positioning)", diffs1, scanner);

        System.out.print("\033[2J"); // clear screen

        // --- Test 2: Using printf for position ---
        long[] diffs2 = new long[MAX_TIMES_TO_RUN];

        for (int run = 0; run < MAX_TIMES_TO_RUN; run++) {
            long startTime = System.nanoTime();

            for (int refresh = 0; refresh < 100; refresh++) {
                for (int row = 1; row <= MAX_ROWS; row++) {
                    for (int col = 1; col <= MAX_COLS; col++) {
                        System.out.printf("\033[%d;%dH@", row, col);
                    }
                }
            }

            long endTime = System.nanoTime();
            diffs2[run] = endTime - startTime;

            displayAt(25, 1, "Run " + (run + 1) + ": " + formatNanos(diffs2[run]));
        }

        System.out.print("\033[2J"); // clear screen
        displayAverageResults("Method 2 (printf positioning)", diffs2, scanner);
    }

    private static void displayAverageResults(String label, long[] diffs, Scanner scanner) {
        System.out.println(label);
        System.out.println("Individual run times:");

        long total = 0;
        for (int i = 0; i < diffs.length; i++) {
            displayAt(i + 1, 1, "Run " + (i + 1) + ": " + formatNanos(diffs[i]));
            total += diffs[i];
        }

        long avg = total / diffs.length;
        displayAt(12, 1, "Total: " + formatNanos(total));
        displayAt(13, 1, "Average: " + formatNanos(avg));

        displayAt(15, 1, "Press enter to continue...");
        scanner.nextLine();
    }

    private static void displayAt(int row, int col, String text) {
        System.out.printf("\033[%d;%dH%s", row, col, text);
    }

    private static String formatNanos(long nanos) {
        long millis = nanos / 1_000_000;
        long secs = millis / 1000;
        long remainMillis = millis % 1000;
        return String.format("%d.%03d seconds", secs, remainMillis);
    }
}
