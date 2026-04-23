package com.coboldemo.subprogram;

import com.coboldemo.subprogram.MainApp.Ref;

/**
 * Java port of sub_program/sub.cbl.
 *
 * Instance fields stand in for COBOL WORKING-STORAGE (persist across calls
 * against the same instance, reset when the instance is replaced).
 * Method-local variables stand in for LOCAL-STORAGE (reinitialised on each
 * invocation).
 */
public final class SubApp {

    // WORKING-STORAGE SECTION: persists across calls to the same instance.
    private String wsTestItem1 = "";
    private String wsTestItem2 = "";

    /** Call-by-content version: mutations are not visible to the caller. */
    public void call(String lTestItem1, String lTestItem2) {
        String local1 = lTestItem1;
        String local2 = lTestItem2;
        emitDiagnostics(local1, local2);
        // Update working-storage (persists).
        wsTestItem1 = local1;
        wsTestItem2 = local2;
    }

    /** Call-by-reference version: the callee mutates the holders in place. */
    public void callByReference(Ref<String> item1, Ref<String> item2) {
        String local1 = item1.get();
        String local2 = item2.get();

        System.out.println("In sub program: " + local1 + " " + local2);
        System.out.println();
        System.out.println("working-storage values at start:");
        System.out.println("ws-test-item-1: " + wsTestItem1);
        System.out.println("ws-test-item-2: " + wsTestItem2);
        System.out.println();
        System.out.println("local-storage values at start:");
        System.out.println("ls-test-item-1: " + "");
        System.out.println("ls-test-item-2: " + "");
        System.out.println();
        System.out.println("Moving linkage section values to ws and ls vars..");

        wsTestItem1 = local1;
        wsTestItem2 = local2;

        System.out.println("setting input variables to new value...");
        // Simulate "move 'replaceX' to l-test-itemX" with ref holder.
        item1.set("replace1");
        item2.set("replace2");

        System.out.println();
        System.out.println("working-storage values at end:");
        System.out.println("ws-test-item-1: " + wsTestItem1);
        System.out.println("ws-test-item-2: " + wsTestItem2);
        System.out.println();
        System.out.println("local-storage values at end:");
        System.out.println("ls-test-item-1: " + local1);
        System.out.println("ls-test-item-2: " + local2);
        System.out.println();
        System.out.println("Exit sub program: " + item1.get() + " "
                + item2.get());
    }

    private void emitDiagnostics(String local1, String local2) {
        System.out.println("In sub program: " + local1 + " " + local2);
        System.out.println();
        System.out.println("working-storage values at start:");
        System.out.println("ws-test-item-1: " + wsTestItem1);
        System.out.println("ws-test-item-2: " + wsTestItem2);
        System.out.println();
        System.out.println("Exit sub program: " + local1 + " " + local2);
    }
}
