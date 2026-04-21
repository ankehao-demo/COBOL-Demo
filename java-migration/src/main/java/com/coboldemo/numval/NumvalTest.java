package com.coboldemo.numval;

import java.util.Scanner;

/**
 * Port of {@code numval_test/numval_test.cbl} — reads a string-valued number
 * and a numeric value, converts the first with {@link Double#parseDouble}
 * (the Java analogue of COBOL's {@code FUNCTION NUMVAL}) and sums them.
 */
public final class NumvalTest {

    private NumvalTest() {
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first number: ");
        String firstRaw = scanner.nextLine().trim();
        double first = firstRaw.isEmpty() ? 0.0 : Double.parseDouble(firstRaw);

        System.out.print("Enter second number: ");
        String secondRaw = scanner.nextLine().trim();
        long second = secondRaw.isEmpty() ? 0L : Long.parseLong(secondRaw);

        double total = first + second;
        System.out.println("Total: " + total);
    }
}
