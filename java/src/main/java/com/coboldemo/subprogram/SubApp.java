package com.coboldemo.subprogram;

/**
 * Java equivalent of sub_program/sub.cbl
 *
 * Represents the COBOL sub-program called by main_app.cbl. Demonstrates the
 * difference between WORKING-STORAGE (instance fields that persist between
 * calls) and LOCAL-STORAGE (local variables that are fresh each call).
 *
 * COBOL Mapping:
 *   WORKING-STORAGE variables → instance fields (persist until re-instantiation)
 *   LOCAL-STORAGE variables   → local variables in execute() (fresh each call)
 *   LINKAGE SECTION           → method parameters
 *   BY CONTENT                → pass copies (caller's values unchanged)
 *   BY REFERENCE (default)    → pass mutable wrappers (caller sees changes)
 *   CANCEL "sub-app"          → re-instantiate (new SubApp()) to reset state
 */
public class SubApp {

    // WORKING-STORAGE: persists across calls until CANCEL (re-instantiation)
    private String wsTestItem1 = "";
    private String wsTestItem2 = "";

    /**
     * Execute the sub-program logic.
     *
     * @param item1 mutable wrapper for linkage item 1
     * @param item2 mutable wrapper for linkage item 2
     */
    public void execute(StringBuilder item1, StringBuilder item2) {
        // LOCAL-STORAGE: fresh each call
        String lsTestItem1 = "";
        String lsTestItem2 = "";

        System.out.println("In sub program: " + item1 + " " + item2);
        System.out.println();

        System.out.println("working-storage values at start:");
        System.out.println("ws-test-item-1: " + pad(wsTestItem1, 10));
        System.out.println("ws-test-item-2: " + pad(wsTestItem2, 10));
        System.out.println();

        System.out.println("local-storage values at start:");
        System.out.println("ls-test-item-1: " + pad(lsTestItem1, 10));
        System.out.println("ls-test-item-2: " + pad(lsTestItem2, 10));
        System.out.println();

        System.out.println("Moving linkage section values to ws and ls vars..");

        // MOVE l-test-item-1 TO ws-test-item-1 (and ls equivalents)
        wsTestItem1 = item1.toString();
        wsTestItem2 = item2.toString();
        lsTestItem1 = item1.toString();
        lsTestItem2 = item2.toString();

        // Setting input variables to new value (modifies BY REFERENCE params)
        System.out.println("setting input variables to new value...");
        item1.replace(0, item1.length(), pad("replace1", 10));
        item2.replace(0, item2.length(), pad("replace2", 10));

        System.out.println();
        System.out.println("working-storage values at end:");
        System.out.println("ws-test-item-1: " + pad(wsTestItem1, 10));
        System.out.println("ws-test-item-2: " + pad(wsTestItem2, 10));
        System.out.println();

        System.out.println("local-storage values at end:");
        System.out.println("ls-test-item-1: " + pad(lsTestItem1, 10));
        System.out.println("ls-test-item-2: " + pad(lsTestItem2, 10));
        System.out.println();

        System.out.println("Exit sub program: " + item1 + " " + item2);
    }

    private static String pad(String s, int len) {
        if (s == null) s = "";
        return String.format("%-" + len + "s", s).substring(0, len);
    }
}
