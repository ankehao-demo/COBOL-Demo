package com.coboldemo.displaytiming;

/**
 * Java port of display_timing/display_timing.cbl.
 *
 * The original benchmarks the difference between two COBOL screen-mode write
 * syntaxes: {@code DISPLAY "@" AT ws-screen-position} vs
 * {@code DISPLAY "@" LINE x COLUMN y}. In Java both resolve to the same ANSI
 * escape sequence; we still run the two loops so the output mirrors the
 * original and lets us compare raw throughput.
 */
public final class DisplayTiming {

    private static final int MAX_ROWS = 20;
    private static final int MAX_COLS = 80;
    private static final int TIMES_TO_RUN = 10;
    private static final int REFRESHES_PER_RUN = 100;

    private DisplayTiming() {
    }

    public static void main(String[] args) {
        System.out.println("Press enter to start...");
        try {
            // Consume one line from stdin to mirror ACCEPT ws-accept.
            int ignored = System.in.read();
            if (ignored != -1) {
                while (System.in.available() > 0 && System.in.read() != '\n') {
                    // drain
                }
            }
        } catch (Exception ignored) {
            // If stdin is closed we still run.
        }

        long[] runAtMillis = runBenchmark(true);
        System.out.println();
        printAverage("'DISPLAY AT ws-screen-position' timing", runAtMillis);

        long[] runLineColMillis = runBenchmark(false);
        System.out.println();
        printAverage("'DISPLAY LINE x COLUMN y' timing", runLineColMillis);

        System.out.println("Done.");
    }

    private static long[] runBenchmark(boolean useAt) {
        long[] millis = new long[TIMES_TO_RUN];
        StringBuilder sink = new StringBuilder(MAX_ROWS * MAX_COLS * 8);
        for (int run = 0; run < TIMES_TO_RUN; run++) {
            long start = System.nanoTime();
            for (int refresh = 0; refresh < REFRESHES_PER_RUN; refresh++) {
                sink.setLength(0);
                for (int row = 1; row <= MAX_ROWS; row++) {
                    for (int col = 1; col <= MAX_COLS; col++) {
                        // Both syntaxes map to the same ANSI positioning.
                        sink.append("\033[").append(row).append(';')
                                .append(col).append('H').append('@');
                    }
                }
                // Actually emit once per refresh so System.out work is counted.
                System.out.print(sink);
            }
            long end = System.nanoTime();
            millis[run] = (end - start) / 1_000_000L;
            System.out.println();
            System.out.println((useAt ? "AT" : "LINE/COL") + " run "
                    + (run + 1) + ": " + millis[run] + " ms");
        }
        return millis;
    }

    private static void printAverage(String label, long[] millis) {
        long sum = 0;
        for (long m : millis) {
            sum += m;
        }
        double avg = sum / (double) millis.length;
        System.out.println(label + " average: "
                + String.format("%.2f", avg) + " ms");
    }
}
