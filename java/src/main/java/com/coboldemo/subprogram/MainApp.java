package com.coboldemo.subprogram;

/**
 * Migrated from: sub_program/main_app.cbl
 * Original author: Erik Eriksen (2022-05-09, updated 2022-06-13)
 * Purpose: Main program demonstrating CALL with BY CONTENT and BY REFERENCE.
 *
 * Demonstrates:
 * 1. CALL BY CONTENT: sub-program receives copies, caller's values unchanged
 * 2. CALL BY REFERENCE (default): sub-program modifies caller's variables
 * 3. CANCEL: resets sub-program's working-storage
 */
public class MainApp {

    public static void main(String[] args) {
        SubApp subApp = new SubApp();

        System.out.println("Sub-program Demo");
        System.out.println("================");
        System.out.println();

        // --- Call 1: BY CONTENT ---
        System.out.println("Call 1: CALL BY CONTENT");
        System.out.println("-----------------------");
        String item1 = "hello";
        String item2 = "world";
        System.out.println("Before call - item-1: '" + item1 + "', item-2: '" + item2 + "'");

        subApp.executeByContent(item1, item2);

        // Values should be unchanged (BY CONTENT)
        System.out.println("After call  - item-1: '" + item1 + "', item-2: '" + item2 + "'");
        System.out.println("(Values unchanged - BY CONTENT passes copies)");
        System.out.println();

        // --- Call 2: BY REFERENCE (default) ---
        System.out.println("Call 2: CALL BY REFERENCE (default)");
        System.out.println("-----------------------------------");
        String[] items = {"goodbye", "earth"};
        System.out.println("Before call - item-1: '" + items[0] + "', item-2: '" + items[1] + "'");

        subApp.execute(items);

        // Values should be modified (BY REFERENCE)
        System.out.println("After call  - item-1: '" + items[0] + "', item-2: '" + items[1] + "'");
        System.out.println("(Values changed - BY REFERENCE modifies caller's data)");
        System.out.println();

        // --- CANCEL and Call 3 ---
        System.out.println("CANCEL sub-program");
        System.out.println("------------------");
        subApp.reset();
        System.out.println();

        System.out.println("Call 3: After CANCEL (working-storage reset)");
        System.out.println("--------------------------------------------");
        String[] items2 = {"new", "data"};
        System.out.println("Before call - item-1: '" + items2[0] + "', item-2: '" + items2[1] + "'");

        subApp.execute(items2);

        System.out.println("After call  - item-1: '" + items2[0] + "', item-2: '" + items2[1] + "'");
        System.out.println("(Call count reset to 1 after CANCEL)");
    }
}
