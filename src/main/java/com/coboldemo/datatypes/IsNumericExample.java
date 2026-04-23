package com.coboldemo.datatypes;

import java.util.Scanner;

/**
 * Migrated from: is_numeric/is_numeric.cbl
 *
 * Demonstrates the COBOL "IS NUMERIC" test and various strategies for
 * handling alphanumeric input that may contain trailing spaces.
 * Three approaches: plain check, right-justify with zero-fill, and trim.
 */
public class IsNumericExample {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        processPlain(scanner);
        processZeroFill(scanner);
        processTrim(scanner);

        scanner.close();
    }

    /**
     * Plain check: if the raw input (padded to 10 chars) contains spaces,
     * it will not pass the numeric test, matching COBOL behavior where
     * trailing spaces cause IS NUMERIC to fail.
     */
    static void processPlain(Scanner scanner) {
        System.out.print("(plain) Enter a value: ");
        String input = scanner.nextLine();
        // Pad to 10 characters (COBOL PIC X(10) behavior)
        String padded = String.format("%-10s", input).substring(0, 10);

        if (isNumericWithSpaces(padded)) {
            System.out.println(padded + " is numeric!");
        } else {
            System.out.println(padded + " is not numeric.");
        }
    }

    /**
     * Right-justify and zero-fill: replace leading spaces with '0' then
     * check for numeric. This matches the COBOL JUSTIFIED RIGHT + INSPECT
     * REPLACING LEADING SPACES BY '0' pattern.
     */
    static void processZeroFill(Scanner scanner) {
        System.out.print("(right justify, zero fill) Enter another value: ");
        String input = scanner.nextLine();
        // Right-justify in 10-char field
        String justified = String.format("%10s", input).substring(0, 10);
        // Replace leading spaces with '0'
        StringBuilder sb = new StringBuilder(justified);
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == ' ') {
                sb.setCharAt(i, '0');
            } else {
                break;
            }
        }
        String zeroFilled = sb.toString();

        if (isAllDigits(zeroFilled)) {
            System.out.println(zeroFilled + " is numeric!");
        } else {
            System.out.println(zeroFilled + " is not numeric.");
        }
    }

    /**
     * Trim: using trim to remove spaces before checking for numeric.
     * Matches COBOL FUNCTION TRIM behavior.
     */
    static void processTrim(Scanner scanner) {
        System.out.print("(trim) Enter a third value: ");
        String input = scanner.nextLine();
        String trimmed = input.trim();

        if (!trimmed.isEmpty() && isAllDigits(trimmed)) {
            System.out.println(trimmed + " is numeric!");
        } else {
            System.out.println(trimmed + " is not numeric.");
        }
    }

    /**
     * Checks if the entire string (including spaces) is numeric.
     * In COBOL, a PIC X field with trailing spaces fails IS NUMERIC.
     */
    static boolean isNumericWithSpaces(String s) {
        if (s == null || s.isEmpty()) return false;
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    static boolean isAllDigits(String s) {
        if (s == null || s.isEmpty()) return false;
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
}
