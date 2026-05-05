package com.coboldemo.subprogram;

import java.util.Scanner;

/**
 * Java port of {@code sub_program/main_app.cbl}.
 *
 * Drives {@link SubApp} three times to demonstrate:
 * <ol>
 *   <li>Calling BY CONTENT - changes made in the sub do not propagate
 *       back to the caller (we pass copies and discard the mutated
 *       holders).</li>
 *   <li>Calling BY REFERENCE - the caller observes the sub's mutations.
 *       The sub's working-storage retains its values between calls.</li>
 *   <li>CANCEL followed by another BY REFERENCE call - working-storage
 *       is reset (we replace the {@link SubApp} instance with a fresh one).</li>
 * </ol>
 */
public class MainApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.print("Enter value for #1: ");
        String item1 = scanner.hasNextLine() ? scanner.nextLine() : "";
        System.out.print("Enter value for #2: ");
        String item2 = scanner.hasNextLine() ? scanner.nextLine() : "";

        SubApp sub = new SubApp();

        // Simulate BY CONTENT: pass copies, ignore any returned mutations.
        displayMessage(item1, item2);
        System.out.println("Calling sub program by content:");
        String[] copy1 = new String[] {item1};
        String[] copy2 = new String[] {item2};
        sub.execute(copy1, copy2);
        // Discard copy1/copy2 mutations; caller's vars are unchanged.
        displayMessage(item1, item2);

        // Simulate BY REFERENCE: pass holders, allow mutation to flow back.
        System.out.println("Second call of sub program should retain WS values.");
        System.out.println("Calling sub program by reference:");
        String[] ref1 = new String[] {item1};
        String[] ref2 = new String[] {item2};
        sub.execute(ref1, ref2);
        item1 = ref1[0];
        item2 = ref2[0];
        displayMessage(item1, item2);

        // CANCEL: replace the sub-program instance to flush working storage.
        System.out.println("Cancelling sub program");
        sub = new SubApp();
        System.out.println("Calling sub program. WS values should be reset:");
        String[] ref3 = new String[] {item1};
        String[] ref4 = new String[] {item2};
        sub.execute(ref3, ref4);
        item1 = ref3[0];
        item2 = ref4[0];
        displayMessage(item1, item2);
    }

    private static void displayMessage(String item1, String item2) {
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println("Main app: " + item1 + item2);
    }
}
