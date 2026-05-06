package com.example.cobol.displaytiming;

import java.util.concurrent.TimeUnit;

import com.example.cobol.AnsiTerm;

/**
 * Java port of {@code display_timing/display_timing.cbl}.
 *
 * <p>The original program benchmarked two different syntaxes for COBOL
 * positioned {@code DISPLAY} statements ({@code AT yyxx} vs
 * {@code LINE yy COLUMN xx}). Java only has one mechanism — the ANSI cursor
 * move — so we just measure how long it takes to repeatedly fill a 20×80
 * grid with '@' characters and report the average duration.
 */
public final class DisplayTiming {

    private static final int MAX_ROWS = 20;
    private static final int MAX_COLS = 80;
    private static final int MAX_TIMES_TO_RUN = 10;
    private static final int REFRESH_PER_RUN = 100;

    private DisplayTiming() {}

    public static void main(String[] args) {
        long[] runMillis = new long[MAX_TIMES_TO_RUN];

        // Clear the screen before we start drawing.
        System.out.print(AnsiTerm.clearScreen());

        for (int run = 0; run < MAX_TIMES_TO_RUN; run++) {
            long startNs = System.nanoTime();

            for (int refresh = 0; refresh < REFRESH_PER_RUN; refresh++) {
                StringBuilder buf = new StringBuilder();
                for (int row = 1; row <= MAX_ROWS; row++) {
                    for (int col = 1; col <= MAX_COLS; col++) {
                        buf.append(AnsiTerm.moveTo(row, col)).append('@');
                    }
                }
                System.out.print(buf);
            }

            long elapsedNs = System.nanoTime() - startNs;
            runMillis[run] = TimeUnit.NANOSECONDS.toMillis(elapsedNs);
        }

        // Reset cursor + clear before printing the report.
        System.out.print(AnsiTerm.clearScreen());

        long total = 0;
        for (int i = 0; i < runMillis.length; i++) {
            System.out.printf("Run %2d: %5d ms%n", i + 1, runMillis[i]);
            total += runMillis[i];
        }
        long avg = total / MAX_TIMES_TO_RUN;
        System.out.println();
        System.out.printf("Average over %d runs: %d ms (%.2f s)%n",
                MAX_TIMES_TO_RUN, avg, avg / 1000.0);
    }
}
