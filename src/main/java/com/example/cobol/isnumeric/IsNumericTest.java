package com.example.cobol.isnumeric;

import java.util.Scanner;

/**
 * Java port of {@code is_numeric/is_numeric.cbl}.
 *
 * <p>Demonstrates three approaches to checking whether a string of input
 * is numeric, mirroring the COBOL "IS NUMERIC" check applied to
 * <ul>
 *   <li>plain input — fails when there is whitespace</li>
 *   <li>right-justified, zero-filled input</li>
 *   <li>trimmed input</li>
 * </ul>
 */
public final class IsNumericTest {

    private IsNumericTest() {}

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            processPlain(in);
            processZeroFill(in);
            processTrim(in);
        }
    }

    private static void processPlain(Scanner in) {
        // Mirrors COBOL behavior: a 10-char field with trailing spaces does
        // *not* pass IS NUMERIC. To replicate, we right-pad to width 10 and
        // require every character to be a digit.
        System.out.print("(plain) Enter a value: ");
        String input = padRight(in.hasNextLine() ? in.nextLine() : "", 10);
        boolean isNum = input.matches("\\d{10}");
        System.out.println(input + (isNum ? " is numeric!" : " is not numeric."));
    }

    private static void processZeroFill(Scanner in) {
        // Right-justify and fill leading spaces with zeros, then test.
        System.out.print("(right justify, zero fill) Enter another value: ");
        String raw = in.hasNextLine() ? in.nextLine() : "";
        String justified = padLeft(raw, 10).replace(' ', '0');
        boolean isNum = justified.matches("\\d{10}");
        System.out.println(justified + (isNum ? " is numeric!" : " is not numeric."));
    }

    private static void processTrim(Scanner in) {
        // Same as the COBOL trim() variant — strip both ends and check that
        // the remainder is all digits.
        System.out.print("(trim) Enter a third value: ");
        String raw = in.hasNextLine() ? in.nextLine() : "";
        String trimmed = raw.strip();
        boolean isNum = !trimmed.isEmpty() && trimmed.matches("\\d+");
        System.out.println(trimmed + (isNum ? " is numeric!" : " is not numeric."));
    }

    private static String padRight(String s, int width) {
        if (s.length() >= width) {
            return s.substring(0, width);
        }
        return String.format("%-" + width + "s", s);
    }

    private static String padLeft(String s, int width) {
        if (s.length() >= width) {
            return s.substring(0, width);
        }
        return String.format("%" + width + "s", s);
    }
}
