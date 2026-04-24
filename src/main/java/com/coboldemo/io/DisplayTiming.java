package com.coboldemo.io;

/**
 * Migrated from: display_timing/display_timing.cbl
 *
 * Tests screen-writing speed by filling a 20x80 grid with '@' characters
 * across 10 iterations (each doing 100 refreshes), then computing the
 * average elapsed time. Two methods are compared (in COBOL these used
 * different position syntaxes; in Java both use System.out).
 */
public class DisplayTiming {

    private static final int MAX_ROWS = 20;
    private static final int MAX_COLS = 80;
    private static final int MAX_TIMES_TO_RUN = 10;
    private static final int REFRESHES_PER_RUN = 100;

    public static void main(String[] args) {
        System.out.println("Press Enter to start...");
        try { System.in.read(); } catch (Exception ignored) {}

        long[] diffs = new long[MAX_TIMES_TO_RUN];

        // Method 1: using print for each character
        System.out.println("Method 1: Character-by-character print");
        for (int run = 0; run < MAX_TIMES_TO_RUN; run++) {
            long startTime = System.nanoTime();

            for (int refresh = 0; refresh < REFRESHES_PER_RUN; refresh++) {
                for (int row = 0; row < MAX_ROWS; row++) {
                    for (int col = 0; col < MAX_COLS; col++) {
                        System.out.print("@");
                    }
                    System.out.println();
                }
            }

            long endTime = System.nanoTime();
            diffs[run] = endTime - startTime;
            System.out.printf("Run %2d: %d.%02d seconds%n",
                    run + 1,
                    diffs[run] / 1_000_000_000,
                    (diffs[run] % 1_000_000_000) / 10_000_000);
        }

        printAverage("Method 1", diffs);

        // Method 2: using StringBuilder for row-at-a-time
        System.out.println("\nMethod 2: Row-at-a-time StringBuilder");
        for (int run = 0; run < MAX_TIMES_TO_RUN; run++) {
            long startTime = System.nanoTime();

            StringBuilder row = new StringBuilder(MAX_COLS);
            for (int refresh = 0; refresh < REFRESHES_PER_RUN; refresh++) {
                for (int r = 0; r < MAX_ROWS; r++) {
                    row.setLength(0);
                    for (int col = 0; col < MAX_COLS; col++) {
                        row.append('@');
                    }
                    System.out.println(row);
                }
            }

            long endTime = System.nanoTime();
            diffs[run] = endTime - startTime;
            System.out.printf("Run %2d: %d.%02d seconds%n",
                    run + 1,
                    diffs[run] / 1_000_000_000,
                    (diffs[run] % 1_000_000_000) / 10_000_000);
        }

        printAverage("Method 2", diffs);
    }

    private static void printAverage(String method, long[] diffs) {
        long total = 0;
        for (long d : diffs) {
            total += d;
        }
        long avg = total / diffs.length;
        System.out.printf("%s Average: %d.%02d seconds%n",
                method,
                avg / 1_000_000_000,
                (avg % 1_000_000_000) / 10_000_000);
    }
}
