package com.coboldemo.accept;

import java.io.Console;
import java.util.Scanner;

/**
 * Java port of accept/accept-secure.cbl.
 *
 * COBOL's ACCEPT ... SECURE hides keystrokes with '*'. Java doesn't expose a
 * direct "show *" API, but {@link Console#readPassword()} suppresses echo
 * entirely which is the conventional secure-entry pattern on the JVM.
 */
public final class AcceptSecure {

    private AcceptSecure() {
    }

    public static void main(String[] args) {
        String password;

        System.out.print("Enter password: ");
        Console console = System.console();
        if (console != null) {
            char[] chars = console.readPassword();
            password = chars == null ? "" : new String(chars);
        } else {
            // Fallback for IDE / piped stdin where Console is unavailable.
            Scanner scanner = new Scanner(System.in);
            password = scanner.nextLine();
        }

        System.out.println("You entered: " + password);
    }
}
