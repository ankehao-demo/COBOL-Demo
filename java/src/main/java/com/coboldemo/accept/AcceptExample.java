package com.coboldemo.accept;

import java.io.Console;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Migrated from: accept/accept.cbl
 * Original author: Erik Eriksen (2022-04-16)
 * Purpose: Examples of using various forms of the ACCEPT verb.
 *
 * COBOL-to-Java mapping:
 *   ACCEPT ws-input                  -> Scanner.nextLine()
 *   ACCEPT OMITTED                   -> Scanner.nextLine() (wait for Enter)
 *   ACCEPT ws-input TIMEOUT n        -> Future.get(timeout) on a reader thread
 *   ACCEPT ws-input AUTO-SKIP        -> Read up to N characters
 *   ACCEPT ws-input NO-ECHO          -> Console.readPassword()
 *   ACCEPT ws-input UPPER            -> input.toUpperCase()
 *   DISPLAY ... AT RRCC              -> ANSI escape codes: \033[row;colH
 */
public class AcceptExample {

    // COBOL: 01 ws-input pic x(16).
    private static final int WS_INPUT_LENGTH = 16;

    /**
     * Moves the cursor to the specified row and column using ANSI escape codes.
     * COBOL "AT RRCC" maps row (RR) and column (CC) to ANSI \033[row;colH.
     */
    private static void moveCursor(int row, int col) {
        System.out.printf("\033[%d;%dH", row, col);
    }

    /**
     * Pads or truncates a string to exactly the given length, matching
     * COBOL PIC X(n) behavior where the field is always n characters wide.
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // ---------------------------------------------------------------
        // COBOL: display "Simple accept. Enter a value: " with no advancing
        //        accept ws-input
        //        display "You entered: " ws-input
        // ---------------------------------------------------------------
        System.out.print("Simple accept. Enter a value: ");
        String wsInput = picX(scanner.nextLine(), WS_INPUT_LENGTH);
        System.out.println("You entered: " + wsInput);

        // ---------------------------------------------------------------
        // COBOL: display "Press any key to enter screen mode."
        //        accept omitted
        // "accept omitted" waits for user input but discards it.
        // ---------------------------------------------------------------
        System.out.println("Press any key to enter screen mode.");
        scanner.nextLine();

        // --- From here on, COBOL enters screen mode with AT RRCC positioning ---
        // We use ANSI escape codes to approximate screen-mode positioning.

        // Clear screen (approximate COBOL screen mode entry)
        System.out.print("\033[2J");

        // ---------------------------------------------------------------
        // COBOL: display "Enter value or wait 3 seconds: " at 0101
        //        accept ws-input timeout 3 at 0132
        //        display "You entered: " at 0201 ws-input at 0214
        //
        // TIMEOUT: In COBOL, ACCEPT TIMEOUT waits n seconds for input.
        // Java equivalent uses a background thread with Future.get(timeout).
        // ---------------------------------------------------------------
        moveCursor(1, 1);
        System.out.print("Enter value or wait 3 seconds: ");
        moveCursor(1, 32);
        wsInput = readWithTimeout(scanner, 3);
        wsInput = picX(wsInput, WS_INPUT_LENGTH);
        moveCursor(2, 1);
        System.out.print("You entered: ");
        moveCursor(2, 14);
        System.out.print(wsInput);

        // ---------------------------------------------------------------
        // COBOL: display "Enter 16 chars to auto skip: " at 0301
        //        accept ws-input auto-skip at 0330
        //        display "You entered: " at 0401 ws-input at 0414
        //
        // AUTO-SKIP: In COBOL, input is automatically accepted once the
        // field width (PIC X(16) = 16 chars) is reached. In Java, we read
        // a line and truncate to 16 characters.
        // Note: True auto-skip (character-by-character) would require raw
        // terminal mode which is not available in standard Java console I/O.
        // ---------------------------------------------------------------
        moveCursor(3, 1);
        System.out.print("Enter 16 chars to auto skip: ");
        moveCursor(3, 30);
        wsInput = picX(scanner.nextLine(), WS_INPUT_LENGTH);
        moveCursor(4, 1);
        System.out.print("You entered: ");
        moveCursor(4, 14);
        System.out.print(wsInput);

        // ---------------------------------------------------------------
        // COBOL: display "Enter a value (no echo): " at 0501
        //        accept ws-input no-echo at 0526
        //        display "You entered: " at 0601 ws-input at 0614
        //
        // NO-ECHO: In COBOL, characters typed are not displayed at all.
        // Java equivalent: Console.readPassword() suppresses echo.
        // Falls back to Scanner if Console is not available (e.g., IDE).
        // ---------------------------------------------------------------
        moveCursor(5, 1);
        System.out.print("Enter a value (no echo): ");
        moveCursor(5, 26);
        wsInput = readNoEcho(scanner);
        wsInput = picX(wsInput, WS_INPUT_LENGTH);
        moveCursor(6, 1);
        System.out.print("You entered: ");
        moveCursor(6, 14);
        System.out.print(wsInput);

        // ---------------------------------------------------------------
        // COBOL: display "Enter a value: " at 0701
        //        accept ws-input upper at 0716
        //        display "You entered: " at 0801 ws-input at 0814
        //
        // UPPER: In COBOL, the UPPER option converts input to uppercase.
        // Java equivalent: String.toUpperCase()
        // ---------------------------------------------------------------
        moveCursor(7, 1);
        System.out.print("Enter a value: ");
        moveCursor(7, 16);
        wsInput = picX(scanner.nextLine().toUpperCase(), WS_INPUT_LENGTH);
        moveCursor(8, 1);
        System.out.print("You entered: ");
        moveCursor(8, 14);
        System.out.println(wsInput);

        scanner.close();
    }

    /**
     * Reads input with a timeout, approximating COBOL's ACCEPT ... TIMEOUT n.
     * Uses a separate thread to read from Scanner, with Future.get(timeout).
     *
     * @param scanner the Scanner to read from
     * @param timeoutSeconds the timeout in seconds
     * @return the input string, or empty string if timeout occurs
     */
    private static String readWithTimeout(Scanner scanner, int timeoutSeconds) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Callable<String>() {
            @Override
            public String call() {
                return scanner.nextLine();
            }
        });

        try {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            // COBOL behavior: if timeout expires, continue with empty/previous value
            future.cancel(true);
            return "";
        } catch (InterruptedException | ExecutionException e) {
            return "";
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * Reads input without echoing characters, approximating COBOL's
     * ACCEPT ... NO-ECHO. Uses Console.readPassword() when available.
     * Falls back to Scanner.nextLine() when Console is not available
     * (e.g., when running in an IDE or piped input).
     *
     * @param scanner fallback Scanner for environments without Console
     * @return the input string
     */
    private static String readNoEcho(Scanner scanner) {
        Console console = System.console();
        if (console != null) {
            char[] password = console.readPassword();
            return password != null ? new String(password) : "";
        } else {
            // Fallback: Console not available (IDE, piped input)
            // Note: echo suppression not possible without Console
            System.out.print("(no-echo unavailable in this environment) ");
            return scanner.nextLine();
        }
    }
}
