package com.example.cobol.screensize;

import java.io.IOException;
import java.util.Scanner;

import com.example.cobol.AnsiTerm;

/**
 * Java port of {@code screen_size/get_screen_size.cbl}.
 *
 * <p>Java has no portable terminal-size API in the JDK. We try a few
 * sources in order:
 * <ol>
 *   <li>{@code COLUMNS} / {@code LINES} env vars</li>
 *   <li>{@code stty size} (POSIX systems)</li>
 *   <li>fallback to 80×24</li>
 * </ol>
 */
public final class GetScreenSize {

    private GetScreenSize() {}

    public static void main(String[] args) {
        System.out.print(AnsiTerm.clearScreen());
        System.out.println(
            "Using env LINES/COLUMNS or 'stty size' to get screen size:");

        try (Scanner in = new Scanner(System.in)) {
            for (int i = 0; i < 3; i++) {
                int[] size = detectSize();
                displayScreenSize(size[0], size[1]);
                System.out.print("Resize and press enter to continue ");
                if (in.hasNextLine()) {
                    in.nextLine();
                }
            }
        }

        System.out.println("Done.");
    }

    private static void displayScreenSize(int rows, int cols) {
        System.out.println("---------------------------------------------------------");
        System.out.println("Current screen size:");
        System.out.printf("Columns: %3d%n", cols);
        System.out.printf("  Lines: %3d%n", rows);
    }

    private static int[] detectSize() {
        int rows = parseEnv("LINES", -1);
        int cols = parseEnv("COLUMNS", -1);
        if (rows > 0 && cols > 0) {
            return new int[] {rows, cols};
        }

        // Try `stty size` — outputs "rows cols"
        try {
            ProcessBuilder pb = new ProcessBuilder("sh", "-c", "stty size < /dev/tty");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            String out = new String(p.getInputStream().readAllBytes()).trim();
            p.waitFor();
            String[] parts = out.split("\\s+");
            if (parts.length == 2) {
                return new int[] {
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1])
                };
            }
        } catch (IOException | InterruptedException | NumberFormatException ignored) {
            // fall through to default
        }

        return new int[] {24, 80};
    }

    private static int parseEnv(String name, int fallback) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            return fallback;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
