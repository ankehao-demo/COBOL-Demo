package com.coboldemo.accept;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Java port of accept/accept_from.cbl.
 *
 * Demonstrates the various ACCEPT ... FROM ... sources. A few COBOL features
 * have no direct JVM analog (notably SET ENVIRONMENT, which mutates the
 * current process environment) and are approximated with the closest
 * equivalent plus a comment.
 */
public final class AcceptFromExample {

    private static final String ENV_KEY = "COB_TEST_ENV_KEY";

    private AcceptFromExample() {
    }

    public static void main(String[] args) {
        System.out.println();
        System.out.println("ACCEPT... FROM... Example Program");
        System.out.println("---------------------------------");
        System.out.println("Pass command line parameters to demo that feature");
        System.out.println();

        // FROM COMMAND-LINE -> the full argument string.
        String commandLine = String.join(" ", args);
        System.out.println("accept from command-line: " + commandLine);

        // FROM ARGUMENT-NUMBER -> the argument count.
        System.out.println("accept from argument-number: " + args.length);

        // Iterate arguments (FROM ARGUMENT-VALUE).
        for (int i = 0; i < args.length; i++) {
            System.out.println("accept from argument-value: " + args[i]);
        }

        // FROM ENVIRONMENT before setting it.
        System.out.println("Before environment setting set:");
        String envValue = System.getenv(ENV_KEY);
        System.out.println("accept from environment: "
                + (envValue == null ? "" : envValue));

        // FROM EXCEPTION STATUS is not directly representable; we emit 0 when
        // the previous lookup succeeded or a sentinel matching EC-IMP-ACCEPT.
        System.out.println("accept from exception status: "
                + (envValue == null ? "1537" : "0"));

        // SET ENVIRONMENT is disallowed in Java runtime. Use System properties
        // as the closest analogue.
        System.setProperty(ENV_KEY, "NOW SET!");

        System.out.println("After environment setting set:");
        String propertyValue = System.getProperty(ENV_KEY);
        System.out.println("accept from environment (system property): "
                + propertyValue);

        // FROM DATE (yyMMdd)
        LocalDate today = LocalDate.now();
        System.out.println("accept from date: "
                + today.format(DateTimeFormatter.ofPattern("yyMMdd")));

        // FROM DATE YYYYMMDD
        System.out.println("accept from date yyyymmdd: "
                + today.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        // FROM DAY (yyDDD)
        System.out.println("accept from day: "
                + today.format(DateTimeFormatter.ofPattern("yyDDD")));

        // FROM DAY YYYYDDD
        System.out.println("accept from day yyyyddd: "
                + today.format(DateTimeFormatter.ofPattern("yyyyDDD")));

        // FROM TIME (HHmmssSS)
        LocalTime now = LocalTime.now();
        String hundredths = String.format("%02d",
                (now.getNano() / 10_000_000));
        System.out.println("accept from time: "
                + now.format(DateTimeFormatter.ofPattern("HHmmss"))
                + hundredths);

        // FROM DAY-OF-WEEK: 1=Monday ... 7=Sunday, same as COBOL.
        System.out.println("accept from day-of-week: "
                + today.getDayOfWeek().getValue());

        // FROM USER NAME
        System.out.println("accept from user name: "
                + System.getProperty("user.name"));

        // FROM CONSOLE
        System.out.print("Enter value: ");
        String line = readLine();
        System.out.println("accept from console: " + line);

        // Screen-mode continuation in the original. Here we emit sequentially.
        System.out.println("Press enter to enter screen mode.");
        readLine();

        int[] size = readScreenSize();
        System.out.println("accept from lines: " + size[0]);
        System.out.println("accept from columns: " + size[1]);

        // CBL_GET_SCR_SIZE equivalent.
        System.out.println("Using CBL_GET_SCR_SIZE instead:");
        System.out.println("Num lines: " + size[0]);
        System.out.println("Num cols: " + size[1]);
    }

    /**
     * Attempts to read current terminal size using {@code tput}. Falls back to
     * a conventional 24x80 if tput is unavailable (e.g. on Windows or when
     * stdin is not a TTY).
     */
    private static int[] readScreenSize() {
        int lines = runTput("lines", 24);
        int cols = runTput("cols", 80);
        return new int[]{lines, cols};
    }

    private static int runTput(String param, int fallback) {
        try {
            Process p = new ProcessBuilder("tput", param)
                    .redirectErrorStream(true)
                    .start();
            try (BufferedReader r = new BufferedReader(
                    new InputStreamReader(p.getInputStream()))) {
                String value = r.readLine();
                p.waitFor();
                return value == null || value.isBlank()
                        ? fallback
                        : Integer.parseInt(value.trim());
            }
        } catch (Exception ignored) {
            return fallback;
        }
    }

    private static String readLine() {
        Scanner scanner = new Scanner(System.in);
        return scanner.hasNextLine() ? scanner.nextLine() : "";
    }
}
