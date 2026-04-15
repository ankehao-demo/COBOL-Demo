package com.coboldemo.trim;

/**
 * Java equivalent of trim/trim.cbl
 *
 * Demonstrates the COBOL intrinsic FUNCTION TRIM with LEADING, TRAILING,
 * and default (both) options. Also shows how MOVE with TRIM affects a
 * fixed-length destination field (PIC X(30)).
 *
 * COBOL Mapping:
 *   FUNCTION TRIM(str)          → str.trim()        (or str.strip())
 *   FUNCTION TRIM(str LEADING)  → str.stripLeading()
 *   FUNCTION TRIM(str TRAILING) → str.stripTrailing()
 */
public class TrimExample {

    public static void main(String[] args) {
        // ws-test-string-1 PIC X(30) VALUE "    hello world       "
        String testString1 = padRight("    hello world       ", 30);

        // DISPLAY "--" ws-test-string-1 "--"
        System.out.println("--" + testString1 + "--");
        // FUNCTION TRIM (both)
        System.out.println("--" + testString1.trim() + "--");
        // FUNCTION TRIM LEADING
        System.out.println("--" + testString1.stripLeading() + "--");
        // FUNCTION TRIM TRAILING
        System.out.println("--" + testString1.stripTrailing() + "--");

        // Demonstrate MOVE to a PIC X(30) field (fixed-length, padded)
        String testString2;

        testString2 = padRight("******************************", 30);
        System.out.println(testString2);
        // MOVE ws-test-string-1 TO ws-test-string-2 (direct move, keeps spaces)
        testString2 = padRight(testString1, 30);
        System.out.println(testString2);

        testString2 = padRight("******************************", 30);
        System.out.println(testString2);
        // MOVE FUNCTION TRIM(ws-test-string-1) TO ws-test-string-2
        testString2 = padRight(testString1.trim(), 30);
        System.out.println(testString2);

        testString2 = padRight("******************************", 30);
        System.out.println(testString2);
        // MOVE FUNCTION TRIM(ws-test-string-1 LEADING)
        testString2 = padRight(testString1.stripLeading(), 30);
        System.out.println(testString2);

        testString2 = padRight("******************************", 30);
        System.out.println(testString2);
        // MOVE FUNCTION TRIM(ws-test-string-1 TRAILING)
        testString2 = padRight(testString1.stripTrailing(), 30);
        System.out.println(testString2);

        // String literal examples
        String literal = "    String literal    ";
        System.out.println("--" + literal + "--");
        System.out.println("--" + "   String literal    ".trim() + "--");
        System.out.println("--" + "     String literal   ".stripLeading() + "--");
        System.out.println("--" + "   String literal    ".stripTrailing() + "--");
    }

    /**
     * Pads a string to a fixed length with trailing spaces, simulating
     * COBOL PIC X(n) field behavior.
     */
    private static String padRight(String s, int length) {
        if (s.length() >= length) {
            return s.substring(0, length);
        }
        return String.format("%-" + length + "s", s);
    }
}
