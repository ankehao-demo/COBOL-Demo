package com.coboldemo.display;

import java.util.Scanner;

/**
 * Migrated from display_timing/display_timing.cbl
 * Benchmarks screen writing speed between two display methods.
 */
public class DisplayTiming {

    private static final int MAX_ROWS = 20;
    private static final int MAX_COLS = 80;
    private static final int MAX_TIMES_TO_RUN = 10;
    private static final int REFRESHES_PER_RUN = 100;
    private static final String ESC = "\033[";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press enter to start...");
        scanner.nextLine();

        // Clear screen
        System.out.print(ESC + "2J");

        long[] diffs = new long[MAX_TIMES_TO_RUN];

        // Method 1: Character-by-character with ANSI positioning
        System.out.println("Method 1: Character-by-character ANSI positioning");
        for (int run = 0; run < MAX_TIMES_TO_RUN; run++) {
            long startTime = System.nanoTime();

            for (int refresh = 0; refresh < REFRESHES_PER_RUN; refresh++) {
                for (int row = 1; row <= MAX_ROWS; row++) {
                    for (int col = 1; col <= MAX_COLS; col++) {
                        System.out.print(ESC + row + ";" + col + "H@");
                    }
                }
            }

            long endTime = System.nanoTime();
            diffs[run] = endTime - startTime;
            displayDiff(run + 1, diffs[run]);
        }

        System.out.print(ESC + "2J");
        displayAverage(diffs);

        System.out.print(ESC + "2J");

        // Method 2: Line-by-line with StringBuilder
        System.out.println("Method 2: Line-by-line StringBuilder");
        for (int run = 0; run < MAX_TIMES_TO_RUN; run++) {
            long startTime = System.nanoTime();

            for (int refresh = 0; refresh < REFRESHES_PER_RUN; refresh++) {
                for (int row = 1; row <= MAX_ROWS; row++) {
                    StringBuilder line = new StringBuilder(MAX_COLS);
                    for (int col = 0; col < MAX_COLS; col++) {
                        line.append('@');
                    }
                    System.out.print(ESC + row + ";1H" + line);
                }
            }

            long endTime = System.nanoTime();
            diffs[run] = endTime - startTime;
            displayDiff(run + 1, diffs[run]);
        }

        System.out.print(ESC + "2J");
        displayAverage(diffs);

        scanner.close();
    }

    private static void displayDiff(int run, long nanos) {
        long millis = nanos / 1_000_000;
        long secs = millis / 1000;
        long remainMillis = millis % 1000;
        System.out.println(String.format("Run %2d: %d.%03d seconds", run, secs, remainMillis));
    }

    private static void displayAverage(long[] diffs) {
        long total = 0;
        for (long d : diffs) {
            total += d;
        }
        long avgNanos = total / diffs.length;
        long avgMillis = avgNanos / 1_000_000;
        long avgSecs = avgMillis / 1000;
        long avgRemainMillis = avgMillis % 1000;
        System.out.println(String.format("Average: %d.%03d seconds", avgSecs, avgRemainMillis));
    }
}
