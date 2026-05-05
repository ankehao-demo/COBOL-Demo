package com.coboldemo.isnumeric;

import java.util.Scanner;

/**
 * Migrated from is_numeric/is_numeric.cbl
 * Demonstrates three approaches to checking if input is numeric.
 */
public class IsNumericTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        processPlain(scanner);
        processZeroFill(scanner);
        processTrim(scanner);
        scanner.close();
    }

    /**
     * Plain check: if the raw input (padded to 10 chars) contains spaces,
     * it will not pass a strict numeric test, matching COBOL behavior.
     */
    private static void processPlain(Scanner scanner) {
        System.out.print("(plain) Enter a value: ");
        String input = scanner.nextLine();
        // Pad to 10 characters to match PIC X(10) behavior
        String padded = String.format("%-10s", input);

        if (padded.matches("\\d{10}")) {
            System.out.println(padded + " is numeric!");
        } else {
            System.out.println(padded + " is not numeric.");
        }
    }

    /**
     * Right-justify and zero-fill: right-justify the input within 10 chars,
     * replace leading spaces with '0', then check if numeric.
     */
    private static void processZeroFill(Scanner scanner) {
        System.out.print("(right justify, zero fill) Enter another value: ");
        String input = scanner.nextLine();
        // Right justify to 10 chars (PIC X(10) JUSTIFIED RIGHT)
        String justified = String.format("%10s", input);
        // Replace leading spaces with zeros
        char[] chars = justified.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ' ') {
                chars[i] = '0';
            } else {
                break;
            }
        }
        String zeroFilled = new String(chars);

        if (zeroFilled.matches("\\d{10}")) {
            System.out.println(zeroFilled + " is numeric!");
        } else {
            System.out.println(zeroFilled + " is not numeric.");
        }
    }

    /**
     * Trim: use trim() to remove spaces, then check if numeric.
     */
    private static void processTrim(Scanner scanner) {
        System.out.print("(trim) Enter a third value: ");
        String input = scanner.nextLine();
        String trimmed = input.trim();

        if (!trimmed.isEmpty() && trimmed.matches("\\d+")) {
            System.out.println(trimmed + " is numeric!");
        } else {
            System.out.println(trimmed + " is not numeric.");
        }
    }
}
