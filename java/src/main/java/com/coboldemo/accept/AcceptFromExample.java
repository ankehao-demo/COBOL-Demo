package com.coboldemo.accept;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Java equivalent of accept/accept_from.cbl
 *
 * Demonstrates various forms of ACCEPT ... FROM ... in COBOL, including
 * retrieving system date, time, day of week, user name, environment variables,
 * command-line arguments, and console input.
 */
public class AcceptFromExample {

    public static void main(String[] args) {
        System.out.println();
        System.out.println("ACCEPT... FROM... Example Program");
        System.out.println("---------------------------------");
        System.out.println("Pass command line parameters to demo that feature");
        System.out.println();

        // ACCEPT ws-input FROM COMMAND-LINE → full command-line argument string
        String commandLine = String.join(" ", args);
        System.out.println("accept from command-line: " + commandLine);

        // ACCEPT ws-input FROM ARGUMENT-NUMBER → number of arguments
        int numArgs = args.length;
        System.out.println("accept from argument-number: " + numArgs);

        // Iterate through each command-line argument
        if (numArgs > 0) {
            for (int i = 0; i < numArgs; i++) {
                // DISPLAY ws-idx UPON ARGUMENT-NUMBER / ACCEPT FROM ARGUMENT-VALUE
                System.out.println("accept from argument-value: " + args[i]);
            }
        }

        // ACCEPT ws-input FROM ENVIRONMENT "COB_TEST_ENV_KEY"
        System.out.println("Before environment setting set:");
        String envVal = System.getenv("COB_TEST_ENV_KEY");
        System.out.println("accept from environment: " + (envVal != null ? envVal : ""));

        // ACCEPT ws-input FROM EXCEPTION STATUS
        // Java does not have a direct equivalent; we note the concept
        System.out.println("accept from exception status: (not applicable in Java)");

        // SET ENVIRONMENT → System.setProperty (process-scoped only)
        System.setProperty("COB_TEST_ENV_KEY", "NOW SET!");

        System.out.println("After environment setting set:");
        String propVal = System.getProperty("COB_TEST_ENV_KEY", "");
        System.out.println("accept from environment: " + propVal);

        // ACCEPT ws-input FROM DATE → YYMMDD
        LocalDate today = LocalDate.now();
        String dateYYMMDD = today.format(DateTimeFormatter.ofPattern("yyMMdd"));
        System.out.println("accept from date: " + dateYYMMDD);

        // ACCEPT ws-input FROM DATE YYYYMMDD
        String dateYYYYMMDD = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        System.out.println("accept from date yyyymmdd: " + dateYYYYMMDD);

        // ACCEPT ws-input FROM DAY → YYDDD (day-of-year)
        String dayYYDDD = today.format(DateTimeFormatter.ofPattern("yy")) +
                String.format("%03d", today.getDayOfYear());
        System.out.println("accept from day: " + dayYYDDD);

        // ACCEPT ws-input FROM DAY YYYYDDD
        String dayYYYYDDD = today.format(DateTimeFormatter.ofPattern("yyyy")) +
                String.format("%03d", today.getDayOfYear());
        System.out.println("accept from day yyyyddd: " + dayYYYYDDD);

        // ACCEPT ws-input FROM TIME → hhmmssnn
        LocalTime now = LocalTime.now();
        String time = String.format("%02d%02d%02d%02d",
                now.getHour(), now.getMinute(), now.getSecond(), now.getNano() / 10_000_000);
        System.out.println("accept from time: " + time);

        // ACCEPT ws-input FROM DAY-OF-WEEK → 1 (Monday) to 7 (Sunday)
        int dayOfWeek = today.getDayOfWeek().getValue();
        System.out.println("accept from day-of-week: " + dayOfWeek);

        // ACCEPT ws-input FROM USER NAME
        String userName = System.getProperty("user.name");
        System.out.println("accept from user name: " + userName);

        // ACCEPT ws-input FROM CONSOLE → standard console input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter value: ");
        String consoleInput = scanner.nextLine();
        System.out.println("accept from console: " + consoleInput);

        // Screen-mode equivalents (ACCEPT FROM LINES / COLUMNS)
        // In a real terminal, you could use Lanterna; here we show fallback values
        String lines = System.getenv("LINES");
        String cols = System.getenv("COLUMNS");
        System.out.println("accept from lines: " + (lines != null ? lines : "(unknown)"));
        System.out.println("accept from columns: " + (cols != null ? cols : "(unknown)"));

        scanner.close();
    }
}
