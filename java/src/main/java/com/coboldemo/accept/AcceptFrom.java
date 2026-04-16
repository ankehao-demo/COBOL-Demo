package com.coboldemo.accept;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Migrated from: accept/accept_from.cbl
 * Original author: Erik Eriksen (2022-04-18, updated 2022-05-02)
 * Purpose: Examples of using various forms of ACCEPT...FROM...
 *
 * COBOL-to-Java mapping:
 *   ACCEPT FROM COMMAND-LINE      -> String.join(" ", args)
 *   ACCEPT FROM ARGUMENT-NUMBER   -> args.length
 *   ACCEPT FROM ARGUMENT-VALUE    -> args[index]
 *   ACCEPT FROM ENVIRONMENT       -> System.getenv()
 *   ACCEPT FROM EXCEPTION STATUS  -> (no direct Java equivalent, noted)
 *   SET ENVIRONMENT ... TO ...    -> (Java cannot set env vars at runtime, noted)
 *   ACCEPT FROM DATE              -> LocalDate.now() formatted as YYMMDD
 *   ACCEPT FROM DATE YYYYMMDD     -> LocalDate.now() formatted as YYYYMMDD
 *   ACCEPT FROM DAY               -> LocalDate.now() formatted as YYDDD
 *   ACCEPT FROM DAY YYYYDDD       -> LocalDate.now() formatted as YYYYDDD
 *   ACCEPT FROM TIME              -> LocalTime.now() formatted as HHmmssnn
 *   ACCEPT FROM DAY-OF-WEEK       -> LocalDate.now().getDayOfWeek() (1=Mon..7=Sun)
 *   ACCEPT FROM USER NAME         -> System.getProperty("user.name")
 *   ACCEPT FROM CONSOLE           -> Scanner.nextLine()
 *   ACCEPT FROM LINES             -> terminal-specific (tput lines)
 *   ACCEPT FROM COLUMNS           -> terminal-specific (tput cols)
 *   CBL_GET_SCR_SIZE              -> terminal-specific (tput lines/cols)
 */
public class AcceptFrom {

    // COBOL: 01 ws-input pic x(50).
    private static final int WS_INPUT_LENGTH = 50;

    /**
     * Moves the cursor to the specified row and column using ANSI escape codes.
     */
    private static void moveCursor(int row, int col) {
        System.out.printf("\033[%d;%dH", row, col);
    }

    /**
     * Pads or truncates a string to exactly the given length, matching
     * COBOL PIC X(n) behavior.
     */
    private static String picX(String value, int length) {
        if (value == null) {
            value = "";
        }
        if (value.length() > length) {
            return value.substring(0, length);
        }
        return String.format("%-" + length + "s", value);
    }

    /**
     * Attempts to get a terminal dimension using the 'tput' command.
     * Returns the value as a string, or "N/A" if unavailable.
     *
     * @param tputArg either "lines" or "cols"
     * @return the terminal dimension as a string
     */
    private static String getTerminalDimension(String tputArg) {
        try {
            ProcessBuilder pb = new ProcessBuilder("tput", tputArg);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                process.waitFor();
                return line != null ? line.trim() : "N/A";
            }
        } catch (Exception e) {
            return "N/A (terminal query not supported)";
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        System.out.println();
        System.out.println("ACCEPT... FROM... Example Program");
        System.out.println("---------------------------------");
        System.out.println("Pass command line parameters to demo that feature");
        System.out.println();

        // ---------------------------------------------------------------
        // COBOL: accept ws-input from command-line
        // Returns the full command line argument string.
        // Java: String.join(" ", args) concatenates all args with spaces.
        // ---------------------------------------------------------------
        String commandLine = String.join(" ", args);
        System.out.println("accept from command-line: " + picX(commandLine, WS_INPUT_LENGTH));

        // ---------------------------------------------------------------
        // COBOL: accept ws-input from argument-number
        // Returns the number of command line arguments.
        // Java: args.length
        // ---------------------------------------------------------------
        int numArgs = args.length;
        System.out.println("accept from argument-number: " + picX(String.valueOf(numArgs), WS_INPUT_LENGTH));

        // ---------------------------------------------------------------
        // COBOL: Iterate through arguments one by one using
        //        DISPLAY ws-idx UPON ARGUMENT-NUMBER / ACCEPT FROM ARGUMENT-VALUE
        // Java: Simple args[] array indexing.
        // ---------------------------------------------------------------
        if (numArgs > 0) {
            for (int i = 0; i < numArgs; i++) {
                System.out.println("accept from argument-value: " + picX(args[i], WS_INPUT_LENGTH));
            }
        }

        // ---------------------------------------------------------------
        // COBOL: accept ws-input from environment "COB_TEST_ENV_KEY"
        // Java: System.getenv() returns the value or null.
        // ---------------------------------------------------------------
        System.out.println("Before environment setting set:");
        String envValue = System.getenv("COB_TEST_ENV_KEY");
        System.out.println("accept from environment: " + picX(envValue != null ? envValue : "", WS_INPUT_LENGTH));

        // ---------------------------------------------------------------
        // COBOL: accept ws-input from exception status
        // No direct Java equivalent. Exception status is COBOL-specific.
        // In COBOL, this returned 1537 (0x0601 = EC-IMP-ACCEPT) after
        // reading a nonexistent env var.
        // ---------------------------------------------------------------
        if (envValue == null) {
            System.out.println("accept from exception status: " +
                picX("N/A (COBOL-specific; env var was not set)", WS_INPUT_LENGTH));
        }

        // ---------------------------------------------------------------
        // COBOL: set environment "COB_TEST_ENV_KEY" to "NOW SET!"
        // Java cannot modify environment variables at runtime.
        // We simulate by using a local variable.
        // ---------------------------------------------------------------
        String simulatedEnvValue = "NOW SET!";
        System.out.println("After environment setting set:");
        // Note: In COBOL, SET ENVIRONMENT modifies the process environment.
        // Java's System.getenv() is read-only. We display the simulated value.
        System.out.println("accept from environment: " + picX(simulatedEnvValue, WS_INPUT_LENGTH));

        // ---------------------------------------------------------------
        // COBOL: accept ws-input from date
        // Returns current date in YYMMDD format.
        // ---------------------------------------------------------------
        String dateYYMMDD = today.format(DateTimeFormatter.ofPattern("yyMMdd"));
        System.out.println("accept from date: " + picX(dateYYMMDD, WS_INPUT_LENGTH));

        // ---------------------------------------------------------------
        // COBOL: accept ws-input from date yyyymmdd
        // Returns current date in YYYYMMDD format.
        // ---------------------------------------------------------------
        String dateYYYYMMDD = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        System.out.println("accept from date yyyymmdd: " + picX(dateYYYYMMDD, WS_INPUT_LENGTH));

        // ---------------------------------------------------------------
        // COBOL: accept ws-input from day
        // Returns current date in YYDDD format (DDD = day of year).
        // ---------------------------------------------------------------
        String dayYYDDD = today.format(DateTimeFormatter.ofPattern("yy"))
                + String.format("%03d", today.getDayOfYear());
        System.out.println("accept from day: " + picX(dayYYDDD, WS_INPUT_LENGTH));

        // ---------------------------------------------------------------
        // COBOL: accept ws-input from day yyyyddd
        // Returns current date in YYYYDDD format.
        // ---------------------------------------------------------------
        String dayYYYYDDD = today.format(DateTimeFormatter.ofPattern("yyyy"))
                + String.format("%03d", today.getDayOfYear());
        System.out.println("accept from day yyyyddd: " + picX(dayYYYYDDD, WS_INPUT_LENGTH));

        // ---------------------------------------------------------------
        // COBOL: accept ws-input from time
        // Returns current time in hhmmssnn format (nn = hundredths of sec).
        // Java: LocalTime formatted as HHmmss + hundredths derived from nanos.
        // ---------------------------------------------------------------
        String timeHHMMSSNN = now.format(DateTimeFormatter.ofPattern("HHmmss"))
                + String.format("%02d", now.getNano() / 10_000_000);
        System.out.println("accept from time: " + picX(timeHHMMSSNN, WS_INPUT_LENGTH));

        // ---------------------------------------------------------------
        // COBOL: accept ws-input from day-of-week
        // Returns 1-7 where Monday=1, Sunday=7.
        // Java: DayOfWeek.getValue() returns 1 (Monday) to 7 (Sunday), same mapping.
        // ---------------------------------------------------------------
        int dayOfWeek = today.getDayOfWeek().getValue();
        System.out.println("accept from day-of-week: " + picX(String.valueOf(dayOfWeek), WS_INPUT_LENGTH));

        // ---------------------------------------------------------------
        // COBOL: accept ws-input from user name
        // Returns the current logged-in user name.
        // Java: System.getProperty("user.name")
        // ---------------------------------------------------------------
        String userName = System.getProperty("user.name");
        System.out.println("accept from user name: " + picX(userName != null ? userName : "", WS_INPUT_LENGTH));

        // ---------------------------------------------------------------
        // COBOL: display "Enter value: " with no advancing
        //        accept ws-input from console
        // Console input - same as basic ACCEPT.
        // ---------------------------------------------------------------
        System.out.print("Enter value: ");
        String consoleInput = picX(scanner.nextLine(), WS_INPUT_LENGTH);
        System.out.println("accept from console: " + consoleInput);

        // ---------------------------------------------------------------
        // COBOL: display "Press enter to enter screen mode."
        //        accept omitted
        // ---------------------------------------------------------------
        System.out.println("Press enter to enter screen mode.");
        scanner.nextLine();

        // --- Screen mode section ---
        // Clear screen
        System.out.print("\033[2J");

        // ---------------------------------------------------------------
        // COBOL: accept ws-input from lines
        // Returns the number of lines of the current screen.
        // Java: Terminal-specific. Uses 'tput lines' on Linux.
        // ---------------------------------------------------------------
        String screenLines = getTerminalDimension("lines");
        moveCursor(2, 1);
        System.out.print("accept from lines: ");
        moveCursor(2, 20);
        System.out.print(picX(screenLines, WS_INPUT_LENGTH));

        // ---------------------------------------------------------------
        // COBOL: accept ws-input from columns
        // Returns the number of columns of the current screen.
        // Java: Terminal-specific. Uses 'tput cols' on Linux.
        // ---------------------------------------------------------------
        String screenCols = getTerminalDimension("cols");
        moveCursor(3, 1);
        System.out.print("accept from columns: ");
        moveCursor(3, 22);
        System.out.print(picX(screenCols, WS_INPUT_LENGTH));

        // ---------------------------------------------------------------
        // COBOL: call "CBL_GET_SCR_SIZE" using ws-num-lines ws-num-cols
        // Same as above but gets both values in one call.
        // Java: We use the same tput approach since there is no
        // CBL_GET_SCR_SIZE equivalent in Java.
        // ---------------------------------------------------------------
        moveCursor(4, 1);
        System.out.print("Using CBL_GET_SCR_SIZE instead: ");

        String cblLines = getTerminalDimension("lines");
        String cblCols = getTerminalDimension("cols");

        moveCursor(5, 1);
        System.out.print("Num lines: ");
        moveCursor(5, 14);
        System.out.print(cblLines);

        moveCursor(6, 1);
        System.out.print("Num cols: ");
        moveCursor(6, 14);
        System.out.println(cblCols);

        scanner.close();
    }
}
