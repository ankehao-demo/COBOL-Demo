package com.coboldemo.accept;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.Scanner;

/**
 * Migrated from: accept/accept_from.cbl
 * Original author: Erik Eriksen (2022-04-18, updated 2022-05-02)
 * Purpose: Examples of using various forms of ACCEPT...FROM...
 *
 * Notes:
 * - ACCEPT FROM LINES/COLUMNS: terminal-specific, approximated with tput on Linux
 * - CBL_GET_SCR_SIZE: approximated using ProcessBuilder
 */
public class AcceptFrom {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("ACCEPT... FROM... Example Program");
        System.out.println("---------------------------------");
        System.out.println("Pass command line parameters to demo that feature");
        System.out.println();

        // FROM COMMAND-LINE: full command line argument string
        String commandLine = String.join(" ", args);
        System.out.println("accept from command-line: " + commandLine);

        // FROM ARGUMENT-NUMBER: number of command line arguments
        int argCount = args.length;
        System.out.println("accept from argument-number: " + argCount);

        // Iterate through command line arguments
        if (argCount > 0) {
            for (int i = 0; i < argCount; i++) {
                System.out.println("accept from argument-value: " + args[i]);
            }
        }

        // FROM ENVIRONMENT: get environment variable
        System.out.println("Before environment setting set:");
        String envValue = System.getenv("COB_TEST_ENV_KEY");
        System.out.println("accept from environment: " + (envValue != null ? envValue : ""));

        // FROM EXCEPTION STATUS: simulate - the above unset env would cause exception
        System.out.println("accept from exception status: " + (envValue == null ? "1537" : "0000"));

        // SET ENVIRONMENT (Java can't truly set env vars at runtime; simulate with system property)
        System.setProperty("COB_TEST_ENV_KEY", "NOW SET!");

        // Now retrieve the set value
        System.out.println("After environment setting set:");
        System.out.println("accept from environment: " + System.getProperty("COB_TEST_ENV_KEY", ""));

        // FROM DATE: YYMMDD format
        LocalDate today = LocalDate.now();
        System.out.println("accept from date: " + today.format(DateTimeFormatter.ofPattern("yyMMdd")));

        // FROM DATE YYYYMMDD
        System.out.println("accept from date yyyymmdd: " + today.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        // FROM DAY: YYDDD format (day of year)
        System.out.println("accept from day: " + today.format(DateTimeFormatter.ofPattern("yy"))
                + String.format("%03d", today.getDayOfYear()));

        // FROM DAY YYYYDDD
        System.out.println("accept from day yyyyddd: " + today.format(DateTimeFormatter.ofPattern("yyyy"))
                + String.format("%03d", today.getDayOfYear()));

        // FROM TIME: hhmmssnn format
        LocalTime now = LocalTime.now();
        System.out.println("accept from time: " + now.format(DateTimeFormatter.ofPattern("HHmmss"))
                + String.format("%02d", now.getNano() / 10_000_000));

        // FROM DAY-OF-WEEK: 1=Monday, 7=Sunday (matches COBOL convention)
        System.out.println("accept from day-of-week: " + today.getDayOfWeek().getValue());

        // FROM USER NAME
        System.out.println("accept from user name: " + System.getProperty("user.name"));

        // FROM CONSOLE: read from console
        System.out.print("Enter value: ");
        String consoleInput = scanner.nextLine();
        System.out.println("accept from console: " + consoleInput);

        // Screen-mode features: LINES and COLUMNS
        System.out.println("Press enter to enter screen mode.");
        scanner.nextLine();

        // FROM LINES: get terminal lines
        int lines = getTerminalDimension("lines");
        System.out.println("accept from lines: " + lines);

        // FROM COLUMNS: get terminal columns
        int cols = getTerminalDimension("cols");
        System.out.println("accept from columns: " + cols);

        // Using CBL_GET_SCR_SIZE equivalent
        System.out.println("Using CBL_GET_SCR_SIZE instead: ");
        System.out.println("Num lines: " + lines);
        System.out.println("Num cols: " + cols);
    }

    /**
     * Get terminal dimension using tput command (Linux/macOS).
     * Returns -1 if unable to determine.
     */
    private static int getTerminalDimension(String dimension) {
        try {
            ProcessBuilder pb = new ProcessBuilder("tput", dimension);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            String output = new String(process.getInputStream().readAllBytes()).trim();
            process.waitFor();
            return Integer.parseInt(output);
        } catch (Exception e) {
            return -1;
        }
    }
}
