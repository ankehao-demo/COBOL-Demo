package com.coboldemo.subprogram;

import java.util.Scanner;

/**
 * Java equivalent of sub_program/main_app.cbl
 *
 * Main application that calls SubApp demonstrating COBOL CALL conventions:
 * BY CONTENT (pass copies, caller values unchanged) and BY REFERENCE
 * (pass references, caller sees modifications). Also demonstrates CANCEL
 * (re-instantiate to reset working-storage state).
 *
 * COBOL Mapping:
 *   CALL "sub-app" USING BY CONTENT ws-item-1 BY CONTENT ws-item-2
 *       → subApp.execute(new StringBuilder(copy1), new StringBuilder(copy2))
 *   CALL "sub-app" USING ws-item-1 ws-item-2 (by reference)
 *       → subApp.execute(item1, item2) with shared StringBuilders
 *   CANCEL "sub-app"
 *       → subApp = new SubApp() (re-instantiate to reset working-storage)
 */
public class MainApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SubApp subApp = new SubApp();

        System.out.println();
        System.out.print("Enter value for #1: ");
        String val1 = scanner.nextLine();

        System.out.print("Enter value for #2: ");
        String val2 = scanner.nextLine();

        // Use StringBuilders as mutable wrappers (simulates BY REFERENCE)
        StringBuilder item1 = new StringBuilder(pad(val1, 10));
        StringBuilder item2 = new StringBuilder(pad(val2, 10));

        displayMessage(item1, item2);

        // CALL "sub-app" USING BY CONTENT → pass copies, originals unchanged
        System.out.println("Calling sub program by content:");
        subApp.execute(new StringBuilder(item1), new StringBuilder(item2));
        displayMessage(item1, item2);

        // Second call BY REFERENCE → sub-program can modify caller's values
        // Working-storage values in sub-program should be retained from first call
        System.out.println("Second call of sub program should retain WS values.");
        System.out.println("Calling sub program by reference:");
        subApp.execute(item1, item2);
        displayMessage(item1, item2);

        // CANCEL "sub-app" → re-instantiate to reset working-storage
        System.out.println("Cancelling sub program");
        subApp = new SubApp();
        System.out.println("Calling sub program. WS values should be reset:");
        subApp.execute(item1, item2);
        displayMessage(item1, item2);

        scanner.close();
    }

    private static void displayMessage(StringBuilder item1, StringBuilder item2) {
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println("Main app: " + item1 + item2);
    }

    private static String pad(String s, int len) {
        if (s == null) s = "";
        return String.format("%-" + len + "s", s).substring(0, len);
    }
}
