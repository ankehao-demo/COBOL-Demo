package com.coboldemo.accept;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Migrated from accept/accept_from.cbl
 * Demonstrates various ACCEPT...FROM... forms.
 */
public class AcceptFrom {

    public static void main(String[] args) {
        System.out.println();
        System.out.println("ACCEPT... FROM... Example Program");
        System.out.println("---------------------------------");
        System.out.println("Pass command line parameters to demo that feature");
        System.out.println();

        // FROM COMMAND-LINE: full command line argument string
        String commandLine = String.join(" ", args);
        System.out.println("accept from command-line: " + commandLine);

        // FROM ARGUMENT-NUMBER: number of command line arguments
        System.out.println("accept from argument-number: " + args.length);

        // Iterate through command line arguments
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                System.out.println("accept from argument-value: " + args[i]);
            }
        }

        // FROM ENVIRONMENT: get environment variable
        System.out.println("Before environment setting set:");
        String envVal = System.getenv("COB_TEST_ENV_KEY");
        System.out.println("accept from environment: " + (envVal != null ? envVal : ""));

        // FROM EXCEPTION STATUS: not directly applicable in Java
        System.out.println("accept from exception status: " + (envVal == null ? "1537" : "0000"));

        // Note: Java cannot set environment variables at runtime like COBOL's SET ENVIRONMENT.
        // In COBOL, this sets COB_TEST_ENV_KEY to "NOW SET!" then reads it back.
        System.out.println("After environment setting set:");
        System.out.println("accept from environment: NOW SET! (simulated - Java cannot set env vars at runtime)");

        // FROM DATE: YYMMDD format
        LocalDate today = LocalDate.now();
        System.out.println("accept from date: " + today.format(DateTimeFormatter.ofPattern("yyMMdd")));

        // FROM DATE YYYYMMDD
        System.out.println("accept from date yyyymmdd: " + today.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        // FROM DAY: YYDDD format (day of year)
        System.out.println("accept from day: " + today.format(DateTimeFormatter.ofPattern("yyDDD")));

        // FROM DAY YYYYDDD
        System.out.println("accept from day yyyyddd: " + today.format(DateTimeFormatter.ofPattern("yyyyDDD")));

        // FROM DAY-OF-WEEK: 1=Monday through 7=Sunday
        System.out.println("accept from day-of-week: " + today.getDayOfWeek().getValue());

        // FROM TIME: HHMMSSss format
        LocalTime now = LocalTime.now();
        System.out.println("accept from time: " + now.format(DateTimeFormatter.ofPattern("HHmmssSSS")));

        // FROM USER NAME
        System.out.println("accept from user name: " + System.getProperty("user.name"));

        // FROM LINES and COLUMNS: terminal size detection
        int lines = getTerminalSize("lines");
        int cols = getTerminalSize("cols");
        System.out.println("accept from lines: " + (lines > 0 ? lines : "unknown"));
        System.out.println("accept from columns: " + (cols > 0 ? cols : "unknown"));
    }

    private static int getTerminalSize(String dimension) {
        try {
            ProcessBuilder pb = new ProcessBuilder("tput", dimension);
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
}
