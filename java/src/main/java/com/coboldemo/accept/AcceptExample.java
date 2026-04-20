package com.coboldemo.accept;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Java equivalent of accept/accept.cbl
 *
 * Demonstrates various forms of the ACCEPT verb in COBOL, mapped to Java
 * console I/O equivalents. Screen-mode positioning (AT LLCC) is replaced
 * with plain console output; use Lanterna for positioned terminal output.
 *
 * All reads use a single BufferedReader wrapping System.in to avoid the
 * buffer-desync issues that arise when mixing Scanner with direct System.in reads.
 */
public class AcceptExample {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Basic accept: ACCEPT ws-input
        System.out.print("Simple accept. Enter a value: ");
        String input = reader.readLine();
        System.out.println("You entered: " + input);

        // ACCEPT OMITTED: wait for user input without storing it
        System.out.println("Press any key to continue.");
        reader.readLine();

        // ACCEPT ... TIMEOUT 3: wait up to 3 seconds for input
        System.out.println("Enter value or wait 3 seconds: ");
        input = readWithTimeout(reader, 3);
        System.out.println("You entered: " + input);

        // ACCEPT ... AUTO-SKIP: read up to N characters (ws-input is PIC X(16))
        System.out.print("Enter 16 chars to auto skip: ");
        input = readUpToNChars(reader, 16);
        System.out.println("You entered: " + input);

        // ACCEPT ... NO-ECHO: read input without echoing characters
        System.out.print("Enter a value (no echo): ");
        if (System.console() != null) {
            char[] noEcho = System.console().readPassword();
            input = new String(noEcho);
        } else {
            input = reader.readLine();
            System.out.println("(no-echo not supported in this terminal)");
        }
        System.out.println("You entered: " + input);

        // ACCEPT ... UPPER: convert input to uppercase
        System.out.print("Enter a value: ");
        input = reader.readLine().toUpperCase();
        System.out.println("You entered: " + input);
    }

    /**
     * Reads a line from System.in with a timeout in seconds.
     * Equivalent to COBOL: ACCEPT ws-input TIMEOUT 3
     *
     * Uses polling via reader.ready() to check for available input
     * without blocking, avoiding buffer desync with the shared reader.
     */
    private static String readWithTimeout(BufferedReader reader, int timeoutSeconds) {
        long deadline = System.currentTimeMillis() + timeoutSeconds * 1000L;
        StringBuilder sb = new StringBuilder();
        try {
            while (System.currentTimeMillis() < deadline) {
                if (reader.ready()) {
                    int ch = reader.read();
                    if (ch == '\n' || ch == -1) {
                        return sb.toString();
                    }
                    if (ch != '\r') {
                        sb.append((char) ch);
                    }
                } else {
                    Thread.sleep(100);
                }
            }
        } catch (IOException | InterruptedException e) {
            // timeout or interruption — return what we have
        }
        return sb.toString();
    }

    /**
     * Reads up to maxChars characters from the scanner.
     * Equivalent to COBOL: ACCEPT ws-input AUTO-SKIP (auto-submits when PIC length reached)
     */
    private static String readUpToNChars(BufferedReader reader, int maxChars) throws IOException {
        String line = reader.readLine();
        if (line != null && line.length() > maxChars) {
            return line.substring(0, maxChars);
        }
        return line != null ? line : "";
    }
}
