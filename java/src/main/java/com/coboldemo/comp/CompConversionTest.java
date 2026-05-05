package com.coboldemo.comp;

import java.util.Scanner;

/**
 * Migrated from comp_test/comp_test.cbl
 * Demonstrates COMP (binary) data type conversions.
 */
public class CompConversionTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // COMP value: binary integer
        int compVal = 12;
        compVal = compVal * 2;
        System.out.println("COMP: " + compVal);

        // Display as fixed-width (PIC 999)
        String dispVal = String.format("%03d", compVal);
        System.out.println("DISP:" + dispVal);

        // Dynamic display with leading zero suppression (PIC ZZ9)
        String dynDispVal = String.format("%3d", compVal);
        System.out.println("DYNA:" + dynDispVal);

        // Accept user input
        System.out.print("INPUT: ");
        String input = scanner.nextLine();
        String inputFormatted = String.format("%3s", input).substring(0, 3);
        System.out.println("INPUT: " + inputFormatted);

        // Move input to COMP
        try {
            compVal = Integer.parseInt(input.trim());
            System.out.println("COMP: " + compVal);
        } catch (NumberFormatException e) {
            System.out.println("COMP: Invalid numeric input");
        }

        scanner.close();
    }
}
