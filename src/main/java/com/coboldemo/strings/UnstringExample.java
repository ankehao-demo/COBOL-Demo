package com.coboldemo.strings;

import java.util.ArrayList;
import java.util.List;

/**
 * Migrated from: unstring/unstring.cbl
 *
 * Demonstrates various forms of the COBOL UNSTRING verb for splitting
 * strings with delimiters, pointer tracking, delimiter capture, count
 * tracking, and tallying. Implements a custom unstring() utility since
 * Java's String.split() doesn't provide delimiter info or position tracking.
 */
public class UnstringExample {

    /** Result of a single unstring field extraction */
    public static class UnstringFieldResult {
        public String value;
        public String delimiter;
        public int charCount;

        public UnstringFieldResult(String value, String delimiter, int charCount) {
            this.value = value;
            this.delimiter = delimiter;
            this.charCount = charCount;
        }
    }

    /** Mutable pointer wrapper for tracking position */
    public static class Pointer {
        public int value;

        public Pointer(int value) {
            this.value = value;
        }
    }

    public static void main(String[] args) {
        // EXAMPLE 1: Simple unstring
        String sourceStr = padRight("Hello World", 30);
        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 1 : SIMPLE UNSTRING");
        System.out.println();
        System.out.println("SOURCE STRING: " + sourceStr);

        String[] parts = sourceStr.split(" ", 2);
        String part1 = parts.length > 0 ? padRight(parts[0], 15) : padRight("", 15);
        String part2 = parts.length > 1 ? padRight(parts[1], 15) : padRight("", 15);
        System.out.println("PART1: " + part1);
        System.out.println("PART2: " + part2);

        // EXAMPLE 2: Unstring multiple times into same destination with pointer
        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 2 : UNSTRING MULTIPLE TIMES INTO SAME DEST.");
        System.out.println();
        System.out.println("SOURCE STRING: " + sourceStr);

        Pointer pointer = new Pointer(0);
        String[] delimiters = {" "};
        boolean useAllSpaces = true;

        for (int i = 0; i < 2; i++) {
            UnstringFieldResult result = unstringOne(sourceStr, delimiters, useAllSpaces, pointer);
            if (i == 0 && pointer.value < sourceStr.length()) {
                System.out.println("ERROR: OVERFLOW");
            } else {
                System.out.println("Successfully unstrung.");
            }
            System.out.println("PART VALUE: " + padRight(result.value, 15));
            System.out.println("POINTER: " + (pointer.value + 1)); // 1-based for display
        }

        // EXAMPLE 3: Unstring into explicit fields
        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 3 : UNSTRING INTO EXPLICIT FIELDS");
        pointer = new Pointer(0);
        System.out.println();
        System.out.println("SOURCE STRING: " + sourceStr);

        List<UnstringFieldResult> results = unstringMultiple(sourceStr, delimiters, useAllSpaces, pointer, 2);
        boolean overflow = pointer.value < sourceStr.stripTrailing().length();
        if (overflow) {
            System.out.println("ERROR: OVERFLOW");
        } else {
            System.out.println("Successfully unstrung.");
        }
        System.out.println("PART1: " + padRight(results.get(0).value, 15));
        System.out.println("PART2: " + padRight(results.get(1).value, 15));
        System.out.println("POINTER: " + (pointer.value + 1));

        // EXAMPLE 4: Unstring with multiple delimiters, single destination per iteration
        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 4 : UNSTRING WITH MULTIPLE DELIMITERS ");
        sourceStr = padRight("A<B<CD>E%FG!HIJ|KL!MN>OP#QR!ST", 30);
        System.out.println();
        System.out.println("SOURCE STRING: " + sourceStr);

        pointer = new Pointer(0);
        String[] multiDelims = {"<", ">", "!", "|"};
        int totalFieldsFilled = 0;

        while (pointer.value < sourceStr.length()) {
            UnstringFieldResult r = unstringOne(sourceStr, multiDelims, false, pointer);
            totalFieldsFilled++;
            System.out.println();
            System.out.println("VALUE: " + padRight(r.value, 5));
            System.out.println("DELIMITER: " + r.delimiter);
            System.out.println("CHAR COUNT:" + r.charCount);
            System.out.println("CURRENT POINTER: " + (pointer.value + 1));
            System.out.println("TOTAL FIELDS FILLED: " + String.format("%02d", totalFieldsFilled));
            System.out.println("-------------------------------------------");
        }

        // EXAMPLE 5: Unstring with multiple delimiters into multiple destinations
        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 5 : UNSTRING WITH MULTIPLE DELIMITERS INTO MULTIPLE DESTINATIONS");
        sourceStr = padRight("A<B<CD>EFG!HIJ|KLMN>O", 30);
        System.out.println();
        System.out.println("SOURCE STRING: " + sourceStr);

        pointer = new Pointer(0);
        results = unstringMultiple(sourceStr, multiDelims, false, pointer, 6);

        for (int i = 0; i < results.size(); i++) {
            UnstringFieldResult r = results.get(i);
            System.out.println();
            System.out.println("STRING NUMBER: " + (i + 1));
            System.out.println("VALUE: " + padRight(r.value, 5));
            System.out.println("DELIMITER: " + r.delimiter);
            System.out.println("CHAR COUNT:" + r.charCount);
            System.out.println("-------------------------------------------");
        }
        System.out.println("TOTALS: ");
        System.out.println("FIELDS FILLED: " + String.format("%02d", results.size()));

        // EXAMPLE 6: Unstring formatted number
        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 6 : UNSTRING FORMATTED NUMBER");
        System.out.println();

        // PIC $999,999.99 with value 123456.12 -> "$123,456.12"
        String sourceNum = String.format("$%,10.2f", 123456.12);
        System.out.println("SOURCE VALUE: " + sourceNum);

        // Skip the '$' (start at position 1) and split by ',' or '.'
        String numPart = sourceNum.substring(1).trim();
        String[] numParts = numPart.split("[,.]");
        for (int i = 0; i < numParts.length && i < 3; i++) {
            System.out.println("PART " + (i + 1) + ": " + String.format("%03d", Integer.parseInt(numParts[i].trim())));
        }
        System.out.println();
    }

    /**
     * Extracts a single field from the source string starting at pointer position.
     * Scans for the first matching delimiter and returns the text before it.
     */
    public static UnstringFieldResult unstringOne(String source, String[] delimiters,
                                                   boolean consumeAllDelims, Pointer pointer) {
        int start = pointer.value;
        int bestPos = source.length();
        String foundDelim = "";

        // Find the nearest delimiter
        for (String d : delimiters) {
            int pos = source.indexOf(d, start);
            if (pos >= 0 && pos < bestPos) {
                bestPos = pos;
                foundDelim = d;
            }
        }

        String value;
        if (bestPos < source.length()) {
            value = source.substring(start, bestPos);
            pointer.value = bestPos + foundDelim.length();

            // If consumeAllDelims, skip consecutive occurrences of the same delimiter
            if (consumeAllDelims) {
                while (pointer.value < source.length() &&
                        source.startsWith(foundDelim, pointer.value)) {
                    pointer.value += foundDelim.length();
                }
            }
        } else {
            value = source.substring(start);
            pointer.value = source.length();
            foundDelim = "";
        }

        return new UnstringFieldResult(value, foundDelim, value.length());
    }

    /**
     * Extracts multiple fields from the source string.
     */
    public static List<UnstringFieldResult> unstringMultiple(String source, String[] delimiters,
                                                              boolean consumeAllDelims, Pointer pointer, int maxFields) {
        List<UnstringFieldResult> results = new ArrayList<>();
        for (int i = 0; i < maxFields && pointer.value < source.length(); i++) {
            results.add(unstringOne(source, delimiters, consumeAllDelims, pointer));
        }
        // Pad with empty results if needed
        while (results.size() < maxFields) {
            results.add(new UnstringFieldResult("", "", 0));
        }
        return results;
    }

    static String padRight(String s, int len) {
        if (s == null) s = "";
        if (s.length() >= len) return s.substring(0, len);
        return String.format("%-" + len + "s", s);
    }
}
