package com.example.cobol.subprogram;

/**
 * Java port of {@code sub_program/sub.cbl}.
 *
 * <p>The COBOL program demonstrates {@code WORKING-STORAGE} (persistent
 * across calls until {@code CANCEL}) vs {@code LOCAL-STORAGE} (fresh on
 * every call). We model this with a class instance that has both a static
 * "working storage" field and a method-local "local storage" variable.
 *
 * <p>"Cancelling" the sub-program in COBOL resets WORKING-STORAGE to its
 * initial value. The Java equivalent is creating a brand-new {@link Sub}
 * instance, which is what {@link MainApp} does when it wants to "cancel".
 */
public final class Sub {

    // ws-test-item-1 / ws-test-item-2 — persistent across calls within
    // the same instance.
    private String wsTestItem1 = "";
    private String wsTestItem2 = "";

    /**
     * Mirrors the original COBOL "by reference" call: the sub-program may
     * modify the linkage section, so we use a mutable holder for the
     * "linkage" parameters. After the call returns, the holders' values
     * have been updated.
     */
    public void call(MutableString lTestItem1, MutableString lTestItem2) {
        // ls-test-item-1 / ls-test-item-2 — fresh local storage.
        String lsTestItem1 = "";
        String lsTestItem2 = "";

        System.out.println("In sub program: " + lTestItem1.value + " " + lTestItem2.value);
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

        wsTestItem1 = lTestItem1.value;
        wsTestItem2 = lTestItem2.value;
        lsTestItem1 = lTestItem1.value;
        lsTestItem2 = lTestItem2.value;

        System.out.println("setting input variables to new value...");
        lTestItem1.value = "replace1";
        lTestItem2.value = "replace2";

        System.out.println();
        System.out.println("working-storage values at end:");
        System.out.println("ws-test-item-1: " + wsTestItem1);
        System.out.println("ws-test-item-2: " + wsTestItem2);
        System.out.println();
        System.out.println("local-storage values at end:");
        System.out.println("ls-test-item-1: " + lsTestItem1);
        System.out.println("ls-test-item-2: " + lsTestItem2);
        System.out.println();
        System.out.println("Exit sub program: " + lTestItem1.value + " " + lTestItem2.value);
    }

    /** Mutable string holder used for "by reference" parameter passing. */
    public static final class MutableString {
        public String value;

        public MutableString(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
