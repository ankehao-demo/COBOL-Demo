package com.coboldemo.datatypes;

import java.util.Scanner;

/**
 * Migrated from: numval_test/numval_test.cbl
 *
 * Demonstrates COBOL NUMVAL function which converts a PIC X (string)
 * value to a numeric value for arithmetic. In Java, this maps to
 * Double.parseDouble() or similar parsing.
 */
public class NumvalTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // PIC X(10) - accepts any string including numeric strings
        System.out.print("Enter first number: ");
        String xVal = scanner.nextLine().trim();

        // PIC 9(10) - strictly numeric
        System.out.print("Enter second number: ");
        String nineVal = scanner.nextLine().trim();

        try {
            // FUNCTION NUMVAL(ws-x-val) -> Double.parseDouble
            double numval = Double.parseDouble(xVal);
            double numericVal = Double.parseDouble(nineVal);

            // COMPUTE ws-total = FUNCTION NUMVAL(ws-x-val) + ws-9-val
            double total = numval + numericVal;

            System.out.println("Total: " + total);
        } catch (NumberFormatException e) {
            System.out.println("Error: invalid numeric input.");
        }

        scanner.close();
    }

    /**
     * Utility method equivalent to COBOL NUMVAL function.
     * Converts a string representation of a number to a double.
     */
    public static double numval(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(value.trim());
    }
}
