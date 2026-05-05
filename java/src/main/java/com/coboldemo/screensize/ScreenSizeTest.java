package com.coboldemo.screensize;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Migrated from screen_size/get_screen_size.cbl
 * Demonstrates getting the terminal screen size.
 */
public class ScreenSizeTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Method 1: Using tput commands (ACCEPT ... FROM LINES/COLUMNS equivalent)
        System.out.println("Using 'tput lines' and 'tput cols' to get screen size:");

        for (int i = 0; i < 3; i++) {
            int lines = runCommand("tput", "lines");
            int cols = runCommand("tput", "cols");
            displayScreenSize(lines, cols);
            System.out.println("Resize and press enter to continue");
            scanner.nextLine();
        }

        // Method 2: Using stty size (CBL_GET_SCR_SIZE equivalent)
        System.out.println();
        System.out.println("Using 'stty size' to get screen size:");

        for (int i = 0; i < 3; i++) {
            int[] size = getSttySize();
            displayScreenSize(size[0], size[1]);
            System.out.println("Resize and press enter to continue");
            scanner.nextLine();
        }

        System.out.println("Done.");
        scanner.close();
    }

    private static void displayScreenSize(int lines, int cols) {
        System.out.println("---------------------------------------------------------");
        System.out.println("Current screen size: ");
        System.out.println("Columns: " + (cols > 0 ? String.format("%3d", cols) : "N/A"));
        System.out.println("  Lines: " + (lines > 0 ? String.format("%3d", lines) : "N/A"));
    }

    private static int runCommand(String... command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result = reader.readLine();
            process.waitFor();
            return Integer.parseInt(result.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private static int[] getSttySize() {
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", "stty size < /dev/tty");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result = reader.readLine();
            process.waitFor();
            if (result != null) {
                String[] parts = result.trim().split("\\s+");
                return new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
            }
        } catch (Exception e) {
            // Fall through
        }
        return new int[]{-1, -1};
    }
}
