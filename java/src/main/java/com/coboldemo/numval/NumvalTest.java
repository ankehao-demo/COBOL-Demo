package com.coboldemo.numval;

/**
 * Migrated from: numval_test/numval_test.cbl
 * Original author: Erik Eriksen (2022-01-27)
 * Purpose: Testing FUNCTION NUMVAL for converting alphanumeric to numeric.
 *
 * Notes:
 * - COBOL NUMVAL function converts an alphanumeric string to a numeric value.
 * - Java equivalent: Double.parseDouble() or Integer.parseInt() with trim.
 */
public class NumvalTest {

    public static void main(String[] args) {
        // ws-alpha-value PIC X(16) VALUE "42"
        String wsAlphaValue = "42";

        // ws-numeric-value PIC 9(16) VALUE 100
        long wsNumericValue = 100;

        System.out.println("Alpha value:   '" + wsAlphaValue + "'");
        System.out.println("Numeric value: " + wsNumericValue);
        System.out.println();

        // COMPUTE ws-numeric-result = FUNCTION NUMVAL(ws-alpha-value) + ws-numeric-value
        double numvalResult = Double.parseDouble(wsAlphaValue.trim());
        long wsNumericResult = (long) (numvalResult + wsNumericValue);

        System.out.println("NUMVAL('" + wsAlphaValue + "') = " + (long) numvalResult);
        System.out.println("Result of NUMVAL(alpha) + numeric: " + wsNumericResult);
    }
}
