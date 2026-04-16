package com.coboldemo.isnumeric;

import java.util.Scanner;

/**
 * Migrated from: is_numeric/is_numeric.cbl
 *
 * COBOL-to-Java mapping:
 *   PIC X(10)         -> String (alphanumeric field, 10 characters)
 *   PIC 9(10)         -> String (numeric display field, 10 digits, right-justified zero-filled)
 *   IS NUMERIC        -> regex check or Integer.parseInt() with try/catch
 *   FUNCTION TRIM()   -> String.trim()
 *   ACCEPT            -> Scanner.nextLine()
 *
 * COBOL's IS NUMERIC test checks if every character in the field is a digit (0-9).
 * In Java we replicate this with a regex pattern \\d+ (one or more digits).
 *
 * The three approaches demonstrated:
 *   1. Direct check on the raw input field (PIC X(10))
 *   2. Right-justify and zero-fill (MOVE to PIC 9(10)), then check
 *   3. Trim whitespace first, then check
 */
public class IsNumericTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // COBOL: 01 ws-test-num-1 PIC X(10).
        // ACCEPT ws-test-num-1
        System.out.print("Enter a number: ");
        String wsTestNum1 = scanner.nextLine();

        // Pad or truncate to 10 characters to mimic PIC X(10) behavior
        wsTestNum1 = padRight(wsTestNum1, 10);

        // ----- Approach 1: Checking input field directly -----
        // COBOL: IF ws-test-num-1 IS NUMERIC
        // In COBOL, IS NUMERIC on PIC X(10) checks all 10 characters are digits.
        // Spaces are NOT numeric, so a padded field with trailing spaces will fail.
        System.out.println("Approach 1: Checking input field directly");
        if (isNumeric(wsTestNum1)) {
            System.out.println(wsTestNum1 + " is numeric.");
        } else {
            System.out.println(wsTestNum1 + " is NOT numeric.");
        }

        // ----- Approach 2: Right justify and zero fill -----
        // COBOL: MOVE ws-test-num-1 TO ws-test-num-2 (PIC 9(10))
        // When moving alphanumeric to numeric, COBOL right-justifies and zero-fills.
        // If the source contains non-numeric chars, the result may be unpredictable.
        // We simulate this: extract digits from input, right-justify, and zero-fill.
        System.out.println("Approach 2: Right justify and zero fill");
        String wsTestNum2 = rightJustifyZeroFill(wsTestNum1, 10);
        if (isNumeric(wsTestNum2)) {
            System.out.println(wsTestNum2 + " is numeric.");
        } else {
            System.out.println(wsTestNum2 + " is NOT numeric.");
        }

        // ----- Approach 3: Trim input -----
        // COBOL: MOVE FUNCTION TRIM(ws-test-num-1) TO ws-test-num-3
        // Then check IS NUMERIC on the trimmed value.
        System.out.println("Approach 3: Trim input (probably the most common)");
        String wsTestNum3 = wsTestNum1.trim();
        if (isNumeric(wsTestNum3)) {
            System.out.println(wsTestNum3 + " is numeric.");
        } else {
            System.out.println(wsTestNum3 + " is NOT numeric.");
        }

        scanner.close();
    }

    /**
     * Checks if a string is numeric (all characters are digits).
     * Mirrors COBOL's IS NUMERIC test which checks every character is 0-9.
     * An empty string is not considered numeric.
     */
    private static boolean isNumeric(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        return value.matches("\\d+");
    }

    /**
     * Pads a string with spaces on the right to reach the target length,
     * or truncates if longer. Mimics COBOL PIC X(n) field storage.
     */
    private static String padRight(String value, int length) {
        if (value.length() >= length) {
            return value.substring(0, length);
        }
        return String.format("%-" + length + "s", value);
    }

    /**
     * Right-justifies and zero-fills a value to the target length.
     * Mimics COBOL behavior when moving an alphanumeric (PIC X) value
     * to a numeric (PIC 9) field: the value is right-justified and
     * leading positions are filled with zeros.
     */
    private static String rightJustifyZeroFill(String value, int length) {
        String trimmed = value.trim();
        if (trimmed.length() >= length) {
            return trimmed.substring(trimmed.length() - length);
        }
        return "0".repeat(length - trimmed.length()) + trimmed;
    }
}
