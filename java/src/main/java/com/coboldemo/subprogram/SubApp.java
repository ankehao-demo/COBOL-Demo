package com.coboldemo.subprogram;

/**
 * Java migration of sub_program/sub.cbl (program-id: sub-app).
 *
 * Demonstrates COBOL sub-program concepts:
 *
 * <ul>
 *   <li><b>WORKING-STORAGE</b> fields persist across calls to {@link #execute}
 *       and {@link #executeByContent} until {@link #reset()} is called.</li>
 *   <li><b>LOCAL-STORAGE</b> fields are reset to empty strings at the start of
 *       every call (COBOL allocates them fresh on each invocation).</li>
 *   <li><b>CALL BY REFERENCE</b> ({@link #execute(String[])}) &mdash; the caller
 *       passes a mutable array; the sub-program can modify the elements and
 *       the changes are visible to the caller.</li>
 *   <li><b>CALL BY CONTENT</b> ({@link #executeByContent(String, String)}) &mdash;
 *       the caller passes copies of the values; the sub-program cannot alter
 *       the caller's original variables.</li>
 *   <li><b>CANCEL</b> ({@link #reset()}) &mdash; resets WORKING-STORAGE fields
 *       to their initial (empty) state, as if the sub-program had never been
 *       loaded.</li>
 * </ul>
 */
public class SubApp {

    // ---------------------------------------------------------------
    // WORKING-STORAGE SECTION
    // These fields persist between calls (until CANCEL / reset).
    // Equivalent to:
    //   01  ws-test-item-1   pic x(10).
    //   01  ws-test-item-2   pic x(10).
    // ---------------------------------------------------------------
    private String wsTestItem1 = "";
    private String wsTestItem2 = "";

    // ---------------------------------------------------------------
    // LOCAL-STORAGE SECTION
    // In COBOL these are allocated fresh (initialised to spaces) on
    // every CALL.  We simulate this by resetting them at the start of
    // each execute / executeByContent invocation.
    // Equivalent to:
    //   01  ls-test-item-1   pic x(10).
    //   01  ls-test-item-2   pic x(10).
    // ---------------------------------------------------------------
    private String lsTestItem1 = "";
    private String lsTestItem2 = "";

    /**
     * Execute the sub-program with <b>BY REFERENCE</b> semantics.
     * <p>
     * In COBOL, {@code CALL "sub-app" USING l-test-item-1 l-test-item-2}
     * passes the linkage-section items by reference.  The sub-program can
     * modify them and the caller sees the changes.
     * <p>
     * In Java we model this by accepting a {@code String[]} whose elements
     * the method may replace.
     *
     * @param items a two-element array; items[0] and items[1] correspond to
     *              l-test-item-1 and l-test-item-2 in the COBOL linkage section.
     */
    public void execute(String[] items) {
        // LOCAL-STORAGE is reset on every call
        lsTestItem1 = "";
        lsTestItem2 = "";

        String lTestItem1 = items[0];
        String lTestItem2 = items[1];

        System.out.println("In sub program: " + lTestItem1 + " " + lTestItem2);
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
        wsTestItem1 = lTestItem1;
        wsTestItem2 = lTestItem2;
        lsTestItem1 = lTestItem1;
        lsTestItem2 = lTestItem2;

        System.out.println("setting input variables to new value...");
        // BY REFERENCE: modifications are visible to the caller
        lTestItem1 = "replace1";
        lTestItem2 = "replace2";
        items[0] = lTestItem1;
        items[1] = lTestItem2;

        System.out.println();
        System.out.println("working-storage values at end:");
        System.out.println("ws-test-item-1: " + wsTestItem1);
        System.out.println("ws-test-item-2: " + wsTestItem2);
        System.out.println();
        System.out.println("local-storage values at end:");
        System.out.println("ls-test-item-1: " + lsTestItem1);
        System.out.println("ls-test-item-2: " + lsTestItem2);
        System.out.println();
        System.out.println("Exit sub program: " + lTestItem1 + " " + lTestItem2);
    }

    /**
     * Execute the sub-program with <b>BY CONTENT</b> semantics.
     * <p>
     * In COBOL, {@code CALL "sub-app" USING BY CONTENT ws-item-1
     * BY CONTENT ws-item-2} passes <em>copies</em> of the data items.
     * The sub-program works on copies, so any modifications it makes
     * do <b>not</b> propagate back to the caller's variables.
     * <p>
     * In Java we model this by accepting plain {@code String} parameters
     * (immutable, pass-by-value of the reference &mdash; the caller's
     * variables are never touched).
     *
     * @param item1 copy of the first data item (l-test-item-1)
     * @param item2 copy of the second data item (l-test-item-2)
     */
    public void executeByContent(String item1, String item2) {
        // LOCAL-STORAGE is reset on every call
        lsTestItem1 = "";
        lsTestItem2 = "";

        String lTestItem1 = item1;
        String lTestItem2 = item2;

        System.out.println("In sub program: " + lTestItem1 + " " + lTestItem2);
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
        wsTestItem1 = lTestItem1;
        wsTestItem2 = lTestItem2;
        lsTestItem1 = lTestItem1;
        lsTestItem2 = lTestItem2;

        System.out.println("setting input variables to new value...");
        // BY CONTENT: these local reassignments do NOT affect the caller
        lTestItem1 = "replace1";
        lTestItem2 = "replace2";

        System.out.println();
        System.out.println("working-storage values at end:");
        System.out.println("ws-test-item-1: " + wsTestItem1);
        System.out.println("ws-test-item-2: " + wsTestItem2);
        System.out.println();
        System.out.println("local-storage values at end:");
        System.out.println("ls-test-item-1: " + lsTestItem1);
        System.out.println("ls-test-item-2: " + lsTestItem2);
        System.out.println();
        System.out.println("Exit sub program: " + lTestItem1 + " " + lTestItem2);
    }

    /**
     * Simulate the COBOL {@code CANCEL "sub-app"} statement.
     * <p>
     * In COBOL, CANCEL unloads the sub-program from memory, so the next
     * CALL re-initialises WORKING-STORAGE to its initial values (spaces
     * for PIC X fields).  LOCAL-STORAGE is always reset on each call
     * regardless of CANCEL.
     */
    public void reset() {
        wsTestItem1 = "";
        wsTestItem2 = "";
    }
}
