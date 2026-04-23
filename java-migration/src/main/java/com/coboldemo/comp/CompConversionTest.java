package com.coboldemo.comp;

import java.util.Scanner;

/**
 * Java port of comp_test/comp_test.cbl.
 *
 * COBOL's {@code PIC 999 COMP} maps cleanly to a Java {@code int}. The
 * interesting part of the original program is the formatting of that integer
 * for display, which this class reproduces using {@link String#format}.
 */
public final class CompConversionTest {

    private CompConversionTest() {
    }

    public static void main(String[] args) {
        int compVal = 12;
        compVal *= 2;
        System.out.println("COMP: " + compVal);

        // PIC 999 display - zero-padded, three digits.
        String dispVal = String.format("%03d", compVal);
        System.out.println("DISP:" + dispVal);

        // PIC ZZ9 display - leading spaces, three characters wide.
        String dynDispVal = String.format("%3d", compVal);
        System.out.println("DYNA:" + dynDispVal);

        System.out.print("INPUT: ");
        Scanner scanner = new Scanner(System.in);
        String raw = scanner.hasNextLine() ? scanner.nextLine() : "0";
        int input;
        try {
            input = Integer.parseInt(raw.trim());
        } catch (NumberFormatException ex) {
            input = 0;
        }

        System.out.println("INPUT: " + String.format("%03d", input));
        compVal = input;
        System.out.println("COMP: " + compVal);
    }
}
