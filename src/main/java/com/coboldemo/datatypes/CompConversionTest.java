package com.coboldemo.datatypes;

import java.util.Scanner;

/**
 * Migrated from: comp_test/comp_test.cbl
 *
 * Demonstrates COBOL COMP (binary) to DISPLAY conversion.
 * In Java, all integer types are binary, so this shows arithmetic
 * operations and formatted display of numeric values.
 */
public class CompConversionTest {

    public static void main(String[] args) {
        // PIC 999 COMP -> Java int (binary storage)
        int compVal = 12;
        compVal = compVal * 2;
        System.out.println("COMP: " + compVal);

        // Move to display: PIC 999 -> zero-padded 3-digit string
        String dispVal = String.format("%03d", compVal);
        System.out.println("DISP:" + dispVal);

        // Dynamic display: PIC zz9 -> leading spaces instead of zeros
        String dynDispVal = String.format("%3d", compVal);
        System.out.println("DYNA:" + dynDispVal);

        // Accept input
        Scanner scanner = new Scanner(System.in);
        System.out.print("INPUT: ");
        String inputStr = scanner.nextLine().trim();
        int input = 0;
        try {
            input = Integer.parseInt(inputStr);
            if (input > 999) input = 999;
            if (input < 0) input = 0;
        } catch (NumberFormatException e) {
            input = 0;
        }

        System.out.println("INPUT: " + String.format("%03d", input));

        // Move input to comp value
        compVal = input;
        System.out.println("COMP: " + compVal);

        scanner.close();
    }
}
