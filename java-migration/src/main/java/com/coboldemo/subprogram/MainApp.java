package com.coboldemo.subprogram;

import com.coboldemo.subprogram.SubApp.Holder;

import java.util.Scanner;

/**
 * Port of {@code sub_program/main_app.cbl} — drives {@link SubApp} in three
 * different modes to demonstrate COBOL CALL semantics:
 * <ul>
 *     <li>{@code BY CONTENT}: the sub program cannot modify the caller's
 *         values, so we pass copies wrapped in {@link Holder} instances
 *         whose updates are ignored afterwards.</li>
 *     <li>{@code BY REFERENCE}: the sub program can modify the caller's
 *         values, so we pass the same {@link Holder} instances.</li>
 *     <li>{@code CANCEL}: resets the sub program's working storage. In Java
 *         this is emulated by instantiating a new {@link SubApp}.</li>
 * </ul>
 */
public final class MainApp {

    private MainApp() {
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.print("Enter value for #1: ");
        String wsItem1 = scanner.nextLine();
        System.out.print("Enter value for #2: ");
        String wsItem2 = scanner.nextLine();

        displayMessage(wsItem1, wsItem2);

        // BY CONTENT — pass copies, drop any mutation the sub-program makes.
        System.out.println("Calling sub program by content:");
        SubApp sub = new SubApp();
        Holder<String> contentA = new Holder<>(wsItem1);
        Holder<String> contentB = new Holder<>(wsItem2);
        sub.run(contentA, contentB);
        displayMessage(wsItem1, wsItem2);

        // BY REFERENCE (the COBOL default) — pass the holders directly so
        // the caller observes the sub-program's changes.
        System.out.println("Second call of sub program should retain WS values.");
        System.out.println("Calling sub program by reference:");
        Holder<String> refA = new Holder<>(wsItem1);
        Holder<String> refB = new Holder<>(wsItem2);
        sub.run(refA, refB);
        wsItem1 = refA.get();
        wsItem2 = refB.get();
        displayMessage(wsItem1, wsItem2);

        // CANCEL — emulate by creating a brand-new SubApp instance, which
        // wipes the "working storage" represented by its instance fields.
        System.out.println("Cancelling sub program");
        sub = new SubApp();
        System.out.println("Calling sub program. WS values should be reset:");
        Holder<String> finalA = new Holder<>(wsItem1);
        Holder<String> finalB = new Holder<>(wsItem2);
        sub.run(finalA, finalB);
        wsItem1 = finalA.get();
        wsItem2 = finalB.get();
        displayMessage(wsItem1, wsItem2);
    }

    private static void displayMessage(String item1, String item2) {
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println("Main app: " + item1 + item2);
    }
}
