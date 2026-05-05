package com.coboldemo.numeric;

import java.util.regex.Pattern;

/**
 * Java equivalent of {@code is_numeric/is_numeric.cbl}.
 *
 * <p>The COBOL program demonstrates the {@code IF ws-variable IS
 * NUMERIC} class condition under three different normalisations:
 * <ol>
 *   <li>plain (no normalisation),</li>
 *   <li>right-justified with leading spaces replaced by {@code '0'},</li>
 *   <li>passed through {@code FUNCTION TRIM} before the test.</li>
 * </ol>
 * COBOL's {@code IS NUMERIC} only accepts a strictly contiguous run of
 * digits with an optional sign, so an input like {@code "  42  "}
 * fails the plain check but passes the trim/zero-fill versions.</p>
 *
 * <p>This Java port supports the same three modes and exercises both
 * idiomatic ways of doing numeric validation in Java:
 * <ul>
 *   <li>{@code try { Long.parseLong(s); return true; }
 *       catch (NumberFormatException e) { return false; }}</li>
 *   <li>a precompiled regex like {@code ^[+-]?\d+$}.</li>
 * </ul>
 * The two strategies should always agree for these inputs.</p>
 */
public class IsNumeric {

    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^[+-]?\\d+$");

    public static void main(String[] args) {
        String[] samples = {
            "12345",
            "  42  ",
            "0042",
            "-7",
            "+9",
            "12.34",
            "1e3",
            "abc",
            "  ",
            "",
            "  42"
        };

        printHeader();
        for (String sample : samples) {
            check("plain    ", sample, sample);
            check("zero-fill", sample, rightJustifyZeroFill(sample, 10));
            check("trim     ", sample, sample == null ? null : sample.trim());
        }
    }

    private static void printHeader() {
        System.out.println(String.format(
                "%-10s %-15s %-15s %-12s %-12s",
                "mode", "input", "normalized", "parseLong", "regex"));
        System.out.println("---------- --------------- --------------- "
                + "------------ ------------");
    }

    private static void check(String mode, String original, String normalized) {
        System.out.println(String.format(
                "%-10s %-15s %-15s %-12s %-12s",
                mode,
                "\"" + original + "\"",
                "\"" + (normalized == null ? "" : normalized) + "\"",
                isNumericByParse(normalized) ? "numeric" : "not numeric",
                isNumericByRegex(normalized) ? "numeric" : "not numeric"));
    }

    /**
     * Validates by parsing. {@code IS NUMERIC} in COBOL allows leading
     * sign characters but no whitespace, so we match {@code Long.parseLong}'s
     * contract directly.
     */
    public static boolean isNumericByParse(String value) {
        if (value == null || value.isEmpty()) return false;
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /**
     * Validates with a precompiled regex.
     */
    public static boolean isNumericByRegex(String value) {
        if (value == null) return false;
        return NUMERIC_PATTERN.matcher(value).matches();
    }

    /**
     * COBOL: {@code 01 ws-input PIC X(10) JUSTIFIED RIGHT.
     * INSPECT ws-input REPLACING LEADING SPACES BY '0'.}
     * Right-justifies the string in a fixed-width buffer and replaces
     * the resulting leading spaces with zeros.
     */
    private static String rightJustifyZeroFill(String value, int width) {
        if (value == null) return null;
        String trimmed = value.trim();
        if (trimmed.length() >= width) {
            return trimmed.substring(trimmed.length() - width);
        }
        StringBuilder sb = new StringBuilder(width);
        for (int i = 0; i < width - trimmed.length(); i++) {
            sb.append('0');
        }
        sb.append(trimmed);
        return sb.toString();
    }
}
