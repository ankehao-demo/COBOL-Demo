package com.coboldemo.comp;

import java.util.Scanner;

/**
 * Java port of {@code comp_test/comp_test.cbl}.
 *
 * COBOL distinguishes between binary (COMP) values and zoned-decimal
 * display values. In Java both end up as {@code int}; we use
 * {@link String#format} to reproduce the COBOL display formats:
 * {@code PIC 999} -> "%03d" and {@code PIC ZZ9} -> "%3d".
 */
public class CompConversionTest {

    public static void main(String[] args) {
        int wsCompVal = 12;
        wsCompVal = wsCompVal * 2;
        System.out.println("COMP: " + String.format("%03d", wsCompVal));

        int wsDispVal = wsCompVal;
        System.out.println("DISP:" + String.format("%03d", wsDispVal));

        int wsDynDispVal = wsCompVal;
        System.out.println("DYNA:" + String.format("%3d", wsDynDispVal));

        System.out.print("INPUT: ");
        Scanner scanner = new Scanner(System.in);
        int wsInput = 0;
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            try {
                wsInput = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                // PIC 999 silently truncates non-numeric input; default 0.
                wsInput = 0;
            }
        }
        System.out.println("INPUT: " + String.format("%03d", wsInput));

        wsCompVal = wsInput;
        System.out.println("COMP: " + String.format("%03d", wsCompVal));
    }
}
