package com.coboldemo.trim;

/**
 * Java port of trim/trim.cbl.
 *
 * Maps COBOL's intrinsic {@code FUNCTION TRIM} variants to Java:
 * <ul>
 *   <li>{@code TRIM(str)} -> {@link String#strip()}</li>
 *   <li>{@code TRIM(str LEADING)} -> {@link String#stripLeading()}</li>
 *   <li>{@code TRIM(str TRAILING)} -> {@link String#stripTrailing()}</li>
 * </ul>
 * Output mirrors the COBOL version's {@code "--"} delimiters so whitespace
 * removal is visible at a glance.
 */
public final class TrimFunctionTest {

    private TrimFunctionTest() {
    }

    public static void main(String[] args) {
        // Mirror PIC X(30) by padding to width 30.
        String testString1 = padRight("    hello world       ", 30);

        System.out.println("--" + testString1 + "--");
        System.out.println("--" + testString1.strip() + "--");
        System.out.println("--" + testString1.stripLeading() + "--");
        System.out.println("--" + testString1.stripTrailing() + "--");

        // Equivalent of moving stars then overwriting with the trimmed value.
        String stars = "******************************";
        String testString2;

        testString2 = stars;
        System.out.println(testString2);
        testString2 = fit(testString1, 30);
        System.out.println(testString2);

        testString2 = stars;
        System.out.println(testString2);
        testString2 = fit(testString1.strip(), 30);
        System.out.println(testString2);

        testString2 = stars;
        System.out.println(testString2);
        testString2 = fit(testString1.stripLeading(), 30);
        System.out.println(testString2);

        testString2 = stars;
        System.out.println(testString2);
        testString2 = fit(testString1.stripTrailing(), 30);
        System.out.println(testString2);

        System.out.println("--" + "    String literal    " + "--");
        System.out.println("--" + "   String literal    ".strip() + "--");
        System.out.println("--" + "     String literal   ".stripLeading()
                + "--");
        System.out.println("--" + "   String literal    ".stripTrailing()
                + "--");
    }

    private static String padRight(String value, int width) {
        if (value.length() >= width) {
            return value.substring(0, width);
        }
        return value + " ".repeat(width - value.length());
    }

    /** Simulate MOVE into a PIC X(n) variable: right-pad or truncate. */
    private static String fit(String value, int width) {
        return padRight(value, width);
    }
}
