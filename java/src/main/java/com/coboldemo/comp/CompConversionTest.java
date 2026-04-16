package com.coboldemo.comp;

/**
 * Migrated from: comp_test/comp_test.cbl
 *
 * COBOL-to-Java mapping:
 *   PIC 9(4) COMP  -> int (unsigned binary, fits in int)
 *   PIC S9(8) COMP -> int (signed binary, fits in int)
 *   PIC 9(8)       -> int (display numeric, used for formatted output)
 *   PIC S9(8)      -> int (signed display numeric)
 *
 * COBOL COMP (USAGE COMPUTATIONAL) stores values in binary.
 * Java int is a 32-bit signed integer, which covers both
 * PIC 9(4) COMP (max 9999) and PIC S9(8) COMP (max +/-99999999).
 *
 * Display formatting: COBOL PIC 9(8) zero-fills to 8 digits;
 * we replicate this with String.format("%08d", value).
 */
public class CompConversionTest {

    public static void main(String[] args) {

        // ----- Comp Test 1 -----
        // COBOL: 01 ws-comp-test
        //   05 ws-comp-value-1  PIC 9(4) COMP VALUE 1234.
        //   05 ws-comp-value-2  PIC 9(4) COMP VALUE 4321.
        //   05 ws-comp-value-disp PIC 9(8).
        int wsCompValue1 = 1234;
        int wsCompValue2 = 4321;
        int wsCompValueDisp;

        System.out.println("Comp Test 1");
        // COBOL DISPLAY of a COMP field shows the numeric value
        System.out.println("ws-comp-value-1: " + wsCompValue1);
        System.out.println("ws-comp-value-2: " + wsCompValue2);

        // COMPUTE ws-comp-value-disp = ws-comp-value-1 + ws-comp-value-2
        wsCompValueDisp = wsCompValue1 + wsCompValue2;
        // PIC 9(8) display: zero-filled to 8 digits
        System.out.println("SUM: " + String.format("%08d", wsCompValueDisp));

        // COMPUTE ws-comp-value-disp = ws-comp-value-1 * ws-comp-value-2
        wsCompValueDisp = wsCompValue1 * wsCompValue2;
        System.out.println("MULT: " + String.format("%08d", wsCompValueDisp));

        // ----- Comp Test 2 -----
        // COBOL: 01 ws-comp-test-2
        //   05 ws-comp-value-3  PIC S9(8) COMP.
        //   05 ws-comp-value-4  PIC S9(8) COMP.
        //   05 ws-comp-value-disp-2 PIC S9(8).
        int wsCompValue3;
        int wsCompValue4;
        int wsCompValueDisp2;

        System.out.println("Comp Test 2");

        // MOVE -1234 TO ws-comp-value-3
        wsCompValue3 = -1234;
        // MOVE 4321 TO ws-comp-value-4
        wsCompValue4 = 4321;

        // COBOL DISPLAY of PIC S9(8) COMP shows the signed value
        System.out.println("ws-comp-value-3: " + wsCompValue3);
        System.out.println("ws-comp-value-4: " + wsCompValue4);

        // COMPUTE ws-comp-value-disp-2 = ws-comp-value-3 + ws-comp-value-4
        wsCompValueDisp2 = wsCompValue3 + wsCompValue4;
        // PIC S9(8) display: signed, zero-filled to 8 digits
        System.out.println("SUM: " + formatSigned8(wsCompValueDisp2));

        // COMPUTE ws-comp-value-disp-2 = ws-comp-value-3 * ws-comp-value-4
        wsCompValueDisp2 = wsCompValue3 * wsCompValue4;
        System.out.println("MULT: " + formatSigned8(wsCompValueDisp2));
    }

    /**
     * Formats an integer as a signed 8-digit display value,
     * mimicking COBOL PIC S9(8) display behavior.
     * Negative values show a leading minus sign followed by zero-filled digits.
     * Positive values are zero-filled to 8 digits.
     */
    private static String formatSigned8(int value) {
        if (value < 0) {
            return "-" + String.format("%08d", Math.abs(value));
        }
        return String.format("%08d", value);
    }
}
