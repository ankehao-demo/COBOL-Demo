package com.coboldemo.accept;

import java.io.Console;
import java.util.Scanner;

/**
 * Migrated from: accept/accept-secure.cbl
 * Original author: Erik Eriksen (2022-02-09)
 * Purpose: Shows using SECURE in ACCEPT statement to hide text.
 *
 * COBOL-to-Java mapping:
 *   ACCEPT ws-password SECURE  -> Console.readPassword() (shows '*' in COBOL,
 *                                 hides all characters in Java)
 *   DISPLAY ... AT RRCC        -> ANSI escape codes: \033[row;colH
 *
 * Note: COBOL SECURE displays '*' for each character typed, while Java's
 * Console.readPassword() hides all characters. The end result (hidden input)
 * is functionally equivalent.
 */
public class AcceptSecure {

    // COBOL: 01 ws-password pic x(16).
    private static final int WS_PASSWORD_LENGTH = 16;

    /**
     * Moves the cursor to the specified row and column using ANSI escape codes.
     * COBOL "AT RRCC" maps row (RR) and column (CC) to ANSI \033[row;colH.
     */
    private static void moveCursor(int row, int col) {
        System.out.printf("\033[%d;%dH", row, col);
    }

    /**
     * Pads or truncates a string to exactly the given length, matching
     * COBOL PIC X(n) behavior where the field is always n characters wide.
     */
    private static String picX(String value, int length) {
        if (value == null) {
            value = "";
        }
        if (value.length() > length) {
            return value.substring(0, length);
        }
        return String.format("%-" + length + "s", value);
    }

    public static void main(String[] args) {
        // Clear screen to approximate COBOL screen mode entry
        System.out.print("\033[2J");

        // ---------------------------------------------------------------
        // COBOL: display "Enter password: " at 0101
        //        accept ws-password secure at 0117
        //        display "You entered: " at 0204 ws-password at 0217
        //
        // SECURE: In COBOL, characters are masked with '*' during entry.
        // Java's Console.readPassword() suppresses all echo instead.
        // ---------------------------------------------------------------
        moveCursor(1, 1);
        System.out.print("Enter password: ");
        moveCursor(1, 17);

        String wsPassword;
        Console console = System.console();
        if (console != null) {
            char[] passwordChars = console.readPassword();
            wsPassword = passwordChars != null ? new String(passwordChars) : "";
        } else {
            // Fallback when Console is not available (IDE, piped input)
            // Note: SECURE/echo suppression not possible without Console
            System.out.print("(secure input unavailable in this environment) ");
            Scanner scanner = new Scanner(System.in);
            wsPassword = scanner.nextLine();
            scanner.close();
        }

        wsPassword = picX(wsPassword, WS_PASSWORD_LENGTH);

        moveCursor(2, 4);
        System.out.print("You entered: ");
        moveCursor(2, 17);
        System.out.println(wsPassword);
    }
}
