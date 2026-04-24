package com.coboldemo.modular;

/**
 * Migrated from: sub_program/sub.cbl
 *
 * Sub-program called by MainApp. Demonstrates COBOL concepts:
 * - WORKING-STORAGE: instance fields that persist between calls
 * - LOCAL-STORAGE: local variables that are fresh each call
 * - LINKAGE SECTION: method parameters
 * - Modifying reference parameters vs. content (copy) parameters
 */
public class SubApp {

    // WORKING-STORAGE: persists between calls until cancel (new instance)
    private String wsTestItem1 = "";
    private String wsTestItem2 = "";

    /**
     * Main procedure of the sub-program.
     *
     * @param items a 2-element array: [item1, item2].
     *              When called BY REFERENCE, modifications are visible to caller.
     *              When called BY CONTENT, caller passes copies.
     */
    public void execute(String[] items) {
        String lItem1 = items[0];
        String lItem2 = items[1];

        System.out.println("In sub program: " + lItem1 + " " + lItem2);
        System.out.println();
        System.out.println("working-storage values at start:");
        System.out.println("ws-test-item-1: " + wsTestItem1);
        System.out.println("ws-test-item-2: " + wsTestItem2);
        System.out.println();

        // LOCAL-STORAGE: fresh each call (these are local variables)
        String lsTestItem1 = "";
        String lsTestItem2 = "";
        System.out.println("local-storage values at start:");
        System.out.println("ls-test-item-1: " + lsTestItem1);
        System.out.println("ls-test-item-2: " + lsTestItem2);
        System.out.println();

        System.out.println("Moving linkage section values to ws and ls vars..");
        wsTestItem1 = lItem1;
        wsTestItem2 = lItem2;
        lsTestItem1 = lItem1;
        lsTestItem2 = lItem2;

        System.out.println("setting input variables to new value...");
        items[0] = padRight("replace1", 10);
        items[1] = padRight("replace2", 10);

        System.out.println();
        System.out.println("working-storage values at end:");
        System.out.println("ws-test-item-1: " + wsTestItem1);
        System.out.println("ws-test-item-2: " + wsTestItem2);
        System.out.println();
        System.out.println("local-storage values at end:");
        System.out.println("ls-test-item-1: " + lsTestItem1);
        System.out.println("ls-test-item-2: " + lsTestItem2);
        System.out.println();
        System.out.println("Exit sub program: " + items[0] + " " + items[1]);
    }

    private static String padRight(String s, int len) {
        if (s == null) s = "";
        if (s.length() >= len) return s.substring(0, len);
        return String.format("%-" + len + "s", s);
    }
}
