package com.coboldemo.accept;

import java.io.Console;
import java.util.Scanner;

/**
 * Port of {@code accept/accept-secure.cbl} — reads a password without echoing
 * it to the screen. COBOL uses {@code ACCEPT ... SECURE} which masks input
 * with '*' characters; {@link Console#readPassword()} in Java suppresses the
 * echo entirely, which is the closest idiomatic equivalent.
 */
public final class AcceptSecure {

    private AcceptSecure() {
    }

    public static void main(String[] args) {
        Console console = System.console();
        String password;
        if (console != null) {
            char[] entered = console.readPassword("Enter password: ");
            password = new String(entered);
        } else {
            // Fallback when the program is not attached to a terminal
            // (for example when run from an IDE or with piped input).
            System.out.print("Enter password (fallback echo): ");
            password = new Scanner(System.in).nextLine();
        }
        System.out.println("You entered: " + password);
    }
}
