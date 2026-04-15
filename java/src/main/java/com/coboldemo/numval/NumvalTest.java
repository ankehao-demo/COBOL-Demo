package com.coboldemo.numval;

import java.util.Scanner;

/**
 * Java equivalent of numval_test/numval_test.cbl
 *
 * Demonstrates the COBOL FUNCTION NUMVAL() which converts a PIC X
 * (alphanumeric string) to a numeric value for arithmetic. In Java,
 * Double.parseDouble() serves the same purpose.
 *
 * COBOL Mapping:
 *   PIC X(10)              → String
 *   PIC 9(10)              → long (or int)
 *   COMP-2                 → double
 *   FUNCTION NUMVAL(ws-x)  → Double.parseDouble(input.trim())
 */
public class NumvalTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // ACCEPT ws-x-val (PIC X(10) - alphanumeric)
        System.out.print("Enter first number: ");
        String xVal = scanner.nextLine();

        // ACCEPT ws-9-val (PIC 9(10) - numeric)
        System.out.print("Enter second number: ");
        String nineVal = scanner.nextLine();

        // COMPUTE ws-total = FUNCTION NUMVAL(ws-x-val) + ws-9-val
        double total = Double.parseDouble(xVal.trim()) + Long.parseLong(nineVal.trim());

        // DISPLAY "Total: " ws-total
        System.out.println("Total: " + total);

        scanner.close();
    }
}
