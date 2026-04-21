package com.coboldemo.trim;

/**
 * Port of {@code trim/trim.cbl} — demonstrates the three Java trimming
 * methods that correspond to COBOL {@code FUNCTION TRIM}.
 *
 * <p>Equivalent mapping:
 * <ul>
 *     <li>{@code FUNCTION TRIM(s)} → {@link String#strip()} (or
 *         {@link String#trim()} for ASCII whitespace only).</li>
 *     <li>{@code FUNCTION TRIM(s LEADING)} → {@link String#stripLeading()}.</li>
 *     <li>{@code FUNCTION TRIM(s TRAILING)} → {@link String#stripTrailing()}.</li>
 * </ul>
 */
public final class TrimFunctionTest {

    private TrimFunctionTest() {
    }

    public static void main(String[] args) {
        // COBOL declares ws-test-string-1 PIC X(30) VALUE "    hello world       ".
        // The field is padded to 30 characters. We replicate the width here
        // so the visual output (bounded by "--") matches the COBOL version.
        String testString1 = padRight("    hello world       ", 30);

        System.out.println("--" + testString1 + "--");
        System.out.println("--" + testString1.strip() + "--");
        System.out.println("--" + testString1.stripLeading() + "--");
        System.out.println("--" + testString1.stripTrailing() + "--");

        String filler = "*".repeat(30);
        String testString2;

        // First: show the 30-char field, then overwrite it with the
        // padded source and display again. This mirrors how COBOL MOVE
        // truncates or pads to the destination field size.
        System.out.println(filler);
        testString2 = fit(testString1, 30);
        System.out.println(testString2);

        System.out.println(filler);
        testString2 = fit(testString1.strip(), 30);
        System.out.println(testString2);

        System.out.println(filler);
        testString2 = fit(testString1.stripLeading(), 30);
        System.out.println(testString2);

        System.out.println(filler);
        testString2 = fit(testString1.stripTrailing(), 30);
        System.out.println(testString2);

        // String literal trimming.
        System.out.println("--" + "    String literal    " + "--");
        System.out.println("--" + "   String literal    ".strip() + "--");
        System.out.println("--" + "     String literal   ".stripLeading() + "--");
        System.out.println("--" + "   String literal    ".stripTrailing() + "--");
    }

    private static String fit(String s, int width) {
        if (s.length() >= width) {
            return s.substring(0, width);
        }
        return s + " ".repeat(width - s.length());
    }

    private static String padRight(String s, int width) {
        if (s.length() >= width) {
            return s.substring(0, width);
        }
        return s + " ".repeat(width - s.length());
    }
}
