package com.coboldemo.subprogram;

/**
 * Migrated from sub_program/sub.cbl
 * Sub-program demonstrating BY REFERENCE and BY CONTENT call conventions.
 * Working-storage fields persist between calls until reset() is called.
 */
public class SubApp {

    // Working-storage: persists between calls (until CANCEL/reset)
    private String wsTestItem1 = "";
    private String wsTestItem2 = "";

    /**
     * Execute by reference: modifies the caller's array elements.
     * COBOL equivalent: CALL "sub-app" USING ws-item-1 ws-item-2
     */
    public void execute(String[] items) {
        // Local-storage: fresh on each call
        String lsTestItem1 = "";
        String lsTestItem2 = "";

        System.out.println("In sub program: " + items[0] + " " + items[1]);
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
        wsTestItem1 = items[0];
        wsTestItem2 = items[1];
        lsTestItem1 = items[0];
        lsTestItem2 = items[1];

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

    /**
     * Execute by content: does NOT modify the caller's variables.
     * COBOL equivalent: CALL "sub-app" USING BY CONTENT ws-item-1 BY CONTENT ws-item-2
     */
    public void executeByContent(String item1, String item2) {
        // Local-storage: fresh on each call
        String lsTestItem1 = "";
        String lsTestItem2 = "";

        System.out.println("In sub program: " + item1 + " " + item2);
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
        wsTestItem1 = item1;
        wsTestItem2 = item2;
        lsTestItem1 = item1;
        lsTestItem2 = item2;

        System.out.println("setting input variables to new value...");
        // By content: modifications are local, don't affect caller
        item1 = padRight("replace1", 10);
        item2 = padRight("replace2", 10);

        System.out.println();
        System.out.println("working-storage values at end:");
        System.out.println("ws-test-item-1: " + wsTestItem1);
        System.out.println("ws-test-item-2: " + wsTestItem2);
        System.out.println();
        System.out.println("local-storage values at end:");
        System.out.println("ls-test-item-1: " + lsTestItem1);
        System.out.println("ls-test-item-2: " + lsTestItem2);
        System.out.println();
        System.out.println("Exit sub program: " + item1 + " " + item2);
    }

    /**
     * Reset working-storage fields. Simulates CANCEL "sub-app" in COBOL.
     */
    public void reset() {
        wsTestItem1 = "";
        wsTestItem2 = "";
    }

    private static String padRight(String s, int width) {
        if (s.length() >= width) return s.substring(0, width);
        return String.format("%-" + width + "s", s);
    }
}
