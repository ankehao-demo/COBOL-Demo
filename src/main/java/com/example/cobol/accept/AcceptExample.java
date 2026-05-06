package com.example.cobol.accept;

import java.util.Scanner;

import com.example.cobol.AnsiTerm;

/**
 * Java port of {@code accept/accept.cbl}.
 *
 * <p>Demonstrates the various forms of the COBOL {@code ACCEPT} verb. Java
 * doesn't have a single-keyword equivalent for each variant, so we simulate
 * the behaviour with {@link Scanner}, {@link String#toUpperCase()}, ANSI
 * cursor positioning, etc.
 */
public final class AcceptExample {

    private AcceptExample() {}

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // Basic ACCEPT: read a value and echo it back.
        System.out.print("Simple accept. Enter a value: ");
        String wsInput = in.nextLine();
        System.out.println("You entered: " + wsInput);

        // ACCEPT OMITTED: just wait for a key press without storing the value.
        System.out.println("Press enter to enter screen mode.");
        in.nextLine();

        // From here out, the original COBOL example switches into screen
        // mode. We approximate that with ANSI cursor positioning. The various
        // ACCEPT options (TIMEOUT, AUTO-SKIP, NO-ECHO, UPPER) don't have
        // direct stdin equivalents in plain Java, so each is faked with the
        // closest behaviour.

        // TIMEOUT — Java's BufferedReader has no read-with-timeout in the
        // standard library; we just note it.
        System.out.print(AnsiTerm.moveTo(1, 1) + "Enter value (timeout not enforced): ");
        wsInput = in.nextLine();
        System.out.print(AnsiTerm.moveTo(2, 1) + "You entered: " + AnsiTerm.moveTo(2, 14) + wsInput);
        System.out.println();

        // AUTO-SKIP — submit automatically once the value reaches a max
        // width. Java has no auto-skip on stdin so we just truncate.
        System.out.print(AnsiTerm.moveTo(3, 1) + "Enter 16 chars (will be truncated): ");
        wsInput = in.nextLine();
        if (wsInput.length() > 16) {
            wsInput = wsInput.substring(0, 16);
        }
        System.out.print(AnsiTerm.moveTo(4, 1) + "You entered: " + AnsiTerm.moveTo(4, 14) + wsInput);
        System.out.println();

        // NO-ECHO — silently consume the line. We use System.console() if
        // the JVM is attached to a real terminal, otherwise we fall back
        // and just don't echo.
        System.out.print(AnsiTerm.moveTo(5, 1) + "Enter a value (no echo): ");
        if (System.console() != null) {
            char[] hidden = System.console().readPassword();
            wsInput = hidden == null ? "" : new String(hidden);
        } else {
            wsInput = in.nextLine();
        }
        System.out.print(AnsiTerm.moveTo(6, 1) + "You entered: " + AnsiTerm.moveTo(6, 14) + wsInput);
        System.out.println();

        // UPPER — convert anything the user enters to uppercase.
        System.out.print(AnsiTerm.moveTo(7, 1) + "Enter a value: ");
        wsInput = in.nextLine().toUpperCase();
        System.out.print(AnsiTerm.moveTo(8, 1) + "You entered: " + AnsiTerm.moveTo(8, 14) + wsInput);
        System.out.println();
    }
}
