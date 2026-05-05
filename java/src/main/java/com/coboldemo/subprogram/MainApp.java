package com.coboldemo.subprogram;

import java.util.Scanner;

/**
 * Migrated from sub_program/main_app.cbl
 * Main application that calls SubApp by content and by reference.
 */
public class MainApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SubApp subApp = new SubApp();

        System.out.println();
        System.out.print("Enter value for #1: ");
        String item1 = padRight(scanner.nextLine(), 10);

        System.out.print("Enter value for #2: ");
        String item2 = padRight(scanner.nextLine(), 10);

        displayMessage(item1, item2);

        // Call by content: variables passed will NOT be modified upon return
        System.out.println("Calling sub program by content:");
        subApp.executeByContent(item1, item2);
        displayMessage(item1, item2);

        // Call by reference: variables CAN be modified by the sub program
        // Working-storage of sub program retains values between calls
        System.out.println("Second call of sub program should retain WS values.");
        System.out.println("Calling sub program by reference:");
        String[] items = {item1, item2};
        subApp.execute(items);
        item1 = items[0];
        item2 = items[1];
        displayMessage(item1, item2);

        // Cancel sub program: resets working-storage
        System.out.println("Cancelling sub program");
        subApp.reset();
        System.out.println("Calling sub program. WS values should be reset:");
        items = new String[]{item1, item2};
        subApp.execute(items);
        item1 = items[0];
        item2 = items[1];
        displayMessage(item1, item2);

        scanner.close();
    }

    private static void displayMessage(String item1, String item2) {
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println("Main app: " + item1 + item2);
    }

    private static String padRight(String s, int width) {
        if (s.length() >= width) return s.substring(0, width);
        return String.format("%-" + width + "s", s);
    }
}
