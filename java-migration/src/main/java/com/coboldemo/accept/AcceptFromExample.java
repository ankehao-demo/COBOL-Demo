package com.coboldemo.accept;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Java port of {@code accept/accept_from.cbl}.
 *
 * Demonstrates the COBOL ACCEPT ... FROM ... clause for command line
 * arguments, environment variables, the current date / time / day of week,
 * the current user, console input, and screen size.
 */
public class AcceptFromExample {

    private static final String ENV_KEY = "COB_TEST_ENV_KEY";

    public static void main(String[] args) {
        System.out.println();
        System.out.println("ACCEPT... FROM... Example Program");
        System.out.println("---------------------------------");
        System.out.println("Pass command line parameters to demo that feature");
        System.out.println();

        // FROM COMMAND-LINE returns the command line argument string in full.
        String fullCommandLine = String.join(" ", args);
        System.out.println("accept from command-line: " + fullCommandLine);

        // FROM ARGUMENT-NUMBER returns the count of arguments.
        System.out.println("accept from argument-number: " + args.length);

        // Iterate through any command line arguments and display them.
        for (int i = 0; i < args.length; i++) {
            System.out.println("accept from argument-value: " + args[i]);
        }

        // FROM ENVIRONMENT before set.
        System.out.println("Before environment setting set:");
        String envBefore = System.getenv(ENV_KEY);
        System.out.println("accept from environment: " + (envBefore == null ? "" : envBefore));

        // SET ENVIRONMENT has no real Java equivalent for the OS environment.
        // We use System.setProperty so later code can still observe a value.
        System.setProperty(ENV_KEY, "NOW SET!");
        System.out.println("After environment setting set:");
        String envAfter = System.getenv(ENV_KEY);
        if (envAfter == null) {
            envAfter = System.getProperty(ENV_KEY, "");
        }
        System.out.println("accept from environment: " + envAfter);

        // FROM DATE / DATE YYYYMMDD / DAY / DAY YYYYDDD
        LocalDate today = LocalDate.now();
        System.out.println("accept from date: " + today.format(DateTimeFormatter.ofPattern("yyMMdd")));
        System.out.println("accept from date yyyymmdd: " + today.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        int dayOfYear = today.getDayOfYear();
        System.out.println(String.format("accept from day: %02d%03d",
                today.getYear() % 100, dayOfYear));
        System.out.println(String.format("accept from day yyyyddd: %04d%03d",
                today.getYear(), dayOfYear));

        // FROM TIME -> hhmmssnn (nn = hundredths of a second).
        LocalTime now = LocalTime.now();
        int hundredths = now.getNano() / 10_000_000;
        System.out.println(String.format("accept from time: %02d%02d%02d%02d",
                now.getHour(), now.getMinute(), now.getSecond(), hundredths));

        // FROM DAY-OF-WEEK: 1=Monday, 7=Sunday (matches COBOL).
        System.out.println("accept from day-of-week: " + today.getDayOfWeek().getValue());

        // FROM USER NAME
        System.out.println("accept from user name: " + System.getProperty("user.name", ""));

        // FROM CONSOLE
        System.out.print("Enter value: ");
        String consoleValue = readLine();
        System.out.println("accept from console: " + consoleValue);

        // FROM LINES / FROM COLUMNS / CBL_GET_SCR_SIZE
        int[] size = detectScreenSize();
        System.out.println("accept from lines: " + size[0]);
        System.out.println("accept from columns: " + size[1]);
        System.out.println("Using CBL_GET_SCR_SIZE instead:");
        System.out.println(String.format("Num lines: %d", size[0]));
        System.out.println(String.format("Num cols: %d", size[1]));
    }

    private static String readLine() {
        Scanner s = new Scanner(System.in);
        return s.hasNextLine() ? s.nextLine() : "";
    }

    private static int[] detectScreenSize() {
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

    private static int runTput(String arg) {
        try {
            Process p = new ProcessBuilder("tput", arg).redirectErrorStream(true).start();
            try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line = r.readLine();
                p.waitFor();
                if (line != null) {
                    return Integer.parseInt(line.trim());
                }
            }
        } catch (Exception e) {
            // ignore - fallback below
        }
        return 0;
    }
}
