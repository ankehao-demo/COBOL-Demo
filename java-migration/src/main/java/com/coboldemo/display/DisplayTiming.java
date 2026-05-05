package com.coboldemo.display;

import java.util.Scanner;

/**
 * Java port of {@code display_timing/display_timing.cbl}.
 *
 * Compares the cost of two ways to position a character on screen
 * (positional "AT yyxx" vs LINE/COLUMN). In COBOL these go through
 * different runtime paths; in Java both reduce to the same ANSI escape
 * sequence so this is largely a reproduction of the timing harness.
 *
 * Writes a {@code MAX_ROWS x MAX_COLS} grid of '@' characters
 * {@code MAX_TIMES_TO_RUN x 100} times and reports per-iteration
 * timing plus the average.
 */
public class DisplayTiming {

    private static final int MAX_ROWS = 20;
    private static final int MAX_COLS = 80;
    private static final int MAX_TIMES_TO_RUN = 10;
    private static final int REFRESHES_PER_RUN = 100;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press enter to start...");
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        clearScreen();

        long[] runDurationsNs = new long[MAX_TIMES_TO_RUN];
        for (int run = 0; run < MAX_TIMES_TO_RUN; run++) {
            long start = System.nanoTime();
            for (int r = 0; r < REFRESHES_PER_RUN; r++) {
                paintGridPositional();
            }
            long end = System.nanoTime();
            runDurationsNs[run] = end - start;
            displayDiff("AT yyxx", run + 1, runDurationsNs[run]);
        }

        clearScreen();
        printAverage("AT yyxx", runDurationsNs);

        clearScreen();
        for (int run = 0; run < MAX_TIMES_TO_RUN; run++) {
            long start = System.nanoTime();
            for (int r = 0; r < REFRESHES_PER_RUN; r++) {
                paintGridLineCol();
            }
            long end = System.nanoTime();
            runDurationsNs[run] = end - start;
            displayDiff("LINE COLUMN", run + 1, runDurationsNs[run]);
        }

        clearScreen();
        printAverage("LINE COLUMN", runDurationsNs);
    }

    private static void paintGridPositional() {
        StringBuilder sb = new StringBuilder();
        for (int row = 1; row <= MAX_ROWS; row++) {
            for (int col = 1; col <= MAX_COLS; col++) {
                sb.append(String.format("\u001b[%d;%dH@", row, col));
            }
        }
        System.out.print(sb);
    }

    private static void paintGridLineCol() {
        StringBuilder sb = new StringBuilder();
        for (int row = 1; row <= MAX_ROWS; row++) {
            for (int col = 1; col <= MAX_COLS; col++) {
                sb.append(String.format("\u001b[%d;%dH@", row, col));
            }
        }
        System.out.print(sb);
    }

    private static void displayDiff(String mode, int run, long ns) {
        double seconds = ns / 1_000_000_000.0;
        System.out.printf("[%s] run %2d: %.3f sec (%d ns)%n", mode, run, seconds, ns);
    }

    private static void printAverage(String mode, long[] durationsNs) {
        long total = 0;
        for (long d : durationsNs) {
            total += d;
        }
        double avgSec = (total / (double) durationsNs.length) / 1_000_000_000.0;
        System.out.printf("[%s] average per run: %.3f sec%n", mode, avgSec);
    }

    private static void clearScreen() {
        System.out.print("\u001b[2J\u001b[H");
    }
}
