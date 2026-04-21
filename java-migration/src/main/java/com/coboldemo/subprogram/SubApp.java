package com.coboldemo.subprogram;

/**
 * Port of {@code sub_program/sub.cbl} — callee-side working storage,
 * linkage and (emulated) local-storage.
 *
 * <p>In COBOL, {@code WORKING-STORAGE} fields persist across calls until a
 * {@code CANCEL} statement is executed. That maps cleanly onto instance
 * fields in Java: keep the same {@code SubApp} instance to mimic persistent
 * working storage, and instantiate a new {@code SubApp} to mimic
 * {@code CANCEL}.
 *
 * <p>{@code LOCAL-STORAGE} fields reset on every call; we emulate them as
 * local variables inside {@link #run(Holder, Holder)}.
 *
 * <p>The linkage section is represented by {@link Holder} wrappers so the
 * caller can see the updates, matching the COBOL default of
 * {@code BY REFERENCE}. Strings themselves are immutable in Java.
 */
public final class SubApp {

    /** Mutable wrapper that emulates pass-by-reference for a single value. */
    public static final class Holder<T> {
        private T value;

        public Holder(T initial) {
            this.value = initial;
        }

        public T get() {
            return value;
        }

        public void set(T value) {
            this.value = value;
        }
    }

    private String wsTestItem1 = "";
    private String wsTestItem2 = "";

    /**
     * Processes the linkage items, mirroring the COBOL procedure division.
     */
    public void run(Holder<String> lTestItem1, Holder<String> lTestItem2) {
        // LOCAL-STORAGE fields — reset on every call.
        String lsTestItem1 = "";
        String lsTestItem2 = "";

        System.out.println("In sub program: " + lTestItem1.get() + " " + lTestItem2.get());
        System.out.println();
        System.out.println("working-storage values at start:");
        System.out.println("ws-test-item-1: " + wsTestItem1);
        System.out.println("ws-test-item-2: " + wsTestItem2);
        System.out.println();
        System.out.println("local-storage values at start:");
        System.out.println("ls-test-item-1: " + lsTestItem1);
        System.out.println("ls-test-item-2: " + lsTestItem2);
        System.out.println();
        System.out.println("Moving linkage section values to ws and ls vars..");

        wsTestItem1 = lTestItem1.get();
        wsTestItem2 = lTestItem2.get();
        lsTestItem1 = lTestItem1.get();
        lsTestItem2 = lTestItem2.get();

        System.out.println("setting input variables to new value...");
        lTestItem1.set("replace1");
        lTestItem2.set("replace2");

        System.out.println();
        System.out.println("working-storage values at end:");
        System.out.println("ws-test-item-1: " + wsTestItem1);
        System.out.println("ws-test-item-2: " + wsTestItem2);
        System.out.println();
        System.out.println("local-storage values at end:");
        System.out.println("ls-test-item-1: " + lsTestItem1);
        System.out.println("ls-test-item-2: " + lsTestItem2);
        System.out.println();
        System.out.println("Exit sub program: " + lTestItem1.get() + " " + lTestItem2.get());
    }

    public static void main(String[] args) {
        // The sub program can also be executed on its own, matching the
        // COBOL convention of every program having a standalone entry point.
        SubApp sub = new SubApp();
        Holder<String> a = new Holder<>("stand-alone");
        Holder<String> b = new Holder<>("test-value");
        sub.run(a, b);
    }
}
