package com.coboldemo.subprogram;

/**
 * Java port of {@code sub_program/sub.cbl}.
 *
 * The COBOL sub-program has WORKING-STORAGE values that persist across
 * calls until the caller issues a CANCEL, plus LOCAL-STORAGE values which
 * are reset on every call. We model both:
 * <ul>
 *   <li>{@code wsTestItem1/2} are instance fields that persist for the
 *       lifetime of the {@code SubApp} instance. The caller simulates
 *       CANCEL by allocating a fresh instance.</li>
 *   <li>Local-storage values are method-local variables in
 *       {@link #execute(String[], String[])}.</li>
 * </ul>
 *
 * The two parameters are {@code String[]} of length 1 so the sub-program
 * can mutate them, mirroring COBOL CALL ... BY REFERENCE. To simulate
 * CALL ... BY CONTENT, the caller passes copies and discards any mutation.
 */
public class SubApp {

    private String wsTestItem1 = "";
    private String wsTestItem2 = "";

    public void execute(String[] item1Holder, String[] item2Holder) {
        String lTestItem1 = item1Holder[0];
        String lTestItem2 = item2Holder[0];

        // local-storage equivalents - reset on every call.
        String lsTestItem1 = "";
        String lsTestItem2 = "";

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
        item1Holder[0] = "replace1";
        item2Holder[0] = "replace2";

        System.out.println();
        System.out.println("working-storage values at end:");
        System.out.println("ws-test-item-1: " + wsTestItem1);
        System.out.println("ws-test-item-2: " + wsTestItem2);
        System.out.println();
        System.out.println("local-storage values at end:");
        System.out.println("ls-test-item-1: " + lsTestItem1);
        System.out.println("ls-test-item-2: " + lsTestItem2);
        System.out.println();
        System.out.println("Exit sub program: " + item1Holder[0] + " " + item2Holder[0]);
    }
}
