package com.coboldemo.accept;

import java.io.Console;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Migrated from: accept/accept.cbl
 * Original author: Erik Eriksen (2022-04-16)
 * Purpose: Examples of using various forms of the ACCEPT verb.
 *
 * Notes on screen-mode features:
 * - timeout: approximated with Future.get(timeout)
 * - no-echo: uses System.console().readPassword() if available
 * - upper: calls toUpperCase() on input
 * - auto-skip: reads up to N characters (limited in plain Java console)
 */
public class AcceptExample {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Basic accept syntax
        System.out.print("Simple accept. Enter a value: ");
        String wsInput = scanner.nextLine();
        System.out.println("You entered: " + wsInput);

        // Accept omitted - wait for user to press enter
        System.out.println("Press any key to enter screen mode.");
        scanner.nextLine();

        // Timeout: wait up to 3 seconds for input
        System.out.print("Enter value or wait 3 seconds: ");
        wsInput = readWithTimeout(3);
        System.out.println("You entered: " + wsInput);

        // Auto-skip: read up to 16 characters
        System.out.print("Enter 16 chars to auto skip: ");
        wsInput = scanner.nextLine();
        if (wsInput.length() > 16) {
            wsInput = wsInput.substring(0, 16);
        }
        System.out.println("You entered: " + wsInput);

        // No-echo: hide input (uses Console.readPassword if available)
        System.out.print("Enter a value (no echo): ");
        Console console = System.console();
        if (console != null) {
            char[] password = console.readPassword();
            wsInput = new String(password);
        } else {
            // Fallback when no console (e.g., IDE)
            wsInput = scanner.nextLine();
        }
        System.out.println("You entered: " + wsInput);

        // Upper: convert input to uppercase
        System.out.print("Enter a value: ");
        wsInput = scanner.nextLine().toUpperCase();
        System.out.println("You entered: " + wsInput);
    }

    /**
     * Reads a line from stdin with a timeout in seconds.
     * Returns empty string if timeout expires.
     */
    private static String readWithTimeout(int timeoutSeconds) {
        // Use daemon thread so the JVM can shut down cleanly if the read is still blocked.
        // Poll System.in.available() to avoid a non-interruptible blocking read that
        // would steal input from the main Scanner after a timeout.
        ThreadFactory daemonFactory = r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        };
        ExecutorService executor = Executors.newSingleThreadExecutor(daemonFactory);
        Future<String> future = executor.submit((Callable<String>) () -> {
            // Poll System.in so Thread.interrupt() can stop us
            while (System.in.available() == 0) {
                Thread.sleep(100);
            }
            Scanner s = new Scanner(System.in);
            return s.nextLine();
        });
        try {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            System.out.println();
            return "";
        } catch (InterruptedException | ExecutionException e) {
            return "";
        } finally {
            executor.shutdownNow();
        }
    }
}
