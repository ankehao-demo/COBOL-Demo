package com.example.cobol.subprogram;

import java.util.Scanner;

import com.example.cobol.subprogram.Sub.MutableString;

/**
 * Java port of {@code sub_program/main_app.cbl}.
 *
 * <p>Demonstrates calling a sub-program both "by content" (the called
 * routine cannot mutate the caller's variables) and "by reference"
 * (mutations propagate back). In Java we model the latter with a
 * {@link MutableString} holder, and the former by creating an independent
 * holder around a copy of the value. "Cancelling" the sub-program is
 * modelled as creating a new {@link Sub} instance.
 */
public final class MainApp {

    private MainApp() {}

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println();
        System.out.print("Enter value for #1: ");
        String wsItem1 = in.hasNextLine() ? padTo10(in.nextLine()) : padTo10("");

        System.out.print("Enter value for #2: ");
        String wsItem2 = in.hasNextLine() ? padTo10(in.nextLine()) : padTo10("");

        displayMessage(wsItem1, wsItem2);

        Sub sub = new Sub();

        // BY CONTENT — call with copies; the caller's variables are
        // untouched after the call.
        System.out.println("Calling sub program by content:");
        MutableString copy1 = new MutableString(wsItem1);
        MutableString copy2 = new MutableString(wsItem2);
        sub.call(copy1, copy2);
        displayMessage(wsItem1, wsItem2);

        // BY REFERENCE — call with mutable holders; sub-program mutations
        // propagate back. Note WS values inside Sub still hold the previous
        // call's data because we reuse the same instance.
        System.out.println("Second call of sub program should retain WS values.");
        System.out.println("Calling sub program by reference:");
        MutableString ref1 = new MutableString(wsItem1);
        MutableString ref2 = new MutableString(wsItem2);
        sub.call(ref1, ref2);
        wsItem1 = ref1.value;
        wsItem2 = ref2.value;
        displayMessage(wsItem1, wsItem2);

        // CANCEL — drop the existing Sub instance; the next call sees fresh
        // working-storage state.
        System.out.println("Cancelling sub program");
        sub = new Sub();
        System.out.println("Calling sub program. WS values should be reset:");
        ref1 = new MutableString(wsItem1);
        ref2 = new MutableString(wsItem2);
        sub.call(ref1, ref2);
        wsItem1 = ref1.value;
        wsItem2 = ref2.value;
        displayMessage(wsItem1, wsItem2);
    }

    private static void displayMessage(String item1, String item2) {
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println("Main app: " + item1 + item2);
    }

    /** Pads / truncates to mirror PIC X(10). */
    private static String padTo10(String s) {
        if (s.length() > 10) {
            return s.substring(0, 10);
        }
        return String.format("%-10s", s);
    }
}
