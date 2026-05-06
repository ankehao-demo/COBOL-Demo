package com.example.cobol.accept;

import java.util.Scanner;

import com.example.cobol.AnsiTerm;

/**
 * Java port of {@code accept/accept-secure.cbl}.
 *
 * <p>Demonstrates the COBOL {@code ACCEPT … SECURE} verb that hides the
 * user's input. Java's analogue is {@link System#console()} +
 * {@link java.io.Console#readPassword()}, which masks input on a real TTY.
 * If the JVM has no attached console (e.g. inside an IDE) we fall back to
 * a regular Scanner read.
 */
public final class AcceptSecure {

    private AcceptSecure() {}

    public static void main(String[] args) {
        System.out.print(AnsiTerm.moveTo(1, 1) + "Enter password: ");

        String password;
        if (System.console() != null) {
            char[] chars = System.console().readPassword();
            password = chars == null ? "" : new String(chars);
        } else {
            password = new Scanner(System.in).nextLine();
        }

        System.out.print(AnsiTerm.moveTo(2, 4) + "You entered: " + AnsiTerm.moveTo(2, 17) + password);
        System.out.println();
    }
}
