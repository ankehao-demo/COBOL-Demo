package com.coboldemo.io;

import java.util.Scanner;

/**
 * Migrated from: accept/accept.cbl
 *
 * Demonstrates various forms of the ACCEPT verb for console input,
 * including simple accept, timeout simulation, auto-skip (max-length),
 * no-echo input, and uppercase conversion.
 */
public class AcceptExample {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Basic accept: entered value is stored in the variable provided
        System.out.print("Simple accept. Enter a value: ");
        String input = scanner.nextLine();
        if (input.length() > 16) {
            input = input.substring(0, 16);
        }
        System.out.println("You entered: " + input);

        // Accept omitted: waits for user input but does not store it
        System.out.println("Press Enter to continue.");
        scanner.nextLine();

        // Timeout: in COBOL this waits 3 seconds; here we just read with a prompt
        System.out.print("Enter value (timeout not enforced in Java): ");
        input = scanner.nextLine();
        if (input.length() > 16) {
            input = input.substring(0, 16);
        }
        System.out.println("You entered: " + input);

        // Auto-skip: automatically accepts input once it reaches 16 characters
        System.out.print("Enter up to 16 chars (auto-skip): ");
        input = scanner.nextLine();
        if (input.length() > 16) {
            input = input.substring(0, 16);
        }
        System.out.println("You entered: " + input);

        // No-echo: in COBOL text entered is not displayed; use Console.readPassword
        // when available, otherwise fall back to Scanner
        java.io.Console console = System.console();
        if (console != null) {
            char[] noEcho = console.readPassword("Enter a value (no echo): ");
            input = new String(noEcho);
            if (input.length() > 16) {
                input = input.substring(0, 16);
            }
        } else {
            System.out.print("Enter a value (no echo - console unavailable, input visible): ");
            input = scanner.nextLine();
            if (input.length() > 16) {
                input = input.substring(0, 16);
            }
        }
        System.out.println("You entered: " + input);

        // Upper: converts any input from the user to uppercase
        System.out.print("Enter a value: ");
        input = scanner.nextLine();
        if (input.length() > 16) {
            input = input.substring(0, 16);
        }
        input = input.toUpperCase();
        System.out.println("You entered: " + input);

        scanner.close();
    }
}
