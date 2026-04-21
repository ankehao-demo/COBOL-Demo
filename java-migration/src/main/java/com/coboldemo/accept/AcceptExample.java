package com.coboldemo.accept;

import java.io.Console;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Port of {@code accept/accept.cbl} — examples of the COBOL ACCEPT verb.
 *
 * <p>The original COBOL program relied on "screen mode" for features like
 * positioning text at specific row/column coordinates, auto-skip, no-echo
 * and upper-case conversion. Screen mode is a terminal-graphics concept that
 * does not have a direct equivalent in standard Java I/O, so the port below
 * performs sequential console I/O and documents what the COBOL source did.
 */
public final class AcceptExample {

    private AcceptExample() {
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Basic accept — read a value from the console.
        System.out.print("Simple accept. Enter a value: ");
        String input = scanner.nextLine();
        System.out.println("You entered: " + input);

        // ACCEPT OMITTED — wait for the user to press a key without storing it.
        System.out.println("Press enter to continue.");
        scanner.nextLine();

        // Timeout — wait up to 3 seconds for input, otherwise continue.
        // COBOL: accept ws-input timeout 3.
        System.out.print("Enter value or wait 3 seconds: ");
        String timed = readWithTimeout(3);
        System.out.println("You entered: " + (timed == null ? "<timed out>" : timed));

        // Auto-skip — automatically accept input once the declared length
        // (PIC X(16)) is reached. We simulate it by truncating at 16 chars.
        System.out.print("Enter up to 16 chars (auto skip at 16): ");
        String autoSkip = scanner.nextLine();
        if (autoSkip.length() > 16) {
            autoSkip = autoSkip.substring(0, 16);
        }
        System.out.println("You entered: " + autoSkip);

        // No-echo — read without echoing characters to the screen.
        // System.console() returns null when not attached to a terminal
        // (e.g. when the program is piped). Fall back to a plain read.
        Console console = System.console();
        String noEcho;
        if (console != null) {
            noEcho = new String(console.readPassword("Enter a value (no echo): "));
        } else {
            System.out.print("Enter a value (no echo, fallback echo): ");
            noEcho = scanner.nextLine();
        }
        System.out.println("You entered: " + noEcho);

        // Upper — convert input to uppercase, mirroring the COBOL UPPER
        // accept option which is commonly used for y/n style prompts.
        System.out.print("Enter a value: ");
        String upper = scanner.nextLine().toUpperCase();
        System.out.println("You entered: " + upper);
    }

    /**
     * Reads a line of input from {@code System.in}, returning {@code null}
     * if nothing was entered within {@code seconds}.
     */
    private static String readWithTimeout(int seconds) {
        ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "accept-timeout");
            t.setDaemon(true);
            return t;
        });
        try {
            Callable<String> task = () -> new Scanner(System.in).nextLine();
            Future<String> future = executor.submit(task);
            try {
                return future.get(seconds, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                future.cancel(true);
                return null;
            } catch (Exception e) {
                return null;
            }
        } finally {
            executor.shutdownNow();
        }
    }
}
