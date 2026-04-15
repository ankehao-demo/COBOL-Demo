package com.coboldemo.display;

import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

/**
 * Java equivalent of display_timing/display_timing.cbl
 *
 * Benchmarks screen-writing speed by filling a 20x80 grid with '@' characters
 * 100 times per run, over 10 runs per method. Reports per-run timings and
 * averages. The original COBOL program compares two different positioning
 * syntaxes; here we compare two approaches to console output.
 */
public class DisplayTimingTest {

    private static final int MAX_ROWS = 20;
    private static final int MAX_COLS = 80;
    private static final int MAX_TIMES_TO_RUN = 10;
    private static final int REFRESHES_PER_RUN = 100;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press enter to start...");
        scanner.nextLine();

        // Method 1: Individual character prints (simulates DISPLAY "@" AT position)
        System.out.println("Method 1: Individual System.out.print per character");
        long[] diffs1 = new long[MAX_TIMES_TO_RUN];

        for (int run = 0; run < MAX_TIMES_TO_RUN; run++) {
            Instant start = Instant.now();

            for (int refresh = 0; refresh < REFRESHES_PER_RUN; refresh++) {
                for (int row = 0; row < MAX_ROWS; row++) {
                    for (int col = 0; col < MAX_COLS; col++) {
                        System.out.print("@");
                    }
                    System.out.println();
                }
            }

            Instant end = Instant.now();
            diffs1[run] = Duration.between(start, end).toMillis();
            System.out.printf("Run %2d: %d ms%n", run + 1, diffs1[run]);
        }

        displayAverage("Method 1", diffs1);

        // Method 2: Line-at-a-time with StringBuilder (simulates LINE/COLUMN syntax)
        System.out.println("\nMethod 2: StringBuilder line-at-a-time");
        long[] diffs2 = new long[MAX_TIMES_TO_RUN];

        for (int run = 0; run < MAX_TIMES_TO_RUN; run++) {
            Instant start = Instant.now();

            for (int refresh = 0; refresh < REFRESHES_PER_RUN; refresh++) {
                StringBuilder sb = new StringBuilder(MAX_COLS);
                for (int row = 0; row < MAX_ROWS; row++) {
                    sb.setLength(0);
                    for (int col = 0; col < MAX_COLS; col++) {
                        sb.append("@");
                    }
                    System.out.println(sb);
                }
            }

            Instant end = Instant.now();
            diffs2[run] = Duration.between(start, end).toMillis();
            System.out.printf("Run %2d: %d ms%n", run + 1, diffs2[run]);
        }

        displayAverage("Method 2", diffs2);

        scanner.close();
    }

    private static void displayAverage(String label, long[] diffs) {
        long totalMs = 0;
        for (long d : diffs) {
            totalMs += d;
        }
        double avgMs = (double) totalMs / diffs.length;
        System.out.printf("%s average: %.2f ms%n", label, avgMs);
    }
}
