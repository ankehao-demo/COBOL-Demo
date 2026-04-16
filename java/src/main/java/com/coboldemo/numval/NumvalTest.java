package com.coboldemo.numval;

import java.util.Scanner;

/**
 * Migrated from: numval_test/numval_test.cbl
 *
 * COBOL-to-Java mapping:
 *   PIC X(10)       -> String (alphanumeric input field)
 *   PIC 9(6)V99     -> double (numeric field with 2 implied decimal places)
 *   IS NUMERIC      -> regex check (digits only)
 *   FUNCTION NUMVAL -> Double.parseDouble() (converts string to numeric)
 *   ACCEPT          -> Scanner.nextLine()
 *
 * COBOL's FUNCTION NUMVAL converts an alphanumeric string representation
 * of a number (which may include leading/trailing spaces, a sign, or a
 * decimal point) into a numeric value. Java's Double.parseDouble() provides
 * equivalent functionality.
 *
 * COBOL PIC 9(6)V99 stores a number with 6 integer digits and 2 decimal
 * places. We format the output to match this display pattern.
 */
public class NumvalTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // COBOL: 01 ws-val-to-test PIC X(10).
        // ACCEPT ws-val-to-test
        System.out.print("Enter a numeric value: ");
        String wsValToTest = scanner.nextLine();

        // COBOL: 01 ws-numval-result PIC 9(6)V99.
        double wsNumvalResult;

        // COBOL: IF ws-val-to-test IS NUMERIC
        // Note: COBOL IS NUMERIC on PIC X checks all chars are digits (no decimal point).
        // NUMVAL can still parse values with decimals/signs even if IS NUMERIC is false.
        if (isNumeric(wsValToTest.trim())) {
            System.out.println("Value is numeric");
            // COMPUTE ws-numval-result = FUNCTION NUMVAL(ws-val-to-test)
            wsNumvalResult = numval(wsValToTest);
            // Display formatted as PIC 9(6)V99 (8 digits total, 2 decimal places)
            System.out.println("NUMVAL result: " + formatPic9_6V99(wsNumvalResult));
        } else {
            System.out.println("Value is not numeric, attempting numval anyway...");
            // COBOL attempts NUMVAL even for non-numeric values.
            // NUMVAL can handle strings with signs, decimal points, and spaces.
            wsNumvalResult = numval(wsValToTest);
            System.out.println("NUMVAL result: " + formatPic9_6V99(wsNumvalResult));
        }

        scanner.close();
    }

    /**
     * Mimics COBOL IS NUMERIC test: checks if the trimmed string contains only digits.
     */
    private static boolean isNumeric(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        return value.matches("\\d+");
    }

    /**
     * Mimics COBOL FUNCTION NUMVAL: converts an alphanumeric string to a numeric value.
     * NUMVAL handles leading/trailing spaces, optional sign, and decimal points.
     * In Java, Double.parseDouble() provides equivalent conversion.
     *
     * @param value the string to convert
     * @return the numeric value, or 0.0 if conversion fails
     */
    private static double numval(String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            // COBOL NUMVAL on invalid data may cause a runtime error.
            // In Java we return 0 and print a warning for safety.
            System.out.println("Warning: Could not parse '" + value.trim() + "' as a number.");
            return 0.0;
        }
    }

    /**
     * Formats a double value to match COBOL PIC 9(6)V99 display format.
     * This produces a string with 6 integer digits and 2 decimal digits,
     * zero-filled on the left (e.g., 123.45 -> "00012345" displayed as "000123.45").
     */
    private static String formatPic9_6V99(double value) {
        // Take absolute value for formatting (COBOL PIC 9 is unsigned)
        double absValue = Math.abs(value);
        // Format with 2 decimal places, zero-filled to 9 chars total (6 digits + '.' + 2 digits)
        return String.format("%09.2f", absValue);
    }
}
