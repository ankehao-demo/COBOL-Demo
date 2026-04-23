package com.coboldemo.accept;

import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Java port of accept/accept.cbl.
 *
 * Demonstrates the various forms of the COBOL ACCEPT verb. The original used
 * screen-mode positioning (e.g. AT 0101). In Java we rely on sequential
 * console output; ANSI escape sequences could be emitted instead but they are
 * distracting on most modern terminals.
 */
public final class AcceptExample {

    private AcceptExample() {
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Basic ACCEPT ws-input.
        System.out.print("Simple accept. Enter a value: ");
        String input = scanner.nextLine();
        System.out.println("You entered: " + input);

        // ACCEPT OMITTED - wait for user to press any key (here: Enter).
        System.out.println("Press enter to enter screen mode.");
        scanner.nextLine();

        // ACCEPT ... TIMEOUT 3 -> read with a 3-second deadline.
        System.out.print("Enter value or wait 3 seconds: ");
        input = acceptWithTimeout(scanner, 3);
        System.out.println("You entered: " + input);

        // ACCEPT ... AUTO-SKIP -> read up to 16 chars.
        System.out.print("Enter 16 chars to auto skip: ");
        input = scanner.nextLine();
        if (input.length() > 16) {
            input = input.substring(0, 16);
        }
        System.out.println("You entered: " + input);

        // ACCEPT ... NO-ECHO -> suppress echoing.
        System.out.print("Enter a value (no echo): ");
        input = readNoEcho(scanner);
        System.out.println("You entered: " + input);

        // ACCEPT ... UPPER -> uppercase the entered value.
        System.out.print("Enter a value: ");
        input = scanner.nextLine().toUpperCase();
        System.out.println("You entered: " + input);
    }

    /**
     * Mirrors ACCEPT ws-input TIMEOUT n. Falls back to an empty string when
     * the deadline elapses without user input.
     */
    private static String acceptWithTimeout(Scanner scanner, int seconds) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> task = scanner::nextLine;
        Future<String> future = executor.submit(task);
        try {
            return future.get(seconds, TimeUnit.SECONDS);
        } catch (TimeoutException te) {
            future.cancel(true);
            System.out.println();
            return "";
        } catch (Exception ex) {
            return "";
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * NO-ECHO equivalent. Uses java.io.Console when available; otherwise falls
     * back to plain Scanner input (e.g. when running inside an IDE).
     */
    private static String readNoEcho(Scanner scanner) {
        if (System.console() != null) {
            char[] chars = System.console().readPassword();
            return chars == null ? "" : new String(chars);
        }
        return scanner.nextLine();
    }
}
