package com.coboldemo.screensize;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Scanner;

/**
 * Java port of screen_size/get_screen_size.cbl.
 *
 * The original queries terminal dimensions three times via ACCEPT FROM
 * LINES/COLUMNS and three more via CBL_GET_SCR_SIZE. In Java we shell out to
 * {@code tput} on POSIX and {@code mode con} on Windows; both paths run the
 * same code so the two halves of the output look similar.
 */
public final class ScreenSizeTest {

    private ScreenSizeTest() {
    }

    public static void main(String[] args) {
        System.out.println("Using 'ACCEPT ... FROM LINES' and 'ACCEPT ... "
                + "FROM COLUMNS' to get screen size:");
        promptLoop(3);

        System.out.println();
        System.out.println("Using 'CBL_GET_SCR_SIZE' to get screen size:");
        promptLoop(3);

        System.out.println("Done.");
    }

    private static void promptLoop(int repetitions) {
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < repetitions; i++) {
            int[] size = detectScreenSize();
            System.out.println("-----------------------------------------"
                    + "-------------------");
            System.out.println("Current screen size:");
            System.out.println("Columns: " + String.format("%3d", size[1]));
            System.out.println("  Lines: " + String.format("%3d", size[0]));
            if (i < repetitions - 1) {
                System.out.print("Resize and press enter to continue");
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
            }
        }
    }

    static int[] detectScreenSize() {
        String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        if (os.contains("win")) {
            return detectViaModeCon();
        }
        int lines = runCapturingInt("tput", "lines");
        int cols = runCapturingInt("tput", "cols");
        return new int[]{lines > 0 ? lines : 24, cols > 0 ? cols : 80};
    }

    private static int[] detectViaModeCon() {
        try {
            Process p = new ProcessBuilder("cmd", "/c", "mode", "con")
                    .redirectErrorStream(true)
                    .start();
            int lines = 24;
            int cols = 80;
            try (BufferedReader r = new BufferedReader(
                    new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = r.readLine()) != null) {
                    String trimmed = line.trim().toLowerCase(Locale.ROOT);
                    if (trimmed.startsWith("lines:")
                            || trimmed.startsWith("rows:")) {
                        lines = extractTrailingInt(trimmed, lines);
                    } else if (trimmed.startsWith("columns:")
                            || trimmed.startsWith("cols:")) {
                        cols = extractTrailingInt(trimmed, cols);
                    }
                }
            }
            p.waitFor();
            return new int[]{lines, cols};
        } catch (Exception ignored) {
            return new int[]{24, 80};
        }
    }

    private static int extractTrailingInt(String line, int fallback) {
        String[] parts = line.split("\\s+");
        for (int i = parts.length - 1; i >= 0; i--) {
            try {
                return Integer.parseInt(parts[i]);
            } catch (NumberFormatException ignored) {
                // try the next token
            }
        }
        return fallback;
    }

    private static int runCapturingInt(String... command) {
        try {
            Process p = new ProcessBuilder(command)
                    .redirectErrorStream(true)
                    .start();
            try (BufferedReader r = new BufferedReader(
                    new InputStreamReader(p.getInputStream()))) {
                String value = r.readLine();
                p.waitFor();
                return value == null || value.isBlank()
                        ? -1
                        : Integer.parseInt(value.trim());
            }
        } catch (Exception ignored) {
            return -1;
        }
    }
}
