package com.coboldemo.numval;

import java.util.Scanner;

/**
 * Migrated from numval_test/numval_test.cbl
 * Demonstrates NUMVAL function for converting strings to numbers.
 */
public class NumvalTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // PIC X(10) - alphanumeric input
        System.out.print("Enter first number: ");
        String xVal = scanner.nextLine();

        // PIC 9(10) - numeric input
        System.out.print("Enter second number: ");
        long nineVal = scanner.nextLong();

        // FUNCTION NUMVAL equivalent: parse string to double
        double total = Double.parseDouble(xVal.trim()) + nineVal;

        System.out.println("Total: " + total);

        scanner.close();
    }
}
