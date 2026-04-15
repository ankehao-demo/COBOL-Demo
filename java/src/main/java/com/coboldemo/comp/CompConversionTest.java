package com.coboldemo.comp;

import java.util.Scanner;

/**
 * Java equivalent of comp_test/comp_test.cbl
 *
 * Demonstrates COBOL COMP (binary) to DISPLAY (character) value conversions.
 * In Java, all integer arithmetic is binary internally; formatting with
 * String.format controls how values are displayed.
 *
 * COBOL Mapping:
 *   PIC 999 COMP    → int (binary storage)
 *   PIC 999         → int displayed as String.format("%03d", val) (zero-padded)
 *   PIC ZZ9         → int displayed as String.format("%3d", val) (zero-suppressed)
 */
public class CompConversionTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // MOVE 12 TO ws-comp-val; MULTIPLY ws-comp-val BY 2
        int compVal = 12;
        compVal = compVal * 2;
        System.out.println("COMP: " + compVal);

        // MOVE ws-comp-val TO ws-disp-val (PIC 999 → zero-padded display)
        String dispVal = String.format("%03d", compVal);
        System.out.println("DISP:" + dispVal);

        // MOVE ws-comp-val TO ws-dyn-disp-val (PIC ZZ9 → zero-suppressed)
        String dynDispVal = String.format("%3d", compVal);
        System.out.println("DYNA:" + dynDispVal);

        // ACCEPT ws-input (PIC 999)
        System.out.print("INPUT: ");
        String input = scanner.nextLine().trim();
        System.out.println("INPUT: " + String.format("%03d", Integer.parseInt(input)));

        // MOVE ws-input TO ws-comp-val
        compVal = Integer.parseInt(input);
        System.out.println("COMP: " + compVal);

        scanner.close();
    }
}
