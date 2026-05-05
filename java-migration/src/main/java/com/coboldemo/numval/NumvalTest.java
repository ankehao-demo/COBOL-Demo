package com.coboldemo.numval;

import java.util.Scanner;

/**
 * Java port of {@code numval_test/numval_test.cbl}.
 *
 * Reads two numbers from the console, parses the alphanumeric one with
 * the equivalent of COBOL's {@code FUNCTION NUMVAL} ({@link Double#parseDouble})
 * and prints the sum.
 */
public class NumvalTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first number: ");
        String firstRaw = scanner.hasNextLine() ? scanner.nextLine() : "";
        double first = numval(firstRaw);

        System.out.print("Enter second number: ");
        String secondRaw = scanner.hasNextLine() ? scanner.nextLine() : "";
        long second;
        try {
            second = Long.parseLong(secondRaw.trim());
        } catch (NumberFormatException e) {
            second = 0;
        }

        double total = first + second;
        System.out.println("Total: " + total);
    }

    /** Java equivalent of COBOL's intrinsic {@code NUMVAL}. */
    static double numval(String s) {
        if (s == null) {
            return 0;
        }
        String trimmed = s.trim();
        if (trimmed.isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(trimmed);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
