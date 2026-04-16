package com.coboldemo.display;

import java.util.Scanner;

/**
 * Migrated from: display_timing/display_timing.cbl
 * Original author: Erik Eriksen (2021-08-29)
 * Purpose: Testing screen writing speed between two different display
 * positioning syntaxes. In Java, both syntaxes use ANSI escape codes,
 * so this becomes a benchmark of ANSI cursor-move + print performance.
 *
 * COBOL-to-Java mapping:
 *   ACCEPT ws-start-time FROM TIME  -> System.nanoTime()
 *   DISPLAY "@" AT ws-screen-pos    -> ANSI \033[row;colH + print "@"
 *   DISPLAY "@" LINE r COLUMN c     -> ANSI \033[r;cH + print "@" (same in Java)
 *   Time diff computation           -> (endTime - startTime) in nanos -> seconds
 *
 * The original COBOL program runs 10 iterations of:
 *   - 100 screen refreshes of a 20x80 grid filled with "@"
 *   - Measures time for "AT RRCC" syntax vs "LINE R COLUMN C" syntax
 *   - Computes and displays averages
 *
 * In Java, both approaches use the same ANSI escape code, so timing
 * differences are negligible. We preserve the benchmark structure for
 * behavioral fidelity.
 */
public class DisplayTiming {

    // COBOL: 01 ws-max-rows constant as 20.
    private static final int MAX_ROWS = 20;
    // COBOL: 01 ws-max-cols constant as 80.
    private static final int MAX_COLS = 80;
    // COBOL: 01 ws-max-times-to-run constant as 10.
    private static final int MAX_TIMES_TO_RUN = 10;
    // Number of screen refreshes per iteration
    private static final int TIMES_TO_REFRESH = 100;

    /**
     * Moves the cursor to the specified row and column using ANSI escape codes.
     */
    private static void moveCursor(int row, int col) {
        System.out.printf("\033[%d;%dH", row, col);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Press enter to start...");
        scanner.nextLine();

        // Clear screen (COBOL: display spaces with blank screen)
        System.out.print("\033[2J");

        // Arrays to store time differences for each iteration (in nanoseconds)
        long[] timeDiffsMethod1 = new long[MAX_TIMES_TO_RUN];
        long[] timeDiffsMethod2 = new long[MAX_TIMES_TO_RUN];

        // ---------------------------------------------------------------
        // Method 1: Using "AT RRCC" syntax (COBOL "display '@' at ws-screen-position")
        // In Java, this is: moveCursor(row, col) + print("@")
        // ---------------------------------------------------------------
        for (int iteration = 0; iteration < MAX_TIMES_TO_RUN; iteration++) {
            long startTime = System.nanoTime();

            for (int refresh = 0; refresh < TIMES_TO_REFRESH; refresh++) {
                for (int row = 1; row <= MAX_ROWS; row++) {
                    for (int col = 1; col <= MAX_COLS; col++) {
                        // COBOL: display "@" at ws-screen-position
                        moveCursor(row, col);
                        System.out.print("@");
                    }
                }
            }

            long endTime = System.nanoTime();
            timeDiffsMethod1[iteration] = endTime - startTime;

            // Display timing info at bottom of screen (COBOL lines 25-27)
            moveCursor(25, 1);
            System.out.printf("Iteration %2d - Start: %d ns", iteration + 1, startTime);
            moveCursor(26, 1);
            System.out.printf("               End:   %d ns", endTime);
            moveCursor(27, 1);
            System.out.printf("               Diff:  %.2f seconds",
                    timeDiffsMethod1[iteration] / 1_000_000_000.0);
        }

        // Clear screen and display averages for Method 1
        System.out.print("\033[2J");
        displayAverage("Method 1 (AT RRCC syntax)", timeDiffsMethod1);

        // Clear screen for Method 2
        System.out.print("\033[2J");

        // ---------------------------------------------------------------
        // Method 2: Using "LINE R COLUMN C" syntax
        // (COBOL: display "@" line ws-row-idx column ws-col-idx)
        // In Java, this is identical to Method 1 since both use ANSI escapes.
        // We preserve the dual-benchmark structure for behavioral fidelity.
        // ---------------------------------------------------------------
        for (int iteration = 0; iteration < MAX_TIMES_TO_RUN; iteration++) {
            long startTime = System.nanoTime();

            for (int refresh = 0; refresh < TIMES_TO_REFRESH; refresh++) {
                for (int row = 1; row <= MAX_ROWS; row++) {
                    for (int col = 1; col <= MAX_COLS; col++) {
                        // COBOL: display "@" line ws-row-idx column ws-col-idx
                        moveCursor(row, col);
                        System.out.print("@");
                    }
                }
            }

            long endTime = System.nanoTime();
            timeDiffsMethod2[iteration] = endTime - startTime;

            // Display timing info
            moveCursor(25, 1);
            System.out.printf("Iteration %2d - Start: %d ns", iteration + 1, startTime);
            moveCursor(26, 1);
            System.out.printf("               End:   %d ns", endTime);
            moveCursor(27, 1);
            System.out.printf("               Diff:  %.2f seconds",
                    timeDiffsMethod2[iteration] / 1_000_000_000.0);
        }

        // Clear screen and display averages for Method 2
        System.out.print("\033[2J");
        displayAverage("Method 2 (LINE/COLUMN syntax)", timeDiffsMethod2);

        scanner.nextLine();
        scanner.close();
    }

    /**
     * Computes and displays the average timing, mimicking the COBOL
     * compute-and-display-average paragraph.
     *
     * @param label description of the method being measured
     * @param timeDiffs array of time differences in nanoseconds
     */
    private static void displayAverage(String label, long[] timeDiffs) {
        moveCursor(1, 1);
        System.out.println(label + " - Individual iterations:");

        long totalNanos = 0;
        for (int i = 0; i < timeDiffs.length; i++) {
            double seconds = timeDiffs[i] / 1_000_000_000.0;
            moveCursor(i + 2, 1);
            System.out.printf("  Iteration %2d: %6.2f seconds%n", i + 1, seconds);
            totalNanos += timeDiffs[i];
        }

        // COBOL: compute ws-time-diff-sec-avg = ws-time-diff-sec-avg / ws-max-times-to-run
        double avgSeconds = (totalNanos / (double) timeDiffs.length) / 1_000_000_000.0;

        moveCursor(13, 1);
        System.out.printf("Average: %.2f seconds%n", avgSeconds);
        moveCursor(15, 1);
        System.out.print("Press enter to continue...");
    }
}
