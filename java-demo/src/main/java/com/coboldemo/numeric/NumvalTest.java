package com.coboldemo.numeric;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Java equivalent of {@code numval_test/numval_test.cbl}.
 *
 * <p>The COBOL program reads two strings, parses one of them with
 * {@code FUNCTION NUMVAL}, adds it to the (pre-numeric) second value,
 * and stores the result in a {@code COMP-2} (double-precision binary
 * float) field.</p>
 *
 * <p>Java equivalents:
 * <ul>
 *   <li>{@code FUNCTION NUMVAL(string)} &rarr;
 *       {@link Double#parseDouble(String)} for an IEEE-754 result, or
 *       {@link BigDecimal#BigDecimal(String)} when exact decimal
 *       precision matters (e.g. money). {@code COMP-2} maps to
 *       {@code double}.</li>
 *   <li>{@code FUNCTION NUMVAL-C(string)} &rarr; strip the currency
 *       symbol and grouping commas first, then parse. We model that
 *       here as a small helper.</li>
 * </ul>
 * The program runs through several canned inputs (whitespace,
 * leading/trailing spaces, embedded sign, scientific notation,
 * currency-formatted strings) so you can see exactly how the parser
 * behaves on each.</p>
 */
public class NumvalTest {

    public static void main(String[] args) {
        System.out.println("FUNCTION NUMVAL / NUMVAL-C demo");
        System.out.println("-------------------------------");

        String[] numvalSamples = {
            "12345",
            "  12345  ",
            "-42",
            "+42",
            "3.14159",
            "  -2.5e3  ",
            "  not a number  ",
            ""
        };

        System.out.println();
        System.out.println("FUNCTION NUMVAL via Double.parseDouble:");
        for (String sample : numvalSamples) {
            double result = numval(sample);
            System.out.println(String.format(
                    "  numval(%-22s) = %s",
                    "\"" + sample + "\"",
                    Double.isNaN(result) ? "<not numeric>" : Double.toString(result)));
        }

        System.out.println();
        System.out.println("FUNCTION NUMVAL via BigDecimal (exact decimal precision):");
        for (String sample : numvalSamples) {
            BigDecimal result = numvalExact(sample);
            System.out.println(String.format(
                    "  numvalExact(%-22s) = %s",
                    "\"" + sample + "\"",
                    result == null ? "<not numeric>" : result.toPlainString()));
        }

        String[] currencySamples = {
            "$1,234.56",
            "  -$2,000.00 ",
            "USD 12,345.67",
            "1.234.567,89", // European-style; not handled, demoes failure
            "1,234"
        };

        System.out.println();
        System.out.println("FUNCTION NUMVAL-C (strip currency / grouping commas):");
        for (String sample : currencySamples) {
            BigDecimal result = numvalC(sample);
            System.out.println(String.format(
                    "  numvalC(%-22s) = %s",
                    "\"" + sample + "\"",
                    result == null ? "<not numeric>" : result.toPlainString()));
        }

        // Replicate the original COBOL flow: prompt for two values,
        // parse the first via NUMVAL, leave the second as numeric.
        if (System.console() != null) {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.print("Enter first number: ");
                String first = scanner.hasNextLine() ? scanner.nextLine() : "";
                System.out.print("Enter second number: ");
                String second = scanner.hasNextLine() ? scanner.nextLine() : "";

                double total = numval(first) + numval(second);
                System.out.println("Total: " + total);
            }
        } else {
            System.out.println();
            System.out.println("Interactive demo skipped: stdin is not a TTY. "
                    + "Run via `mvn exec:java` from a real terminal to try it.");
        }
    }

    /**
     * COBOL {@code FUNCTION NUMVAL} parses an alphanumeric value into a
     * numeric one. Whitespace is ignored; the result is double-precision.
     * Returns {@link Double#NaN} for inputs that cannot be parsed,
     * which the caller can detect via {@link Double#isNaN}.
     */
    public static double numval(String input) {
        if (input == null) return Double.NaN;
        String trimmed = input.trim();
        if (trimmed.isEmpty()) return Double.NaN;
        try {
            return Double.parseDouble(trimmed);
        } catch (NumberFormatException nfe) {
            return Double.NaN;
        }
    }

    /**
     * Variant of {@link #numval(String)} that preserves exact decimal
     * precision via {@link BigDecimal}. Returns {@code null} on parse
     * failure.
     */
    public static BigDecimal numvalExact(String input) {
        if (input == null) return null;
        String trimmed = input.trim();
        if (trimmed.isEmpty()) return null;
        try {
            return new BigDecimal(trimmed);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    /**
     * COBOL {@code FUNCTION NUMVAL-C} accepts currency-formatted
     * strings: a leading currency symbol and grouping commas are
     * stripped before the value is parsed. Returns {@code null} on
     * parse failure.
     */
    public static BigDecimal numvalC(String input) {
        if (input == null) return null;
        // Drop everything that isn't a digit, sign, or decimal point.
        String stripped = input.replaceAll("[^0-9+\\-\\.]", "");
        if (stripped.isEmpty() || stripped.equals("+") || stripped.equals("-")) {
            return null;
        }
        try {
            return new BigDecimal(stripped);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }
}
