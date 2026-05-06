package com.example.cobol.accept;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Java port of {@code accept/accept_from.cbl}.
 *
 * <p>Demonstrates the various {@code ACCEPT … FROM …} sources: command line,
 * argument values, environment variables, dates / times / day-of-week,
 * console, and screen-size queries.
 *
 * <p>Java's stdlib has direct equivalents for almost everything the COBOL
 * version pulls out of the runtime, so the port is largely a straight
 * one-to-one mapping.
 */
public final class AcceptFromExample {

    private static final String ENV_KEY = "COB_TEST_ENV_KEY";

    private AcceptFromExample() {}

    public static void main(String[] args) {
        System.out.println();
        System.out.println("ACCEPT... FROM... Example Program");
        System.out.println("---------------------------------");
        System.out.println("Pass command line parameters to demo that feature");
        System.out.println();

        // FROM COMMAND-LINE — the full joined command-line string.
        String fullCmdLine = String.join(" ", args);
        System.out.println("accept from command-line: " + fullCmdLine);

        // FROM ARGUMENT-NUMBER — the count of args.
        System.out.println("accept from argument-number: " + args.length);

        // Iterate through individual command-line arguments — equivalent of
        // setting "DISPLAY idx UPON ARGUMENT-NUMBER" + "ACCEPT FROM ARGUMENT-VALUE".
        for (int i = 0; i < args.length; i++) {
            System.out.println("accept from argument-value: " + args[i]);
        }

        // FROM ENVIRONMENT — JVM system properties / process env vars.
        System.out.println("Before environment setting set:");
        String beforeEnv = System.getProperty(ENV_KEY, System.getenv().getOrDefault(ENV_KEY, ""));
        System.out.println("accept from environment: " + beforeEnv);

        // SET ENVIRONMENT — Java cannot mutate the process env, but we can
        // set a system property which is the analogous in-process key/value
        // store.
        System.setProperty(ENV_KEY, "NOW SET!");
        System.out.println("After environment setting set:");
        System.out.println("accept from environment: " + System.getProperty(ENV_KEY));

        // FROM DATE / DATE YYYYMMDD / DAY / DAY YYYYDDD / TIME / DAY-OF-WEEK
        LocalDateTime now = LocalDateTime.now();
        System.out.println("accept from date: "
                + now.format(DateTimeFormatter.ofPattern("yyMMdd")));
        System.out.println("accept from date yyyymmdd: "
                + now.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        LocalDate today = now.toLocalDate();
        int dayOfYear = today.getDayOfYear();
        System.out.printf("accept from day: %02d%03d%n",
                today.getYear() % 100, dayOfYear);
        System.out.printf("accept from day yyyyddd: %04d%03d%n",
                today.getYear(), dayOfYear);

        LocalTime nowTime = now.toLocalTime();
        System.out.printf("accept from time: %02d%02d%02d%02d%n",
                nowTime.getHour(), nowTime.getMinute(), nowTime.getSecond(),
                (nowTime.getNano() / 10_000_000) % 100);

        // COBOL day-of-week is 1=Monday..7=Sunday; java.time matches.
        System.out.println("accept from day-of-week: " + now.getDayOfWeek().getValue());

        // FROM USER NAME
        System.out.println("accept from user name: "
                + System.getProperty("user.name", ""));

        // FROM CONSOLE — read from stdin.
        System.out.print("Enter value: ");
        try (Scanner in = new Scanner(System.in)) {
            String line = in.hasNextLine() ? in.nextLine() : "";
            System.out.println("accept from console: " + line);
        }

        // FROM LINES / COLUMNS — Java has no portable terminal-size API in
        // the stdlib. We fall back to the COLUMNS / LINES env vars when set,
        // otherwise default to 80x24.
        int rows = parseIntOrDefault(System.getenv("LINES"), 24);
        int cols = parseIntOrDefault(System.getenv("COLUMNS"), 80);
        System.out.println("accept from lines: " + rows);
        System.out.println("accept from columns: " + cols);
        System.out.println("Using CBL_GET_SCR_SIZE instead:");
        System.out.println("Num lines: " + rows);
        System.out.println("Num cols: " + cols);
    }

    private static int parseIntOrDefault(String value, int fallback) {
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
