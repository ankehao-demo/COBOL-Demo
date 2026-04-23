package com.coboldemo.io;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Scanner;

/**
 * Migrated from: accept/accept_from.cbl
 *
 * Demonstrates various forms of ACCEPT...FROM... including command-line
 * arguments, environment variables, date/time values, user name, console
 * input, and terminal size (via Lanterna or fallback).
 */
public class AcceptFromExample {

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
        int argCount = args.length;
        System.out.println("accept from argument-number: " + argCount);

        // Iterate through command line arguments
        if (argCount > 0) {
            for (int i = 0; i < argCount; i++) {
                System.out.println("accept from argument-value: " + args[i]);
            }
        }

        // FROM ENVIRONMENT: read environment variable
        System.out.println("Before environment setting set:");
        String envValue = System.getenv("COB_TEST_ENV_KEY");
        System.out.println("accept from environment: " + (envValue != null ? envValue : ""));

        // Exception status: report if the env var was missing
        if (envValue == null) {
            System.out.println("accept from exception status: EC-IMP-ACCEPT (env var not set)");
        }

        // SET ENVIRONMENT: Java cannot modify its own process environment at runtime,
        // so we simulate by using System.setProperty
        System.setProperty("COB_TEST_ENV_KEY", "NOW SET!");
        System.out.println("After environment setting set:");
        System.out.println("accept from environment: " + System.getProperty("COB_TEST_ENV_KEY"));

        // FROM DATE: current date in YYMMDD format
        LocalDate today = LocalDate.now();
        String dateYYMMDD = DateTimeFormatter.ofPattern("yyMMdd").format(today);
        System.out.println("accept from date: " + dateYYMMDD);

        // FROM DATE YYYYMMDD
        String dateYYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd").format(today);
        System.out.println("accept from date yyyymmdd: " + dateYYYYMMDD);

        // FROM DAY: YYDDD format (day of year)
        String dayYYDDD = DateTimeFormatter.ofPattern("yy").format(today)
                + String.format("%03d", today.getDayOfYear());
        System.out.println("accept from day: " + dayYYDDD);

        // FROM DAY YYYYDDD
        String dayYYYYDDD = DateTimeFormatter.ofPattern("yyyy").format(today)
                + String.format("%03d", today.getDayOfYear());
        System.out.println("accept from day yyyyddd: " + dayYYYYDDD);

        // FROM TIME: hhmmssnn format
        LocalTime now = LocalTime.now();
        String time = String.format("%02d%02d%02d%02d",
                now.getHour(), now.getMinute(), now.getSecond(),
                now.get(ChronoField.MILLI_OF_SECOND) / 10);
        System.out.println("accept from time: " + time);

        // FROM DAY-OF-WEEK: 1=Monday .. 7=Sunday
        int dayOfWeek = today.getDayOfWeek().getValue();
        System.out.println("accept from day-of-week: " + dayOfWeek);

        // FROM USER NAME
        String userName = System.getProperty("user.name");
        System.out.println("accept from user name: " + userName);

        // FROM CONSOLE: read user input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter value: ");
        String consoleInput = scanner.nextLine();
        System.out.println("accept from console: " + consoleInput);

        // Terminal size: try to detect using Lanterna
        System.out.println();
        System.out.println("Terminal size (attempting detection):");
        try {
            com.googlecode.lanterna.terminal.DefaultTerminalFactory factory =
                    new com.googlecode.lanterna.terminal.DefaultTerminalFactory();
            com.googlecode.lanterna.terminal.Terminal terminal = factory.createTerminal();
            com.googlecode.lanterna.TerminalSize size = terminal.getTerminalSize();
            System.out.println("accept from lines: " + size.getRows());
            System.out.println("accept from columns: " + size.getColumns());
            terminal.close();
        } catch (Exception e) {
            System.out.println("accept from lines: (unavailable - no terminal)");
            System.out.println("accept from columns: (unavailable - no terminal)");
        }

        // CBL_GET_SCR_SIZE: same information, already shown above
        System.out.println("Using CBL_GET_SCR_SIZE equivalent (same as above).");

        scanner.close();
    }
}
