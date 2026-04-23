package com.coboldemo.isnumeric;

import java.util.Scanner;

/**
 * Java port of is_numeric/is_numeric.cbl.
 *
 * Demonstrates three ways to answer "IS NUMERIC" questions about user input
 * in Java: plain string-level check, right-justified zero-fill, and trim.
 */
public final class IsNumericTest {

    private IsNumericTest() {
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        processPlain(scanner);
        processZeroFill(scanner);
        processTrim(scanner);
    }

    private static void processPlain(Scanner scanner) {
        System.out.print("(plain) Enter a value: ");
        String input = readPadded(scanner, 10);
        if (isNumeric(input)) {
            System.out.println(input + " is numeric!");
        } else {
            System.out.println(input + " is not numeric.");
        }
    }

    private static void processZeroFill(Scanner scanner) {
        System.out.print("(right justify, zero fill) Enter another value: ");
        String raw = scanner.hasNextLine() ? scanner.nextLine() : "";
        if (raw.length() > 10) {
            raw = raw.substring(0, 10);
        }
        // Right-justify to 10 chars then replace leading spaces with '0'.
        String justified = String.format("%10s", raw).replace(' ', '0');
        if (isNumeric(justified)) {
            System.out.println(justified + " is numeric!");
        } else {
            System.out.println(justified + " is not numeric.");
        }
    }

    private static void processTrim(Scanner scanner) {
        System.out.print("(trim) Enter a third value: ");
        String input = readPadded(scanner, 10);
        String trimmed = input.trim();
        if (isNumeric(trimmed)) {
            System.out.println(trimmed + " is numeric!");
        } else {
            System.out.println(trimmed + " is not numeric.");
        }
    }

    /**
     * Replicates COBOL PIC X(10) by padding/truncating to a fixed width so the
     * plain-check behaviour matches the original program (trailing spaces
     * cause it to fail the numeric test).
     */
    private static String readPadded(Scanner scanner, int width) {
        String raw = scanner.hasNextLine() ? scanner.nextLine() : "";
        if (raw.length() > width) {
            return raw.substring(0, width);
        }
        return String.format("%-" + width + "s", raw);
    }

    private static boolean isNumeric(String value) {
        return value != null && !value.isEmpty() && value.matches("\\d+");
    }
}
