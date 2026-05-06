package com.example.cobol.numval;

import java.util.Scanner;

/**
 * Java port of {@code numval_test/numval_test.cbl}.
 *
 * <p>Reads two numbers from stdin (one as a string, one as an integer) and
 * prints their sum. Equivalent to COBOL's {@code FUNCTION NUMVAL} which
 * converts a {@code PIC X} string to a numeric value.
 */
public final class NumvalTest {

    private NumvalTest() {}

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("Enter first number: ");
            String wsXVal = in.hasNextLine() ? in.nextLine().trim() : "0";

            System.out.print("Enter second number: ");
            String wsNineVal = in.hasNextLine() ? in.nextLine().trim() : "0";

            double total = parse(wsXVal) + parse(wsNineVal);
            // PIC ... COMP-2 is double precision, formatted with full precision.
            System.out.println("Total: " + total);
        }
    }

    private static double parse(String s) {
        if (s == null || s.isBlank()) {
            return 0;
        }
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
