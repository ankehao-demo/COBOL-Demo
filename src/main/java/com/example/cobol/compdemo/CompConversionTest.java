package com.example.cobol.compdemo;

import java.util.Scanner;

/**
 * Java port of {@code comp_test/comp_test.cbl}.
 *
 * <p>The original program plays with COMP (binary) and DISPLAY (zoned
 * decimal) representations of a 3-digit number. Java doesn't differentiate
 * between binary and zoned-decimal storage at the language level — every
 * {@code int} is binary — so the equivalent demo simply formats the same
 * value in different display widths via {@link String#format(String, Object...)}.
 */
public final class CompConversionTest {

    private CompConversionTest() {}

    public static void main(String[] args) {
        int wsCompVal = 12;
        wsCompVal *= 2;
        System.out.println("COMP: " + wsCompVal);

        // PIC 999 — three-digit zero-padded display.
        System.out.println("DISP:" + String.format("%03d", wsCompVal));

        // PIC zz9 — leading-zero suppression.
        System.out.println("DYNA:" + String.format("%3d", wsCompVal));

        System.out.print("INPUT: ");
        try (Scanner in = new Scanner(System.in)) {
            int wsInput = 0;
            if (in.hasNextLine()) {
                String line = in.nextLine().trim();
                try {
                    wsInput = Integer.parseInt(line);
                } catch (NumberFormatException ignored) {
                    // leave as 0
                }
            }
            System.out.println("INPUT: " + String.format("%03d", wsInput));
            System.out.println("COMP: " + wsInput);
        }
    }
}
