package com.coboldemo.screensize;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Port of {@code screen_size/get_screen_size.cbl} — attempts to determine
 * the terminal dimensions.
 *
 * <p>GnuCOBOL exposes {@code ACCEPT ... FROM LINES/COLUMNS} and the
 * {@code CBL_GET_SCR_SIZE} runtime call. Java has no portable equivalent;
 * the best effort on Linux is to read the {@code COLUMNS} / {@code LINES}
 * environment variables (when the shell exports them) or invoke
 * {@code tput cols} / {@code tput lines}.
 */
public final class ScreenSizeTest {

    private ScreenSizeTest() {
    }

    public static void main(String[] args) {
        System.out.println("Using environment variables to get screen size:");
        reportFromEnv();
        System.out.println();
        System.out.println("Using 'tput' to get screen size:");
        reportFromTput();
        System.out.println();
        System.out.println("Done.");
    }

    private static void reportFromEnv() {
        String columns = System.getenv("COLUMNS");
        String lines = System.getenv("LINES");
        printSize(columns, lines);
    }

    private static void reportFromTput() {
        String cols = runTput("cols");
        String lines = runTput("lines");
        printSize(cols, lines);
    }

    private static String runTput(String arg) {
        try {
            ProcessBuilder builder = new ProcessBuilder("tput", arg);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                process.waitFor();
                return line == null ? null : line.trim();
            }
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

    private static void printSize(String columns, String lines) {
        System.out.println("-------------------------------------------------");
        System.out.println("Current screen size:");
        System.out.println("Columns: " + (columns == null ? "<unknown>" : columns));
        System.out.println("  Lines: " + (lines == null ? "<unknown>" : lines));
    }
}
