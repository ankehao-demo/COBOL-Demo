package com.coboldemo.accept;

import java.io.Console;
import java.util.Scanner;

/**
 * Java port of {@code accept/accept-secure.cbl}.
 *
 * Demonstrates COBOL's ACCEPT ... SECURE: read a value without echoing
 * the characters to the screen, then display the captured value.
 *
 * In Java this is most directly expressed with
 * {@link java.io.Console#readPassword(String, Object...)}. When no system
 * console is attached (e.g. when launched from an IDE), we fall back to a
 * normal {@link Scanner} read so the program is still runnable.
 */
public class AcceptSecureExample {

    public static void main(String[] args) {
        Console console = System.console();
        String password;
        if (console != null) {
            char[] entered = console.readPassword("Enter password: ");
            password = entered == null ? "" : new String(entered);
        } else {
            System.out.print("Enter password: ");
            Scanner s = new Scanner(System.in);
            password = s.hasNextLine() ? s.nextLine() : "";
            System.out.println("(note: no system console; input was not hidden)");
        }
        System.out.println("You entered: " + password);
    }
}
