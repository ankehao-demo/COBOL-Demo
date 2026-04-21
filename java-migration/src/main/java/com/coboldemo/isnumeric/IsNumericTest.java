package com.coboldemo.isnumeric;

import java.util.Scanner;

/**
 * Port of {@code is_numeric/is_numeric.cbl} — three ways to verify that a
 * user-entered value is numeric, mirroring the three COBOL paragraphs.
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

    /**
     * Plain numeric check — matches the COBOL {@code IS NUMERIC} test on a
     * raw {@code PIC X(10)} field, which fails when there are trailing
     * spaces because the spaces are part of the value being tested.
     */
    private static void processPlain(Scanner scanner) {
        System.out.print("(plain) Enter a value: ");
        String input = padRight(scanner.nextLine(), 10);
        if (input.matches("\\d+")) {
            System.out.println(input + " is numeric!");
        } else {
            System.out.println(input + " is not numeric.");
        }
    }

    /**
     * Right-justify the input (padded to 10 chars) and replace leading
     * spaces with zeros, mirroring COBOL's {@code INSPECT ... REPLACING
     * LEADING SPACES BY '0'}.
     */
    private static void processZeroFill(Scanner scanner) {
        System.out.print("(right justify, zero fill) Enter another value: ");
        String raw = scanner.nextLine();
        StringBuilder sb = new StringBuilder(padLeft(raw, 10));
        for (int i = 0; i < sb.length() && sb.charAt(i) == ' '; i++) {
            sb.setCharAt(i, '0');
        }
        String justified = sb.toString();
        if (justified.matches("\\d+")) {
            System.out.println(justified + " is numeric!");
        } else {
            System.out.println(justified + " is not numeric.");
        }
    }

    /**
     * Trim the input (COBOL intrinsic {@code TRIM}) before running the
     * numeric check, which succeeds for contiguous digits surrounded by
     * whitespace.
     */
    private static void processTrim(Scanner scanner) {
        System.out.print("(trim) Enter a third value: ");
        String input = scanner.nextLine();
        String trimmed = input.trim();
        if (trimmed.matches("\\d+")) {
            System.out.println(trimmed + " is numeric!");
        } else {
            System.out.println(trimmed + " is not numeric.");
        }
    }

    private static String padRight(String s, int width) {
        if (s.length() >= width) {
            return s.substring(0, width);
        }
        return s + " ".repeat(width - s.length());
    }

    private static String padLeft(String s, int width) {
        if (s.length() >= width) {
            return s.substring(s.length() - width);
        }
        return " ".repeat(width - s.length()) + s;
    }
}
