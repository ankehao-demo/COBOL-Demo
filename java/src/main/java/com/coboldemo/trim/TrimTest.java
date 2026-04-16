package com.coboldemo.trim;

/**
 * Migrated from: trim/trim.cbl
 * Original author: Erik Eriksen (2022-01-23)
 * Purpose: Tests FUNCTION TRIM with LEADING and TRAILING variants.
 *
 * Mapping:
 * - FUNCTION TRIM(x)          -> x.trim()
 * - FUNCTION TRIM(x LEADING)  -> x.stripLeading()  (Java 11+)
 * - FUNCTION TRIM(x TRAILING) -> x.stripTrailing() (Java 11+)
 */
public class TrimTest {

    public static void main(String[] args) {
        // ws-test-value PIC X(32) VALUE "   Hello World   "
        String wsTestValue = "   Hello World   ";

        System.out.println("TRIM Test");
        System.out.println("=========");
        System.out.println();

        // Direct display of trim results
        System.out.println("Original value: '" + wsTestValue + "'");
        System.out.println();

        System.out.println("FUNCTION TRIM (both):     '" + wsTestValue.trim() + "'");
        System.out.println("FUNCTION TRIM (leading):  '" + wsTestValue.stripLeading() + "'");
        System.out.println("FUNCTION TRIM (trailing): '" + wsTestValue.stripTrailing() + "'");
        System.out.println();

        // Move trimmed values to display variables (PIC X(32))
        String wsTrimBoth = String.format("%-32s", wsTestValue.trim());
        String wsTrimLeading = String.format("%-32s", wsTestValue.stripLeading());
        String wsTrimTrailing = String.format("%-32s", wsTestValue.stripTrailing());

        System.out.println("Stored in PIC X(32) variables:");
        System.out.println("TRIM (both):     '" + wsTrimBoth + "'");
        System.out.println("TRIM (leading):  '" + wsTrimLeading + "'");
        System.out.println("TRIM (trailing): '" + wsTrimTrailing + "'");
    }
}
