package com.coboldemo.display;

/**
 * Java equivalent of {@code display_timing/display_timing.cbl}.
 *
 * <p>The original COBOL program writes a single character to every
 * cell of a 20x80 grid, repeats that 100 times, and times each
 * iteration. It then averages the timings across ten runs. The whole
 * point is to compare the relative speed of two GnuCOBOL display
 * syntaxes ({@code DISPLAY ... AT row,col} versus
 * {@code DISPLAY ... LINE r COLUMN c}).</p>
 *
 * <p>This Java port keeps the same shape: it runs the same workload
 * twice using two slightly different output techniques and reports
 * timings using {@link System#nanoTime()}, which is the standard JDK
 * primitive for measuring elapsed time at sub-millisecond resolution
 * (analogous to {@code ACCEPT ws-time FROM TIME}).</p>
 *
 * <p>Output is buffered to stderr (the workload) and stdout (the
 * results) so you can run this non-interactively without flooding the
 * terminal: pipe stderr to {@code /dev/null} if you only care about
 * the timings.</p>
 */
public class DisplayTiming {

    private static final int MAX_ROWS = 20;
    private static final int MAX_COLS = 80;
    private static final int MAX_TIMES_TO_RUN = 10;
    private static final int REFRESHES_PER_RUN = 100;

    public static void main(String[] args) {
        System.out.println("Press enter to start... (skipped when no TTY attached)");

        long[] concatTimingsMs = runWorkload("concat-style", DisplayTiming::workloadConcatStyle);
        printAverages("concat-style (DISPLAY ... AT)", concatTimingsMs);

        long[] formattedTimingsMs = runWorkload("printf-style", DisplayTiming::workloadFormattedStyle);
        printAverages("printf-style (DISPLAY ... LINE/COLUMN)", formattedTimingsMs);
    }

    private static long[] runWorkload(String label, Runnable workload) {
        long[] millis = new long[MAX_TIMES_TO_RUN];
        for (int i = 0; i < MAX_TIMES_TO_RUN; i++) {
            long start = System.nanoTime();
            for (int repeat = 0; repeat < REFRESHES_PER_RUN; repeat++) {
                workload.run();
            }
            long end = System.nanoTime();
            millis[i] = (end - start) / 1_000_000;
            System.out.println(String.format(
                    "[%s] run %2d: elapsed = %4d ms",
                    label, i + 1, millis[i]));
        }
        return millis;
    }

    /**
     * "concat-style" workload: build each row as a single string and
     * push it through {@code System.err.print}. This mirrors the
     * COBOL program's first loop, which uses
     * {@code DISPLAY "@" AT ws-screen-position} to render one cell at
     * a time.
     */
    private static void workloadConcatStyle() {
        StringBuilder sb = new StringBuilder(MAX_ROWS * (MAX_COLS + 1));
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                sb.append('@');
            }
            sb.append('\n');
        }
        // stderr keeps the workload output out of the timing summary
        // when stdout is captured.
        System.err.print(sb);
    }

    /**
     * "printf-style" workload: emit each cell with a separate
     * {@code System.err.printf}. This is the verbose / per-call
     * variant, mirroring the second COBOL loop.
     */
    private static void workloadFormattedStyle() {
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                System.err.printf("%c", '@');
            }
            System.err.printf("%n");
        }
    }

    private static void printAverages(String label, long[] millis) {
        long total = 0;
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        for (long ms : millis) {
            total += ms;
            if (ms < min) min = ms;
            if (ms > max) max = ms;
        }
        double avg = (double) total / millis.length;
        System.out.println("---- " + label + " ----");
        System.out.println(String.format("runs:    %d", millis.length));
        System.out.println(String.format("min:     %d ms", min));
        System.out.println(String.format("max:     %d ms", max));
        System.out.println(String.format("average: %.2f ms", avg));
    }
}
