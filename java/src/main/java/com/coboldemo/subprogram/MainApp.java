package com.coboldemo.subprogram;

import java.util.Scanner;

/**
 * Java migration of sub_program/main_app.cbl (program-id: main-app).
 *
 * Demonstrates how a COBOL main program calls a sub-program using different
 * calling conventions:
 *
 * <ol>
 *   <li><b>CALL BY CONTENT</b> &mdash; the sub-program receives <em>copies</em>
 *       of the data items.  Any modifications inside the sub-program do
 *       <b>not</b> affect the caller's variables.</li>
 *   <li><b>CALL BY REFERENCE</b> (default) &mdash; the sub-program receives
 *       the actual data items.  Modifications inside the sub-program
 *       <b>are</b> visible to the caller.</li>
 *   <li><b>CANCEL</b> &mdash; resets the sub-program's WORKING-STORAGE to
 *       initial values, as if it had never been called before.</li>
 * </ol>
 *
 * The flow mirrors the COBOL source exactly:
 * <pre>
 *   1. Accept two values from the user
 *   2. Display main app state
 *   3. Call sub by CONTENT  (main state unchanged afterwards)
 *   4. Display main app state
 *   5. Call sub by REFERENCE (main state changed to "replace1"/"replace2")
 *   6. Display main app state
 *   7. CANCEL sub (reset working storage)
 *   8. Call sub by REFERENCE again (WS values should be reset)
 *   9. Display main app state
 * </pre>
 */
public class MainApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // ---------------------------------------------------------
        // WORKING-STORAGE SECTION
        // 01 ws-group-1.
        //     05 ws-item-1   pic x(10).
        //     05 ws-item-2   pic x(10).
        // In COBOL, ws-group-1 is the concatenation of ws-item-1
        // and ws-item-2.  We model the group display accordingly.
        // ---------------------------------------------------------
        String wsItem1;
        String wsItem2;

        // Accept values from the user (ACCEPT ws-item-1 / ws-item-2)
        System.out.print("Enter value for #1: ");
        wsItem1 = scanner.nextLine();

        System.out.print("Enter value for #2: ");
        wsItem2 = scanner.nextLine();

        // Create the sub-program instance (analogous to loading "sub-app")
        SubApp subApp = new SubApp();

        // Display initial main app state
        displayMessage(wsItem1, wsItem2);

        // ---------------------------------------------------------
        // CALL "sub-app" USING BY CONTENT ws-item-1
        //                       BY CONTENT ws-item-2
        //
        // BY CONTENT: the sub-program gets copies of the values.
        // The caller's ws-item-1 and ws-item-2 are NOT modified.
        // ---------------------------------------------------------
        System.out.println("Calling sub program by content:");
        subApp.executeByContent(wsItem1, wsItem2);

        // Main app state should be unchanged after a BY CONTENT call
        displayMessage(wsItem1, wsItem2);

        // ---------------------------------------------------------
        // Second call should retain WORKING-STORAGE values from the
        // first call (they persist across invocations).
        //
        // CALL "sub-app" USING ws-item-1 ws-item-2
        //
        // BY REFERENCE (default): the sub-program can modify the
        // caller's variables.  We pass a String[] so modifications
        // to the array elements propagate back.
        // ---------------------------------------------------------
        System.out.println("Second call of sub program should retain WS values.");
        System.out.println("Calling sub program by reference:");
        String[] refItems = new String[] { wsItem1, wsItem2 };
        subApp.execute(refItems);
        wsItem1 = refItems[0];
        wsItem2 = refItems[1];

        // Main app state should now reflect the sub-program's changes
        displayMessage(wsItem1, wsItem2);

        // ---------------------------------------------------------
        // CANCEL "sub-app"
        //
        // Resets the sub-program's WORKING-STORAGE to initial values
        // (empty strings for PIC X fields).  The next CALL will see
        // fresh WORKING-STORAGE.
        // ---------------------------------------------------------
        System.out.println("Cancelling sub program");
        subApp.reset();

        System.out.println("Calling sub program. WS values should be reset:");
        refItems = new String[] { wsItem1, wsItem2 };
        subApp.execute(refItems);
        wsItem1 = refItems[0];
        wsItem2 = refItems[1];

        // Display final main app state
        displayMessage(wsItem1, wsItem2);

        scanner.close();
    }

    /**
     * Mirrors the COBOL paragraph {@code display-message}.
     * <p>
     * Displays the concatenation of ws-item-1 and ws-item-2
     * (equivalent to displaying ws-group-1 in COBOL).
     *
     * @param wsItem1 current value of ws-item-1
     * @param wsItem2 current value of ws-item-2
     */
    private static void displayMessage(String wsItem1, String wsItem2) {
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println("Main app: " + wsItem1 + wsItem2);
    }
}
