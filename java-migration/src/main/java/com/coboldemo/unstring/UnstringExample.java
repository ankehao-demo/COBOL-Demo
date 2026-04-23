package com.coboldemo.unstring;

import java.util.ArrayList;
import java.util.List;

/**
 * Java port of unstring/unstring.cbl.
 *
 * Each numbered example in the original COBOL program is reproduced here as a
 * separate method that operates on the same inputs.
 */
public final class UnstringExample {

    private static final char DELIMITER = '|';

    private UnstringExample() {
    }

    public static void main(String[] args) {
        example1();
        example2();
        example3();
        example4();
        example5();
        example6();
    }

    /** EX 1: simple UNSTRING DELIMITED BY space INTO ws-part-1 ws-part-2. */
    private static void example1() {
        String source = fixedWidth("Hello World", 30);

        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 1 : SIMPLE UNSTRING");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        String[] parts = source.split(" ", 2);
        String part1 = parts.length > 0 ? fixedWidth(parts[0].trim(), 15) : fixedWidth("", 15);
        String part2 = parts.length > 1 ? fixedWidth(parts[1].trim(), 15) : fixedWidth("", 15);
        System.out.println("PART1: " + part1);
        System.out.println("PART2: " + part2);
    }

    /** EX 2: UNSTRING with a pointer, same destination across iterations. */
    private static void example2() {
        String source = fixedWidth("Hello World", 30);
        int pointer = 1;

        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 2 : UNSTRING MULTIPLE TIMES INTO SAME DEST.");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        for (int i = 0; i < 2; i++) {
            UnstringStep step = unstringOne(source, pointer, " ");
            if (step == null) {
                System.out.println("ERROR: OVERFLOW");
                break;
            }
            String part = fixedWidth(step.value, 15);
            // Overflow when we still haven't consumed the full source string.
            boolean overflow = step.nextPointer <= source.length()
                    && !source.substring(step.nextPointer - 1).isBlank();
            if (overflow) {
                System.out.println("ERROR: OVERFLOW");
            } else {
                System.out.println("Successfully unstrung.");
            }
            System.out.println("PART VALUE: " + part);
            System.out.println("POINTER: " + step.nextPointer);
            pointer = step.nextPointer;
        }
    }

    /** EX 3: UNSTRING into two explicit destinations in a single call. */
    private static void example3() {
        String source = fixedWidth("Hello World", 30);
        int pointer = 1;

        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 3 : UNSTRING INTO EXPLICIT FIELDS");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        String[] parts = source.split(" ", 2);
        String part1 = parts.length > 0 ? fixedWidth(parts[0].trim(), 15) : fixedWidth("", 15);
        String part2 = parts.length > 1 ? fixedWidth(parts[1].trim(), 15) : fixedWidth("", 15);
        pointer = source.length() + 1;
        System.out.println("Successfully unstrung.");
        System.out.println("PART1: " + part1);
        System.out.println("PART2: " + part2);
        System.out.println("POINTER: " + pointer);
    }

    /** EX 4: multiple delimiters, single destination, with stats. */
    private static void example4() {
        String source = fixedWidth("A<B<CD>E%FG!HIJ|KL!MN>OP#QR!ST", 30);
        int pointer = 1;
        int fieldsFilled = 0;

        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 4 : UNSTRING WITH MULTIPLE DELIMITERS ");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        while (pointer <= source.length()) {
            UnstringStep step = unstringOne(source, pointer, "<", ">", "!",
                    String.valueOf(DELIMITER));
            if (step == null) {
                break;
            }
            fieldsFilled++;
            String value = fixedWidth(step.value, 5);

            System.out.println();
            System.out.println("VALUE: " + value);
            System.out.println("DELIMITER: " + step.delimiter);
            System.out.println("CHAR COUNT:" + step.value.length());
            System.out.println("CURRENT POINTER: " + step.nextPointer);
            System.out.println("TOTAL FIELDS FILLED: "
                    + String.format("%02d", fieldsFilled));
            System.out.println("-------------------------------------------");
            pointer = step.nextPointer;
        }
    }

    /** EX 5: multiple delimiters, multiple destinations. */
    private static void example5() {
        String source = fixedWidth("A<B<CD>EFG!HIJ|KLMN>O", 30);

        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 5 : UNSTRING WITH MULTIPLE DELIMITERS "
                + "INTO MULTIPLE DESTINATIONS");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        List<UnstringStep> steps = new ArrayList<>();
        int pointer = 1;
        while (pointer <= source.length() && steps.size() < 6) {
            UnstringStep step = unstringOne(source, pointer, "<", ">", "!",
                    String.valueOf(DELIMITER));
            if (step == null) {
                break;
            }
            steps.add(step);
            pointer = step.nextPointer;
        }

        for (int i = 0; i < 6; i++) {
            System.out.println();
            System.out.println("STRING NUMBER: "
                    + String.format("%02d", i + 1));
            if (i < steps.size()) {
                UnstringStep s = steps.get(i);
                System.out.println("VALUE: " + fixedWidth(s.value, 5));
                System.out.println("DELIMITER: " + s.delimiter);
                System.out.println("CHAR COUNT:" + s.value.length());
            } else {
                System.out.println("VALUE: " + fixedWidth("", 5));
                System.out.println("DELIMITER: ");
                System.out.println("CHAR COUNT:0");
            }
            System.out.println("-------------------------------------------");
        }

        System.out.println("TOTALS: ");
        System.out.println("FIELDS FILLED: "
                + String.format("%02d", steps.size()));
    }

    /** EX 6: unstring a formatted number "$123,456.12". */
    private static void example6() {
        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 6 : UNSTRING FORMATTED NUMBER");
        System.out.println();

        String source = "$123,456.12";
        System.out.println("SOURCE VALUE: " + source);

        // Skip the leading '$' to match COBOL's (2:) substring.
        String[] parts = source.substring(1).split("[,.]");
        String[] padded = new String[3];
        for (int i = 0; i < 3; i++) {
            String value = i < parts.length ? parts[i] : "";
            try {
                padded[i] = String.format("%03d",
                        Integer.parseInt(value.trim()));
            } catch (NumberFormatException ex) {
                padded[i] = "000";
            }
        }
        System.out.println("PART 1: " + padded[0]);
        System.out.println("PART 2: " + padded[1]);
        System.out.println("PART 3: " + padded[2]);
        System.out.println();
    }

    /**
     * Single-shot unstring helper. {@code pointer} is 1-based to match COBOL.
     * Returns the extracted value, the delimiter that terminated it, and the
     * updated pointer.
     */
    static UnstringStep unstringOne(String source, int pointer,
                                    String... delimiters) {
        if (pointer > source.length()) {
            return null;
        }
        int startIndex = pointer - 1;
        int earliest = -1;
        String hitDelimiter = "";
        for (String d : delimiters) {
            int idx = source.indexOf(d, startIndex);
            if (idx >= 0 && (earliest == -1 || idx < earliest)) {
                earliest = idx;
                hitDelimiter = d;
            }
        }
        String value;
        int nextPointer;
        if (earliest == -1) {
            value = source.substring(startIndex);
            nextPointer = source.length() + 1;
        } else {
            value = source.substring(startIndex, earliest);
            nextPointer = earliest + hitDelimiter.length() + 1;
        }
        return new UnstringStep(value, hitDelimiter, nextPointer);
    }

    private static String fixedWidth(String value, int width) {
        String v = value == null ? "" : value;
        if (v.length() >= width) {
            return v.substring(0, width);
        }
        return v + " ".repeat(width - v.length());
    }

    /** Result of a single UNSTRING iteration. */
    public static final class UnstringStep {
        public final String value;
        public final String delimiter;
        public final int nextPointer;

        UnstringStep(String value, String delimiter, int nextPointer) {
            this.value = value;
            this.delimiter = delimiter;
            this.nextPointer = nextPointer;
        }
    }
}
