package com.coboldemo.modular;

import java.util.Scanner;

/**
 * Migrated from: sub_program/main_app.cbl
 *
 * Main application that calls SubApp demonstrating COBOL inter-program
 * communication concepts:
 * - CALL BY CONTENT: pass copies (modifications not visible to caller)
 * - CALL BY REFERENCE: pass by reference (modifications visible to caller)
 * - CANCEL: reset sub-program's working-storage by creating a new instance
 */
public class MainApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.print("Enter value for #1: ");
        String item1 = padRight(scanner.nextLine(), 10);

        System.out.print("Enter value for #2: ");
        String item2 = padRight(scanner.nextLine(), 10);

        displayMessage(item1, item2);

        SubApp subApp = new SubApp();

        // CALL BY CONTENT: pass copies of values; original variables unchanged
        System.out.println("Calling sub program by content:");
        String[] contentCopy = {item1, item2};
        subApp.execute(contentCopy);
        // item1 and item2 remain unchanged (by content)
        displayMessage(item1, item2);

        // CALL BY REFERENCE: pass mutable array; sub can modify values
        System.out.println("Second call of sub program should retain WS values.");
        System.out.println("Calling sub program by reference:");
        String[] refItems = {item1, item2};
        subApp.execute(refItems);
        // BY REFERENCE: pick up modifications
        item1 = refItems[0];
        item2 = refItems[1];
        displayMessage(item1, item2);

        // CANCEL: create a new SubApp instance to reset working-storage
        System.out.println("Cancelling sub program");
        subApp = new SubApp();
        System.out.println("Calling sub program. WS values should be reset:");
        String[] cancelItems = {item1, item2};
        subApp.execute(cancelItems);
        item1 = cancelItems[0];
        item2 = cancelItems[1];
        displayMessage(item1, item2);

        scanner.close();
    }

    private static void displayMessage(String item1, String item2) {
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println("Main app: " + item1 + item2);
    }

    private static String padRight(String s, int len) {
        if (s == null) s = "";
        if (s.length() >= len) return s.substring(0, len);
        return String.format("%-" + len + "s", s);
    }
}
