package com.coboldemo.accept;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;

/**
 * Port of {@code accept/accept_from.cbl} — demonstrates the COBOL
 * {@code ACCEPT ... FROM ...} variants. Each COBOL {@code FROM} source has
 * been mapped to its idiomatic Java counterpart.
 */
public final class AcceptFromExample {

    private AcceptFromExample() {
    }

    public static void main(String[] args) {
        System.out.println();
        System.out.println("ACCEPT... FROM... Example Program");
        System.out.println("---------------------------------");
        System.out.println("Pass command line parameters to demo that feature");
        System.out.println();

        // FROM COMMAND-LINE — full command line argument string.
        String commandLine = String.join(" ", args);
        System.out.println("accept from command-line: " + commandLine);

        // FROM ARGUMENT-NUMBER — count of arguments.
        System.out.println("accept from argument-number: " + args.length);

        // Iterate through arguments one-by-one (FROM ARGUMENT-VALUE).
        for (int i = 0; i < args.length; i++) {
            System.out.println("accept from argument-value: " + args[i]);
        }

        // FROM ENVIRONMENT — read environment variable.
        String envKey = "COB_TEST_ENV_KEY";
        System.out.println("Before environment setting set:");
        String envValue = System.getenv(envKey);
        System.out.println("accept from environment: " + (envValue == null ? "" : envValue));

        // FROM EXCEPTION STATUS — COBOL sets a specific code (1537) when
        // an env var is missing. Java has no direct equivalent, so we
        // mirror the COBOL behaviour symbolically.
        System.out.println("accept from exception status: " + (envValue == null ? "1537" : "0"));

        // System.setenv does not exist in Java; we cannot mutate the parent
        // process environment. Show the conceptual follow-up by reading an
        // env var that may have been set by the caller.
        System.out.println("After environment setting set:");
        Map<String, String> env = System.getenv();
        System.out.println("accept from environment: " + env.getOrDefault(envKey, "<unset>"));

        // FROM DATE and FROM DATE YYYYMMDD.
        LocalDate today = LocalDate.now();
        System.out.println("accept from date: " + today.format(DateTimeFormatter.ofPattern("yyMMdd")));
        System.out.println("accept from date yyyymmdd: " + today.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        // FROM DAY and FROM DAY YYYYDDD (ordinal day of year).
        String yy = today.format(DateTimeFormatter.ofPattern("yy"));
        String yyyy = today.format(DateTimeFormatter.ofPattern("yyyy"));
        String ddd = String.format("%03d", today.getDayOfYear());
        System.out.println("accept from day: " + yy + ddd);
        System.out.println("accept from day yyyyddd: " + yyyy + ddd);

        // FROM TIME — COBOL format is hhmmssnn (nn = hundredths of a second).
        LocalTime now = LocalTime.now();
        int hundredths = now.getNano() / 10_000_000;
        System.out.println(String.format("accept from time: %02d%02d%02d%02d",
                now.getHour(), now.getMinute(), now.getSecond(), hundredths));

        // FROM DAY-OF-WEEK — 1 = Monday ... 7 = Sunday.
        DayOfWeek dow = today.getDayOfWeek();
        System.out.println("accept from day-of-week: " + dow.getValue());

        // FROM USER NAME.
        System.out.println("accept from user name: " + System.getProperty("user.name", ""));

        // FROM CONSOLE — read a line from stdin.
        System.out.print("Enter value: ");
        try (Scanner scanner = new Scanner(System.in)) {
            String value = scanner.hasNextLine() ? scanner.nextLine() : "";
            System.out.println("accept from console: " + value);
        }

        // FROM LINES / FROM COLUMNS — COBOL switches into screen mode to
        // report terminal dimensions. Java does not ship with a standard
        // API for this, so we probe the COLUMNS/LINES env vars (set by
        // most shells) and fall back to a note if unavailable.
        String columns = System.getenv("COLUMNS");
        String lines = System.getenv("LINES");
        System.out.println("accept from lines: " + (lines == null ? "<unknown>" : lines));
        System.out.println("accept from columns: " + (columns == null ? "<unknown>" : columns));

        System.out.println("Using CBL_GET_SCR_SIZE instead: (not portable in Java)");
        System.out.println("Num lines: " + (lines == null ? "<unknown>" : lines));
        System.out.println("Num cols: " + (columns == null ? "<unknown>" : columns));
    }
}
