package com.coboldemo.trim;

/**
 * Java migration of trim/trim.cbl
 *
 * COBOL-to-Java mapping:
 *   FUNCTION TRIM(x)          -> x.trim()
 *   FUNCTION TRIM(x LEADING)  -> x.stripLeading()   (Java 11+)
 *   FUNCTION TRIM(x TRAILING) -> x.stripTrailing()   (Java 11+)
 *
 *   PIC X(30) field           -> Fixed-width 30-char string, padded with spaces on the right.
 *   MOVE trimmed TO pic-field -> String.format("%-30s", trimmed) to left-justify and pad.
 *
 * Original author: Erik Eriksen (COBOL version)
 */
public class TrimTest {

    /** Width of the COBOL PIC X(30) fields. */
    private static final int FIELD_WIDTH = 30;

    /**
     * Emulates COBOL MOVE of a value into a PIC X(30) field:
     * left-justify the source and pad (or truncate) to exactly 30 characters.
     */
    private static String moveToPicX30(String value) {
        if (value.length() >= FIELD_WIDTH) {
            return value.substring(0, FIELD_WIDTH);
        }
        return String.format("%-" + FIELD_WIDTH + "s", value);
    }

    public static void main(String[] args) {

        // COBOL: 01 ws-test-string-1 pic x(30) value "    hello world       ".
        // The literal is 22 chars; PIC X(30) pads it to 30 with trailing spaces.
        String wsTestString1 = moveToPicX30("    hello world       ");

        // COBOL: 01 ws-test-string-2 pic x(30).
        String wsTestString2;

        // --- Display raw and trimmed variants of ws-test-string-1 ---

        // COBOL: display "--" ws-test-string-1 "--"
        System.out.println("--" + wsTestString1 + "--");

        // COBOL: display "--" function trim(ws-test-string-1) "--"
        System.out.println("--" + wsTestString1.trim() + "--");

        // COBOL: display "--" function trim(ws-test-string-1 leading) "--"
        System.out.println("--" + wsTestString1.stripLeading() + "--");

        // COBOL: display "--" function trim(ws-test-string-1 trailing) "--"
        System.out.println("--" + wsTestString1.stripTrailing() + "--");

        // --- Asterisk fill / move demonstrations ---

        // COBOL: move "******************************" to ws-test-string-2
        wsTestString2 = "******************************";
        System.out.println(wsTestString2);

        // COBOL: move ws-test-string-1 to ws-test-string-2
        wsTestString2 = moveToPicX30(wsTestString1);
        System.out.println(wsTestString2);

        // Full trim -> move to PIC X(30)
        wsTestString2 = "******************************";
        System.out.println(wsTestString2);

        // COBOL: move function trim(ws-test-string-1) to ws-test-string-2
        wsTestString2 = moveToPicX30(wsTestString1.trim());
        System.out.println(wsTestString2);

        // Leading trim -> move to PIC X(30)
        wsTestString2 = "******************************";
        System.out.println(wsTestString2);

        // COBOL: move function trim(ws-test-string-1 leading) to ws-test-string-2
        wsTestString2 = moveToPicX30(wsTestString1.stripLeading());
        System.out.println(wsTestString2);

        // Trailing trim -> move to PIC X(30)
        wsTestString2 = "******************************";
        System.out.println(wsTestString2);

        // COBOL: move function trim(ws-test-string-1 trailing) to ws-test-string-2
        wsTestString2 = moveToPicX30(wsTestString1.stripTrailing());
        System.out.println(wsTestString2);

        // --- String literal trim demonstrations ---

        // COBOL: display "--" "    String literal    " "--"
        System.out.println("--" + "    String literal    " + "--");

        // COBOL: display "--" function trim("   String literal    ") "--"
        System.out.println("--" + "   String literal    ".trim() + "--");

        // COBOL: display "--" function trim("     String literal   " leading) "--"
        System.out.println("--" + "     String literal   ".stripLeading() + "--");

        // COBOL: display "--" function trim("   String literal    " trailing) "--"
        System.out.println("--" + "   String literal    ".stripTrailing() + "--");
    }
}
