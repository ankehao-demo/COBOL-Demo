package com.coboldemo.isnumeric;

import java.util.Scanner;

/**
 * Java equivalent of is_numeric/is_numeric.cbl
 *
 * Demonstrates the COBOL "IS NUMERIC" test. In COBOL, an alphanumeric field
 * with trailing spaces will fail the IS NUMERIC test even if the user typed
 * only digits. This program shows three approaches: plain check, right-justify
 * with zero-fill, and trim before checking.
 *
 * COBOL Mapping:
 *   IS NUMERIC → try Integer.parseInt() or regex \\d+
 *   INSPECT ... REPLACING LEADING SPACES BY '0' → right-justify + zero-pad
 *   FUNCTION TRIM(...) → String.trim()
 */
public class IsNumericTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        processPlain(scanner);
        processZeroFill(scanner);
        processTrim(scanner);

        scanner.close();
    }

    /**
     * Plain check: in COBOL, trailing spaces cause IS NUMERIC to fail.
     * We simulate this by checking the raw (un-trimmed) input including spaces.
     */
    private static void processPlain(Scanner scanner) {
        System.out.print("(plain) Enter a value: ");
        String input = scanner.nextLine();

        // Pad to 10 chars to simulate PIC X(10) behavior
        String padded = String.format("%-10s", input);

        if (padded.matches("\\d+")) {
            System.out.println(padded + " is numeric!");
        } else {
            System.out.println(padded + " is not numeric.");
        }
    }

    /**
     * Right-justify and zero-fill: simulates COBOL JUSTIFIED RIGHT with
     * INSPECT REPLACING LEADING SPACES BY '0'.
     */
    private static void processZeroFill(Scanner scanner) {
        System.out.print("(right justify, zero fill) Enter another value: ");
        String input = scanner.nextLine();

        // Right-justify in a 10-char field, then replace leading spaces with '0'
        String rightJustified = String.format("%10s", input);
        String zeroFilled = rightJustified.replace(' ', '0');

        if (zeroFilled.matches("\\d+")) {
            System.out.println(zeroFilled + " is numeric!");
        } else {
            System.out.println(zeroFilled + " is not numeric.");
        }
    }

    /**
     * Trim approach: using FUNCTION TRIM to remove spaces before testing.
     */
    private static void processTrim(Scanner scanner) {
        System.out.print("(trim) Enter a third value: ");
        String input = scanner.nextLine();

        String trimmed = input.trim();
        if (!trimmed.isEmpty() && trimmed.matches("\\d+")) {
            System.out.println(trimmed + " is numeric!");
        } else {
            System.out.println(trimmed + " is not numeric.");
        }
    }
}
