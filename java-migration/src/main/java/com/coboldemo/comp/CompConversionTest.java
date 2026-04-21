package com.coboldemo.comp;

import java.util.Scanner;

/**
 * Port of {@code comp_test/comp_test.cbl} — demonstrates converting between
 * COBOL {@code COMP} (packed / binary) and display (character) representations.
 * In Java primitives there is no "display" vs "comp" distinction, so we use
 * {@code int} for the numeric value and {@code String.format} to emulate the
 * PIC ZZ9-style edited output.
 */
public final class CompConversionTest {

    private CompConversionTest() {
    }

    public static void main(String[] args) {
        int compVal = 12;
        compVal = compVal * 2;
        System.out.println("COMP: " + compVal);

        // PIC 999 — always three digits, zero-padded.
        String dispVal = String.format("%03d", compVal);
        System.out.println("DISP:" + dispVal);

        // PIC ZZ9 — three digits with leading zero suppression.
        String dynDispVal = String.format("%3d", compVal);
        System.out.println("DYNA:" + dynDispVal);

        System.out.print("INPUT: ");
        Scanner scanner = new Scanner(System.in);
        int input = 0;
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                try {
                    input = Integer.parseInt(line);
                } catch (NumberFormatException ignored) {
                    // Leave input at 0, matching COBOL default for numeric fields.
                }
            }
        }

        System.out.println("INPUT: " + String.format("%03d", input));

        compVal = input;
        System.out.println("COMP: " + compVal);
    }
}
