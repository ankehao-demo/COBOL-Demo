package com.coboldemo.isnumeric;

import java.util.Scanner;

/**
 * Migrated from: is_numeric/is_numeric.cbl
 * Original author: Erik Eriksen (2021-09-28, updated 2022-01-27)
 * Purpose: Tests numeric validation with three approaches:
 *   1. Plain IS NUMERIC check
 *   2. Right-justify with zero-fill
 *   3. TRIM before check
 */
public class IsNumericTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("IS NUMERIC Test");
        System.out.println("===============");
        System.out.println();

        // Test 1: Plain IS NUMERIC check
        System.out.println("Test 1: Plain IS NUMERIC check");
        System.out.println("-------------------------------");
        System.out.print("Enter a value: ");
        String input = scanner.nextLine();
        // Pad to 16 chars (like COBOL PIC X(16))
        String wsInput = String.format("%-16s", input);

        if (isNumeric(wsInput)) {
            System.out.println("'" + wsInput + "' IS NUMERIC");
        } else {
            System.out.println("'" + wsInput + "' IS NOT NUMERIC");
        }
        System.out.println();

        // Test 2: Right-justify with zero-fill
        System.out.println("Test 2: Right-justify with zero-fill");
        System.out.println("------------------------------------");
        System.out.print("Enter a value: ");
        input = scanner.nextLine();
        wsInput = String.format("%-16s", input);

        // Right-justify: strip spaces, then right-justify in 16-char field with zero fill
        String trimmed = wsInput.trim();
        String rightJustified;
        if (trimmed.isEmpty()) {
            rightJustified = "0".repeat(16);
        } else {
            rightJustified = String.format("%16s", trimmed).replace(' ', '0');
        }

        if (isNumeric(rightJustified)) {
            System.out.println("'" + rightJustified + "' IS NUMERIC");
        } else {
            System.out.println("'" + rightJustified + "' IS NOT NUMERIC");
        }
        System.out.println();

        // Test 3: TRIM before check
        System.out.println("Test 3: TRIM before check");
        System.out.println("-------------------------");
        System.out.print("Enter a value: ");
        input = scanner.nextLine();
        wsInput = String.format("%-16s", input);

        String trimmedInput = wsInput.trim();
        if (isNumeric(trimmedInput)) {
            System.out.println("'" + trimmedInput + "' IS NUMERIC");
        } else {
            System.out.println("'" + trimmedInput + "' IS NOT NUMERIC");
        }
    }

    /**
     * Check if a string is numeric (contains only digits, optionally with sign and decimal).
     * This mirrors COBOL's IS NUMERIC check which only allows digits in PIC X fields.
     */
    private static boolean isNumeric(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        return value.chars().allMatch(Character::isDigit);
    }
}
