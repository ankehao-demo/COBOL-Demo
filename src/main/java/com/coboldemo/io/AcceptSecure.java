package com.coboldemo.io;

import java.util.Scanner;

/**
 * Migrated from: accept/accept-secure.cbl
 *
 * Demonstrates secure (password-masked) input using Console.readPassword().
 * Falls back to Scanner if no console is available.
 */
public class AcceptSecure {

    public static void main(String[] args) {
        String password;
        java.io.Console console = System.console();

        if (console != null) {
            char[] chars = console.readPassword("Enter password: ");
            password = new String(chars);
        } else {
            System.out.print("Enter password (no console - input visible): ");
            Scanner scanner = new Scanner(System.in);
            password = scanner.nextLine();
            scanner.close();
        }

        if (password.length() > 16) {
            password = password.substring(0, 16);
        }
        System.out.println("You entered: " + password);
    }
}
