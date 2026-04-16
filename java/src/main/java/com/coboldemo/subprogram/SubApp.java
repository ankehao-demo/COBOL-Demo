package com.coboldemo.subprogram;

/**
 * Migrated from: sub_program/sub.cbl
 * Original author: Erik Eriksen (2022-05-09, updated 2022-06-13)
 * Purpose: Subprogram demonstrating working-storage persistence and linkage section.
 *
 * Key concepts:
 * - WORKING-STORAGE variables persist between calls (instance fields)
 * - LOCAL-STORAGE variables are fresh on each call (local variables in method)
 * - LINKAGE SECTION parameters are passed from caller (method parameters)
 * - CANCEL resets WORKING-STORAGE (reset() method)
 */
public class SubApp {

    // WORKING-STORAGE: persists between calls
    private int wsCallCount = 0;
    private String wsLastItem1 = "";
    private String wsLastItem2 = "";

    /**
     * Execute with BY REFERENCE semantics.
     * Modifies the items array directly (caller sees changes).
     *
     * @param items array of two items, modified in place
     */
    public void execute(String[] items) {
        // LOCAL-STORAGE equivalent: fresh each call
        int localCounter = 0;

        wsCallCount++;
        localCounter++;

        System.out.println("  Sub-program called (call #" + wsCallCount + ")");
        System.out.println("  Local counter: " + localCounter + " (always 1 - fresh each call)");
        System.out.println("  Received item-1: '" + items[0] + "'");
        System.out.println("  Received item-2: '" + items[1] + "'");

        // Show previous values (working-storage persistence)
        if (wsCallCount > 1) {
            System.out.println("  Previous item-1: '" + wsLastItem1 + "' (from working-storage)");
            System.out.println("  Previous item-2: '" + wsLastItem2 + "' (from working-storage)");
        }

        // Save current values to working-storage
        wsLastItem1 = items[0];
        wsLastItem2 = items[1];

        // Modify items (BY REFERENCE - caller sees changes)
        items[0] = items[0].toUpperCase();
        items[1] = items[1].toUpperCase();

        System.out.println("  Modified item-1: '" + items[0] + "' (uppercased)");
        System.out.println("  Modified item-2: '" + items[1] + "' (uppercased)");
    }

    /**
     * Execute with BY CONTENT semantics.
     * Does not modify the caller's variables (works on copies).
     *
     * @param item1 first item (copy, not modified in caller)
     * @param item2 second item (copy, not modified in caller)
     */
    public void executeByContent(String item1, String item2) {
        // LOCAL-STORAGE equivalent: fresh each call
        int localCounter = 0;

        wsCallCount++;
        localCounter++;

        System.out.println("  Sub-program called BY CONTENT (call #" + wsCallCount + ")");
        System.out.println("  Local counter: " + localCounter + " (always 1 - fresh each call)");
        System.out.println("  Received item-1: '" + item1 + "'");
        System.out.println("  Received item-2: '" + item2 + "'");

        // Save current values to working-storage (same as BY REFERENCE path)
        wsLastItem1 = item1;
        wsLastItem2 = item2;

        // Modify local copies (caller does NOT see these changes)
        item1 = item1.toUpperCase();
        item2 = item2.toUpperCase();

        System.out.println("  Modified item-1: '" + item1 + "' (uppercased, but caller won't see)");
        System.out.println("  Modified item-2: '" + item2 + "' (uppercased, but caller won't see)");
    }

    /**
     * Reset working-storage to simulate CANCEL.
     * After CANCEL, the next call will have fresh working-storage.
     */
    public void reset() {
        wsCallCount = 0;
        wsLastItem1 = "";
        wsLastItem2 = "";
        System.out.println("  Sub-program CANCELLED (working-storage reset)");
    }
}
