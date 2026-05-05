package com.coboldemo.trim;

/**
 * Migrated from trim/trim.cbl
 * Demonstrates FUNCTION TRIM equivalents in Java.
 */
public class TrimTest {

    public static void main(String[] args) {
        // PIC X(30) VALUE "    hello world       "
        String testString1 = padRight("    hello world       ", 30);

        // Display with delimiters
        System.out.println("--" + testString1 + "--");
        System.out.println("--" + testString1.strip() + "--");
        System.out.println("--" + testString1.stripLeading() + "--");
        System.out.println("--" + testString1.stripTrailing() + "--");

        // MOVE behavior: destination is 30 chars filled with "*"
        // When MOVEing in COBOL, the destination is overwritten completely (right-padded with spaces)
        String dest;

        dest = padRight("******************************", 30);
        System.out.println(dest);
        dest = padRight(testString1, 30);
        System.out.println(dest);

        dest = padRight("******************************", 30);
        System.out.println(dest);
        dest = padRight(testString1.strip(), 30);
        System.out.println(dest);

        dest = padRight("******************************", 30);
        System.out.println(dest);
        dest = padRight(testString1.stripLeading(), 30);
        System.out.println(dest);

        dest = padRight("******************************", 30);
        System.out.println(dest);
        dest = padRight(testString1.stripTrailing(), 30);
        System.out.println(dest);

        // String literal tests
        String literal = "    String literal    ";
        System.out.println("--" + literal + "--");
        System.out.println("--" + literal.strip() + "--");
        System.out.println("--" + literal.stripLeading() + "--");
        System.out.println("--" + literal.stripTrailing() + "--");
    }

    private static String padRight(String s, int width) {
        if (s.length() >= width) {
            return s.substring(0, width);
        }
        return String.format("%-" + width + "s", s);
    }
}
