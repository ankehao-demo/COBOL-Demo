package com.coboldemo.screensize;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Java port of {@code screen_size/get_screen_size.cbl}.
 *
 * Detects the terminal size three times in succession, prompting the user
 * to resize the terminal between probes. Mirrors both halves of the
 * original program (ACCEPT FROM LINES/COLUMNS and CBL_GET_SCR_SIZE) by
 * shelling out to {@code tput lines} / {@code tput cols} and falling back
 * to the {@code LINES} / {@code COLUMNS} environment variables.
 */
public class ScreenSizeTest {

    public static void main(String[] args) {
        clearScreen();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Using 'ACCEPT ... FROM LINES' and 'ACCEPT ... FROM COLUMNS' "
                + "to get screen size:");

        for (int i = 0; i < 3; i++) {
            int[] size = detectScreenSizeViaTput();
            displaySize(size);
            System.out.println("Resize and press enter to continue");
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
        }

        clearScreen();
        System.out.println("Using 'CBL_GET_SCR_SIZE' to get screen size:");

        for (int i = 0; i < 3; i++) {
            // A second strategy that also looks at LINES/COLUMNS env vars.
            int[] size = detectScreenSizeViaEnv();
            displaySize(size);
            System.out.println("Resize and press enter to continue");
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
        }

        System.out.println("Done.");
    }

    private static void displaySize(int[] size) {
        System.out.println("-------------------------------------------------------------");
        System.out.println("Current screen size: ");
        System.out.println(String.format("Columns: %3d", size[1]));
        System.out.println(String.format("  Lines: %3d", size[0]));
    }

    private static int[] detectScreenSizeViaTput() {
        int lines = runTput("lines");
        int cols = runTput("cols");
        if (lines == 0) {
            lines = parseEnv("LINES", 24);
        }
        if (cols == 0) {
            cols = parseEnv("COLUMNS", 80);
        }
        return new int[] {lines, cols};
    }

    private static int[] detectScreenSizeViaEnv() {
        int lines = parseEnv("LINES", 0);
        int cols = parseEnv("COLUMNS", 0);
        if (lines == 0) {
            lines = runTput("lines");
        }
        if (cols == 0) {
            cols = runTput("cols");
        }
        if (lines == 0) {
            lines = 24;
        }
        if (cols == 0) {
            cols = 80;
        }
        return new int[] {lines, cols};
    }

    private static int runTput(String arg) {
        try {
            Process p = new ProcessBuilder("tput", arg).redirectErrorStream(true).start();
            try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line = r.readLine();
                p.waitFor();
                if (line != null && !line.isBlank()) {
                    return Integer.parseInt(line.trim());
                }
            }
        } catch (Exception e) {
            // Ignored: tput is not available on every platform.
        }
        return 0;
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

    private static void clearScreen() {
        System.out.print("\u001b[2J\u001b[H");
    }
}
