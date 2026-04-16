package com.coboldemo.accept;

import java.io.Console;
import java.util.Scanner;

/**
 * Migrated from: accept/accept-secure.cbl
 * Original author: Erik Eriksen (2022-02-09)
 * Purpose: Shows using secure (password) input to hide text.
 */
public class AcceptSecure {

    public static void main(String[] args) {
        System.out.print("Enter password: ");

        String wsPassword;
        Console console = System.console();
        if (console != null) {
            char[] password = console.readPassword();
            wsPassword = new String(password);
        } else {
            // Fallback when no console available (e.g., running in IDE)
            Scanner scanner = new Scanner(System.in);
            wsPassword = scanner.nextLine();
        }

        System.out.println("You entered: " + wsPassword);
    }
}
