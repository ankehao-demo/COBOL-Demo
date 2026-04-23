package com.coboldemo.numval;

import java.util.Scanner;

/**
 * Java port of numval_test/numval_test.cbl.
 *
 * Uses {@link Double#parseDouble(String)} as the Java equivalent of COBOL's
 * intrinsic {@code FUNCTION NUMVAL}. The second input is kept as a string and
 * parsed identically; COBOL's PIC 9(10) required numeric-only input, which we
 * enforce with a try/catch.
 */
public final class NumvalTest {

    private NumvalTest() {
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first number: ");
        String xVal = scanner.hasNextLine() ? scanner.nextLine() : "0";

        System.out.print("Enter second number: ");
        String nineVal = scanner.hasNextLine() ? scanner.nextLine() : "0";

        double total = numval(xVal) + numval(nineVal);
        System.out.println("Total: " + total);
    }

    /** COBOL FUNCTION NUMVAL: parses a decimal from a whitespace-padded string. */
    private static double numval(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException ex) {
            return 0.0;
        }
    }
}
