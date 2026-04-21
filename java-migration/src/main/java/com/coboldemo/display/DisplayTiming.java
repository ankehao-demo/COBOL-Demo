package com.coboldemo.display;

/**
 * Port of {@code display_timing/display_timing.cbl} — benchmarks two ways of
 * writing a 20x80 grid of characters to the screen.
 *
 * <p>The original COBOL program used {@code DISPLAY "@" AT ws-screen-position}
 * to set the cursor via screen mode. Java's {@link System#out} is stream
 * based, so both approaches here write to the stream. The first approach
 * calls {@code print} once per character; the second buffers the whole
 * iteration into a {@link StringBuilder} before a single {@code print}.
 *
 * <p>Console output is suppressed via {@link System#setOut(java.io.PrintStream)}
 * with a no-op stream so the benchmark does not drown the terminal.
 */
public final class DisplayTiming {

    private static final int MAX_ROWS = 20;
    private static final int MAX_COLS = 80;
    private static final int REFRESHES = 100;
    private static final int RUNS = 10;

    private DisplayTiming() {
    }

    public static void main(String[] args) {
        System.out.println("Starting display timing benchmark...");

        long[] perCharTimes = new long[RUNS];
        long[] bufferedTimes = new long[RUNS];

        java.io.PrintStream realOut = System.out;
        java.io.PrintStream devNull = new java.io.PrintStream(java.io.OutputStream.nullOutputStream());

        // Approach 1: one print per character (mirrors DISPLAY "@" AT pos).
        System.setOut(devNull);
        for (int run = 0; run < RUNS; run++) {
            long start = System.nanoTime();
            for (int refresh = 0; refresh < REFRESHES; refresh++) {
                for (int row = 0; row < MAX_ROWS; row++) {
                    for (int col = 0; col < MAX_COLS; col++) {
                        System.out.print('@');
                    }
                }
            }
            perCharTimes[run] = System.nanoTime() - start;
        }
        System.setOut(realOut);
        System.out.println("Approach 1 — print per character:");
        printTimings(perCharTimes);

        // Approach 2: buffer the grid in a StringBuilder then print once.
        System.setOut(devNull);
        for (int run = 0; run < RUNS; run++) {
            long start = System.nanoTime();
            for (int refresh = 0; refresh < REFRESHES; refresh++) {
                StringBuilder sb = new StringBuilder(MAX_ROWS * (MAX_COLS + 1));
                for (int row = 0; row < MAX_ROWS; row++) {
                    for (int col = 0; col < MAX_COLS; col++) {
                        sb.append('@');
                    }
                    sb.append('\n');
                }
                System.out.print(sb);
            }
            bufferedTimes[run] = System.nanoTime() - start;
        }
        System.setOut(realOut);
        System.out.println("Approach 2 — buffered StringBuilder:");
        printTimings(bufferedTimes);
    }

    private static void printTimings(long[] times) {
        long total = 0L;
        for (int i = 0; i < times.length; i++) {
            double seconds = times[i] / 1_000_000_000.0;
            System.out.printf("  run %2d: %.4f s%n", i + 1, seconds);
            total += times[i];
        }
        double avg = (total / (double) times.length) / 1_000_000_000.0;
        System.out.printf("  average: %.4f s%n", avg);
    }
}
