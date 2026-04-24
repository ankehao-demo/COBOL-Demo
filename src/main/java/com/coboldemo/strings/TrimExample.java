package com.coboldemo.strings;

/**
 * Migrated from: trim/trim.cbl
 *
 * Demonstrates the COBOL intrinsic TRIM function with LEADING, TRAILING,
 * and default (both) modes. In Java, these map to String.strip(),
 * String.stripLeading(), and String.stripTrailing().
 */
public class TrimExample {

    public static void main(String[] args) {
        String testString1 = padRight("    hello world       ", 30);

        System.out.println("--" + testString1 + "--");
        System.out.println("--" + trimBoth(testString1) + "--");
        System.out.println("--" + trimLeading(testString1) + "--");
        System.out.println("--" + trimTrailing(testString1) + "--");

        // Move demonstrations showing COBOL field-width behavior
        String testString2 = "******************************";
        System.out.println(testString2);
        // MOVE ws-test-string-1 TO ws-test-string-2 (pads/truncates to 30)
        testString2 = padRight(testString1, 30);
        System.out.println(testString2);

        testString2 = "******************************";
        System.out.println(testString2);
        // MOVE FUNCTION TRIM(ws-test-string-1) TO ws-test-string-2
        testString2 = padRight(trimBoth(testString1), 30);
        System.out.println(testString2);

        testString2 = "******************************";
        System.out.println(testString2);
        // MOVE FUNCTION TRIM(ws-test-string-1 LEADING) TO ws-test-string-2
        testString2 = padRight(trimLeading(testString1), 30);
        System.out.println(testString2);

        testString2 = "******************************";
        System.out.println(testString2);
        // MOVE FUNCTION TRIM(ws-test-string-1 TRAILING) TO ws-test-string-2
        testString2 = padRight(trimTrailing(testString1), 30);
        System.out.println(testString2);

        // String literal examples
        System.out.println("--" + "    String literal    " + "--");
        System.out.println("--" + trimBoth("   String literal    ") + "--");
        System.out.println("--" + trimLeading("     String literal   ") + "--");
        System.out.println("--" + trimTrailing("   String literal    ") + "--");
    }

    /** FUNCTION TRIM(x) - trim both leading and trailing spaces */
    public static String trimBoth(String s) {
        return s.strip();
    }

    /** FUNCTION TRIM(x LEADING) - trim only leading spaces */
    public static String trimLeading(String s) {
        return s.stripLeading();
    }

    /** FUNCTION TRIM(x TRAILING) - trim only trailing spaces */
    public static String trimTrailing(String s) {
        return s.stripTrailing();
    }

    static String padRight(String s, int len) {
        if (s.length() >= len) return s.substring(0, len);
        return String.format("%-" + len + "s", s);
    }
}
