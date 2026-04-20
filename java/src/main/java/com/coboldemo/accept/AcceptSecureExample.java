package com.coboldemo.accept;

import java.util.Scanner;

/**
 * Java equivalent of accept/accept-secure.cbl
 *
 * Demonstrates the COBOL ACCEPT ... SECURE statement, which hides user input
 * (showing '*' characters). In Java, System.console().readPassword() provides
 * similar functionality by suppressing echo entirely.
 */
public class AcceptSecureExample {

    public static void main(String[] args) {
        String password;

        if (System.console() != null) {
            // ACCEPT ws-password SECURE → readPassword() suppresses echo
            char[] passwordChars = System.console().readPassword("Enter password: ");
            password = new String(passwordChars);
        } else {
            // Fallback when no console is available (e.g., running in an IDE)
            System.out.print("Enter password: ");
            Scanner scanner = new Scanner(System.in);
            password = scanner.nextLine();
            scanner.close();
        }

        System.out.println("You entered: " + password);
    }
}
