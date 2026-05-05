package com.coboldemo.numeric;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Java equivalent of {@code comp_test/comp_test.cbl}.
 *
 * <p>The COBOL program declares a {@code PIC 999 COMP} field, doubles
 * its value, and then shows how the same number is rendered when
 * stored as a {@code PIC 999} (zero-padded display) field versus a
 * {@code PIC ZZ9} (zero-suppressed display) field. It finishes by
 * accepting a value from the console and round-tripping it through
 * {@code COMP} again.</p>
 *
 * <p>In COBOL, the {@code USAGE} clause controls how a numeric value
 * is laid out in memory:
 * <ul>
 *   <li>{@code COMP} (a.k.a. {@code COMP-4}, {@code BINARY}): a binary
 *       integer of platform-dependent size, conventionally one of
 *       2 / 4 / 8 bytes. Java's {@code short}, {@code int}, and
 *       {@code long} are the closest analogues.</li>
 *   <li>{@code COMP-3} ("packed decimal"): two decimal digits per byte
 *       plus a nibble for the sign. There is no primitive Java type
 *       for this; {@link BigDecimal} preserves the exact decimal value
 *       without binary rounding errors.</li>
 *   <li>{@code COMP-5}: native binary, identical to {@code COMP} on
 *       most modern platforms.</li>
 * </ul>
 * The {@code PIC} clause is independent: it controls how many decimal
 * digits the value can hold and how it formats when displayed.</p>
 *
 * <p>This program walks through each mapping and prints the
 * representations for direct comparison with the COBOL output.</p>
 */
public class CompTest {

    public static void main(String[] args) {
        // PIC 999 COMP -> a small binary integer. `int` is plenty.
        int compVal = 12;
        compVal = compVal * 2;
        System.out.println("COMP: " + compVal);

        // MOVE ws-comp-val TO ws-disp-val (PIC 999) -> zero-padded width 3.
        String dispVal = String.format("%03d", compVal);
        System.out.println("DISP:" + dispVal);

        // MOVE ws-comp-val TO ws-dyn-disp-val (PIC ZZ9) -> width 3 with
        // leading zero suppression except for the trailing digit.
        String dynaVal = formatPicZZ9(compVal);
        System.out.println("DYNA:" + dynaVal);

        // Show how COBOL USAGE clauses map to Java numeric types.
        showStorageMappings(compVal);

        // ACCEPT ws-input + MOVE ws-input TO ws-comp-val. Skip when no TTY.
        if (System.console() != null) {
            System.out.print("INPUT: ");
            try (Scanner scanner = new Scanner(System.in)) {
                if (scanner.hasNextLine()) {
                    String raw = scanner.nextLine().trim();
                    System.out.println("INPUT: " + raw);
                    try {
                        int parsed = Integer.parseInt(raw);
                        System.out.println("COMP: " + parsed);
                    } catch (NumberFormatException nfe) {
                        System.out.println("COMP: <not a valid integer: " + raw + ">");
                    }
                }
            }
        } else {
            System.out.println("INPUT:  <skipped: no interactive console attached>");
        }
    }

    /**
     * COBOL {@code PIC ZZ9}: width 3 with leading-zero suppression of
     * all but the last digit, so {@code 4} renders as {@code "  4"}
     * and {@code 24} renders as {@code " 24"}.
     */
    private static String formatPicZZ9(int value) {
        return String.format("%3d", value);
    }

    private static void showStorageMappings(int sampleValue) {
        System.out.println();
        System.out.println("COBOL USAGE -> Java type cheat sheet");
        System.out.println("-------------------------------------");

        short asShort = (short) sampleValue;
        System.out.println(String.format(
                "PIC 9(4)  COMP    -> short      (%d bytes)  value=%d",
                Short.BYTES, asShort));

        int asInt = sampleValue;
        System.out.println(String.format(
                "PIC 9(9)  COMP    -> int        (%d bytes)  value=%d",
                Integer.BYTES, asInt));

        long asLong = sampleValue;
        System.out.println(String.format(
                "PIC 9(18) COMP    -> long       (%d bytes)  value=%d",
                Long.BYTES, asLong));

        // COMP-3 (packed decimal) preserves exact decimal precision;
        // BigDecimal is the only standard Java type that can match it.
        BigDecimal asBigDecimal = BigDecimal.valueOf(sampleValue);
        System.out.println(String.format(
                "PIC 9(3)  COMP-3  -> BigDecimal             value=%s, scale=%d, precision=%d",
                asBigDecimal.toPlainString(),
                asBigDecimal.scale(),
                asBigDecimal.precision()));

        BigDecimal scaled = new BigDecimal("123.45");
        System.out.println(String.format(
                "PIC 9(3)V99 COMP-3 -> BigDecimal            value=%s, scale=%d",
                scaled.toPlainString(), scaled.scale()));
    }
}
