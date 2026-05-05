package com.coboldemo.accept;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Java port of {@code accept/accept.cbl}.
 *
 * Demonstrates the COBOL ACCEPT verb variants:
 * basic accept, accept with timeout, auto-skip on a fixed-width field,
 * no-echo password-style entry, and uppercase-folded input.
 *
 * Notes:
 * - COBOL screen-mode positioning ("AT yyxx", LINE/COLUMN clauses) does not
 *   have a direct equivalent in plain Java console I/O. A real TUI library
 *   (JLine / Lanterna) would be needed for that. Here we just emit the
 *   prompts sequentially on stdout.
 * - "TIMEOUT" is implemented with a Future on a separate thread.
 * - "NO-ECHO" uses {@link java.io.Console#readPassword()} which behaves like
 *   COBOL SECURE (no characters are shown at all). True ANSI no-echo without
 *   prompting requires platform-specific terminal control.
 */
public class AcceptExample {

    private static final int FIELD_WIDTH = 16;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Basic accept syntax. Entered value is stored in the variable provided.
        System.out.print("Simple accept. Enter a value: ");
        String wsInput = readFixedWidth(scanner);
        System.out.println("You entered: " + wsInput);

        // ACCEPT OMITTED waits for user input but does not store it.
        System.out.println("Press enter to continue.");
        scanner.nextLine();

        // TIMEOUT specifies how long to wait for the user to enter a value.
        System.out.print("Enter value or wait 3 seconds: ");
        wsInput = acceptWithTimeout(3, TimeUnit.SECONDS);
        System.out.println("You entered: " + wsInput);

        // AUTO-SKIP submits the input once the field width is reached.
        // We simulate by reading a line and trimming to FIELD_WIDTH characters.
        System.out.print("Enter up to " + FIELD_WIDTH + " chars to auto skip: ");
        wsInput = readFixedWidth(scanner);
        System.out.println("You entered: " + wsInput);

        // NO-ECHO: do not display characters as the user types.
        System.out.print("Enter a value (no echo): ");
        java.io.Console console = System.console();
        if (console != null) {
            char[] pwd = console.readPassword();
            wsInput = padOrTrim(new String(pwd != null ? pwd : new char[0]));
        } else {
            // Fallback when not running on a real terminal.
            System.out.println("(No system console available; echo cannot be suppressed.)");
            wsInput = readFixedWidth(scanner);
        }
        System.out.println("You entered: " + wsInput);

        // UPPER converts user input to uppercase (useful for y/n prompts).
        System.out.print("Enter a value: ");
        wsInput = readFixedWidth(scanner).toUpperCase();
        System.out.println("You entered: " + wsInput);
    }

    private static String readFixedWidth(Scanner scanner) {
        String line = scanner.hasNextLine() ? scanner.nextLine() : "";
        return padOrTrim(line);
    }

    private static String padOrTrim(String s) {
        if (s.length() > FIELD_WIDTH) {
            return s.substring(0, FIELD_WIDTH);
        }
        return String.format("%-" + FIELD_WIDTH + "s", s);
    }

    private static String acceptWithTimeout(long timeout, TimeUnit unit) {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            Callable<String> task = reader::readLine;
            Future<String> future = exec.submit(task);
            try {
                String value = future.get(timeout, unit);
                return padOrTrim(value == null ? "" : value);
            } catch (TimeoutException e) {
                future.cancel(true);
                System.out.println();
                System.out.println("(timed out)");
                return padOrTrim("");
            } catch (Exception e) {
                return padOrTrim("");
            }
        } finally {
            exec.shutdownNow();
        }
    }
}
