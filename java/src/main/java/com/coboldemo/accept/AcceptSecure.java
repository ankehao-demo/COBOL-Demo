package com.coboldemo.accept;

import java.util.Scanner;

/**
 * Migrated from accept/accept-secure.cbl
 * Demonstrates using SECURE in ACCEPT to hide text input.
 */
public class AcceptSecure {

    public static void main(String[] args) {
        System.out.print("Enter password: ");
        String password;
        if (System.console() != null) {
            char[] hidden = System.console().readPassword();
            password = new String(hidden);
        } else {
            // Fallback when no console is available (e.g., running from IDE)
            Scanner scanner = new Scanner(System.in);
            password = scanner.nextLine();
            scanner.close();
        }
        System.out.println("You entered: " + password);
    }
}
