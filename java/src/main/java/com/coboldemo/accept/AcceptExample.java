package com.coboldemo.accept;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Migrated from accept/accept.cbl
 * Demonstrates various forms of the ACCEPT verb in COBOL.
 */
public class AcceptExample {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Basic accept syntax
        System.out.print("Simple accept. Enter a value: ");
        String wsInput = scanner.nextLine();
        System.out.println("You entered: " + wsInput);

        // Accept omitted waits for user input but does not store it
        System.out.println("Press any key to enter screen mode.");
        scanner.nextLine();

        // Timeout: wait 3 seconds for input
        System.out.print("Enter value or wait 3 seconds: ");
        wsInput = readWithTimeout(3);
        System.out.println("You entered: " + wsInput);

        // Auto-skip: read up to 16 characters
        // Note: true auto-skip (submitting after N chars without Enter) requires raw terminal mode.
        // This approximation reads a line and truncates to 16 chars.
        System.out.print("Enter 16 chars to auto skip: ");
        scanner = new Scanner(System.in);
        wsInput = scanner.nextLine();
        if (wsInput.length() > 16) {
            wsInput = wsInput.substring(0, 16);
        }
        System.out.println("You entered: " + wsInput);

        // No-echo: hide user input during entry
        System.out.print("Enter a value (no echo): ");
        if (System.console() != null) {
            char[] hidden = System.console().readPassword();
            wsInput = new String(hidden);
        } else {
            wsInput = scanner.nextLine();
        }
        System.out.println("You entered: " + wsInput);

        // Upper: convert input to uppercase
        System.out.print("Enter a value: ");
        wsInput = scanner.nextLine().toUpperCase();
        System.out.println("You entered: " + wsInput);

        scanner.close();
    }

    private static String readWithTimeout(int timeoutSeconds) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                return reader.readLine();
            }
        });
        try {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            return "";
        } catch (InterruptedException | ExecutionException e) {
            return "";
        } finally {
            executor.shutdownNow();
        }
    }
}
