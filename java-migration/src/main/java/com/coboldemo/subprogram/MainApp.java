package com.coboldemo.subprogram;

import java.util.Scanner;

/**
 * Java port of sub_program/main_app.cbl.
 *
 * Demonstrates COBOL's CALL conventions (BY CONTENT / BY REFERENCE) and the
 * {@code CANCEL} statement which resets a sub-program's WORKING-STORAGE. In
 * Java we model:
 *
 * <ul>
 *   <li>BY CONTENT -> pass immutable {@code String} copies.</li>
 *   <li>BY REFERENCE -> pass a {@link Ref} holder so the callee can mutate
 *       the caller's view of the value.</li>
 *   <li>CANCEL -> construct a new {@link SubApp} instance (fresh
 *       working-storage).</li>
 * </ul>
 */
public final class MainApp {

    private MainApp() {
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.print("Enter value for #1: ");
        String item1 = scanner.hasNextLine() ? scanner.nextLine() : "";
        System.out.print("Enter value for #2: ");
        String item2 = scanner.hasNextLine() ? scanner.nextLine() : "";

        Ref<String> ref1 = new Ref<>(item1);
        Ref<String> ref2 = new Ref<>(item2);

        displayMessage(ref1, ref2);

        SubApp sub = new SubApp();

        // Call by content: pass immutable String copies; mutations inside the
        // sub-program do not propagate back to the caller.
        System.out.println("Calling sub program by content:");
        sub.call(new String(ref1.get()), new String(ref2.get()));
        displayMessage(ref1, ref2);

        // Call by reference: the holder's value is mutated in place.
        System.out.println("Second call of sub program should retain WS values.");
        System.out.println("Calling sub program by reference:");
        sub.callByReference(ref1, ref2);
        displayMessage(ref1, ref2);

        // CANCEL -> new instance resets working-storage state.
        System.out.println("Cancelling sub program");
        sub = new SubApp();
        System.out.println("Calling sub program. WS values should be reset:");
        sub.callByReference(ref1, ref2);
        displayMessage(ref1, ref2);
    }

    private static void displayMessage(Ref<String> a, Ref<String> b) {
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println("Main app: " + a.get() + b.get());
    }

    /** Simple mutable holder used to emulate CALL BY REFERENCE. */
    public static final class Ref<T> {
        private T value;

        public Ref(T value) {
            this.value = value;
        }

        public T get() {
            return value;
        }

        public void set(T value) {
            this.value = value;
        }
    }
}
