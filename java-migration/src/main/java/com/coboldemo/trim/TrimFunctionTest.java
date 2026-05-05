package com.coboldemo.trim;

/**
 * Java port of {@code trim/trim.cbl}.
 *
 * Demonstrates trimming whitespace with the equivalents of COBOL's
 * intrinsic {@code TRIM}, {@code TRIM(... LEADING)}, and
 * {@code TRIM(... TRAILING)} - i.e. {@link String#trim},
 * {@link String#stripLeading}, and {@link String#stripTrailing}.
 *
 * The first test string is padded to 30 characters to match the COBOL
 * {@code PIC X(30)} declaration, and we surround the printed values with
 * "--" delimiters to make the trailing space behaviour visible.
 */
public class TrimFunctionTest {

    private static final int WIDTH = 30;

    public static void main(String[] args) {
        String wsTestString1 = padToWidth("    hello world       ", WIDTH);

        System.out.println("--" + wsTestString1 + "--");
        System.out.println("--" + wsTestString1.trim() + "--");
        System.out.println("--" + wsTestString1.stripLeading() + "--");
        System.out.println("--" + wsTestString1.stripTrailing() + "--");

        String wsTestString2 = "******************************";
        System.out.println(wsTestString2);
        wsTestString2 = padToWidth(wsTestString1, WIDTH);
        System.out.println(wsTestString2);

        wsTestString2 = "******************************";
        System.out.println(wsTestString2);
        wsTestString2 = padToWidth(wsTestString1.trim(), WIDTH);
        System.out.println(wsTestString2);

        wsTestString2 = "******************************";
        System.out.println(wsTestString2);
        wsTestString2 = padToWidth(wsTestString1.stripLeading(), WIDTH);
        System.out.println(wsTestString2);

        wsTestString2 = "******************************";
        System.out.println(wsTestString2);
        wsTestString2 = padToWidth(wsTestString1.stripTrailing(), WIDTH);
        System.out.println(wsTestString2);

        System.out.println("--" + "    String literal    " + "--");
        System.out.println("--" + "   String literal    ".trim() + "--");
        System.out.println("--" + "     String literal   ".stripLeading() + "--");
        System.out.println("--" + "   String literal    ".stripTrailing() + "--");
    }

    private static String padToWidth(String s, int width) {
        if (s.length() >= width) {
            return s.substring(0, width);
        }
        return String.format("%-" + width + "s", s);
    }
}
