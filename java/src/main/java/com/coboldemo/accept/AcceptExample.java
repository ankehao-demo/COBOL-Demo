package com.coboldemo.accept;

import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Java equivalent of accept/accept.cbl
 *
 * Demonstrates various forms of the ACCEPT verb in COBOL, mapped to Java
 * console I/O equivalents. Screen-mode positioning (AT LLCC) is replaced
 * with plain console output; use Lanterna for positioned terminal output.
 */
public class AcceptExample {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Basic accept: ACCEPT ws-input
        System.out.print("Simple accept. Enter a value: ");
        String input = scanner.nextLine();
        System.out.println("You entered: " + input);

        // ACCEPT OMITTED: wait for user input without storing it
        System.out.println("Press any key to continue.");
        scanner.nextLine();

        // ACCEPT ... TIMEOUT 3: wait up to 3 seconds for input
        System.out.println("Enter value or wait 3 seconds: ");
        input = readWithTimeout(3);
        System.out.println("You entered: " + input);

        // ACCEPT ... AUTO-SKIP: read up to N characters (ws-input is PIC X(16))
        System.out.print("Enter 16 chars to auto skip: ");
        input = readUpToNChars(scanner, 16);
        System.out.println("You entered: " + input);

        // ACCEPT ... NO-ECHO: read input without echoing characters
        System.out.print("Enter a value (no echo): ");
        if (System.console() != null) {
            char[] noEcho = System.console().readPassword();
            input = new String(noEcho);
        } else {
            input = scanner.nextLine();
            System.out.println("(no-echo not supported in this terminal)");
        }
        System.out.println("You entered: " + input);

        // ACCEPT ... UPPER: convert input to uppercase
        System.out.print("Enter a value: ");
        input = scanner.nextLine().toUpperCase();
        System.out.println("You entered: " + input);

        scanner.close();
    }

    /**
     * Reads a line from System.in with a timeout in seconds.
     * Equivalent to COBOL: ACCEPT ws-input TIMEOUT 3
     */
    private static String readWithTimeout(int timeoutSeconds) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> readTask = () -> {
            Scanner s = new Scanner(System.in);
            return s.nextLine();
        };
        Future<String> future = executor.submit(readTask);
        try {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            return "";
        } catch (Exception e) {
            return "";
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * Reads up to maxChars characters from the scanner.
     * Equivalent to COBOL: ACCEPT ws-input AUTO-SKIP (auto-submits when PIC length reached)
     */
    private static String readUpToNChars(Scanner scanner, int maxChars) {
        String line = scanner.nextLine();
        if (line.length() > maxChars) {
            return line.substring(0, maxChars);
        }
        return line;
    }
}
