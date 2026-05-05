package com.coboldemo.isnumeric;

import java.util.Scanner;

/**
 * Java port of {@code is_numeric/is_numeric.cbl}.
 *
 * Reproduces the COBOL "IS NUMERIC" test in three flavours:
 * <ol>
 *   <li>plain - any embedded space causes the test to fail.</li>
 *   <li>zero-fill - right-justify in a fixed-width field, replace
 *       leading spaces with zeros, then test.</li>
 *   <li>trim - strip surrounding whitespace then test.</li>
 * </ol>
 */
public class IsNumericTest {

    private static final int FIELD_WIDTH = 10;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        processPlain(scanner);
        processZeroFill(scanner);
        processTrim(scanner);
    }

    private static void processPlain(Scanner scanner) {
        System.out.print("(plain) Enter a value: ");
        String input = readFixedWidth(scanner);
        if (isNumeric(input)) {
            System.out.println(input + " is numeric!");
        } else {
            System.out.println(input + " is not numeric.");
        }
    }

    private static void processZeroFill(Scanner scanner) {
        System.out.print("(right justify, zero fill) Enter another value: ");
        String raw = readFixedWidth(scanner);
        // Right-justify in FIELD_WIDTH chars, then replace leading spaces
        // with '0'. Trailing spaces / non-digits remain a failure case.
        String rightJustified = String.format("%" + FIELD_WIDTH + "s", raw.trim());
        String filled = rightJustified.replace(' ', '0');
        if (isNumeric(filled)) {
            System.out.println(filled + " is numeric!");
        } else {
            System.out.println(filled + " is not numeric.");
        }
    }

    private static void processTrim(Scanner scanner) {
        System.out.print("(trim) Enter a third value: ");
        String input = readFixedWidth(scanner);
        String trimmed = input.trim();
        if (isNumeric(trimmed)) {
            System.out.println(trimmed + " is numeric!");
        } else {
            System.out.println(trimmed + " is not numeric.");
        }
    }

    /** True iff the input is a non-empty string of decimal digits only. */
    static boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        return s.matches("\\d+");
    }

    private static String readFixedWidth(Scanner scanner) {
        String line = scanner.hasNextLine() ? scanner.nextLine() : "";
        if (line.length() > FIELD_WIDTH) {
            line = line.substring(0, FIELD_WIDTH);
        } else {
            line = String.format("%-" + FIELD_WIDTH + "s", line);
        }
        return line;
    }
}
