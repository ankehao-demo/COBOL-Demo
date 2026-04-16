package com.coboldemo.comp;

/**
 * Migrated from: comp_test/comp_test.cbl
 * Original author: Erik Eriksen (2021-09-03)
 * Purpose: Testing the COMP type and its conversion/display properties.
 *
 * Notes:
 * - COBOL COMP (binary) maps to Java int.
 * - COBOL's PIC 999 COMP holds values 0-999 in binary storage.
 * - Java int is always 32-bit signed, so no storage difference to demonstrate,
 *   but we replicate the arithmetic and display formatting behavior.
 */
public class CompConversionTest {

    public static void main(String[] args) {
        // PIC 999 COMP: binary storage for 0-999
        int wsCompTest = 0;

        System.out.println("Initial value: " + wsCompTest);

        // MOVE 1 to ws-comp-test
        wsCompTest = 1;
        System.out.println("After MOVE 1: " + wsCompTest);

        // ADD 1 to ws-comp-test
        wsCompTest += 1;
        System.out.println("After ADD 1: " + wsCompTest);

        // MULTIPLY ws-comp-test BY 5
        wsCompTest *= 5;
        System.out.println("After MULTIPLY BY 5: " + wsCompTest);

        // Display with different formatting (PIC clause equivalents)
        System.out.println();
        System.out.println("Display with different formats:");

        // PIC 999 display: zero-padded 3 digits
        System.out.printf("PIC 999:       %03d%n", wsCompTest);

        // PIC 9(5) display: zero-padded 5 digits
        System.out.printf("PIC 9(5):      %05d%n", wsCompTest);

        // PIC ZZ9 display: space-padded, at least 1 digit
        System.out.printf("PIC ZZ9:       %3d%n", wsCompTest);

        // PIC Z(4)9 display: space-padded, at least 1 digit, 5 wide
        System.out.printf("PIC Z(4)9:     %5d%n", wsCompTest);
    }
}
