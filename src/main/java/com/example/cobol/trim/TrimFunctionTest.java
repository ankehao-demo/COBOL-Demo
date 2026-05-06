package com.example.cobol.trim;

/**
 * Java port of {@code trim/trim.cbl}.
 *
 * <p>Demonstrates string trimming behaviour. COBOL has
 * {@code FUNCTION TRIM(value)}, {@code FUNCTION TRIM(value LEADING)} and
 * {@code FUNCTION TRIM(value TRAILING)}. Java's String class has direct
 * equivalents: {@link String#strip()},
 * {@link String#stripLeading()}, and {@link String#stripTrailing()}.
 */
public final class TrimFunctionTest {

    private TrimFunctionTest() {}

    public static void main(String[] args) {
        // 30-char fixed-width PIC X(30) value padded with trailing spaces.
        String wsTestString1 = String.format("%-30s", "    hello world       ");

        System.out.println("--" + wsTestString1 + "--");
        System.out.println("--" + wsTestString1.strip() + "--");
        System.out.println("--" + wsTestString1.stripLeading() + "--");
        System.out.println("--" + wsTestString1.stripTrailing() + "--");

        String wsTestString2;

        wsTestString2 = "******************************";
        System.out.println(wsTestString2);
        wsTestString2 = wsTestString1;
        System.out.println(wsTestString2);

        wsTestString2 = "******************************";
        System.out.println(wsTestString2);
        wsTestString2 = wsTestString1.strip();
        System.out.println(wsTestString2);

        wsTestString2 = "******************************";
        System.out.println(wsTestString2);
        wsTestString2 = wsTestString1.stripLeading();
        System.out.println(wsTestString2);

        wsTestString2 = "******************************";
        System.out.println(wsTestString2);
        wsTestString2 = wsTestString1.stripTrailing();
        System.out.println(wsTestString2);

        System.out.println("--" + "    String literal    " + "--");
        System.out.println("--" + "   String literal    ".strip() + "--");
        System.out.println("--" + "     String literal   ".stripLeading() + "--");
        System.out.println("--" + "   String literal    ".stripTrailing() + "--");
    }
}
