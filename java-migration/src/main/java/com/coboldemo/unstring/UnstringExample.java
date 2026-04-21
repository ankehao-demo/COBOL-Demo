package com.coboldemo.unstring;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Port of {@code unstring/unstring.cbl} — covers all six examples from the
 * COBOL source, including multi-delimiter parsing with counts and
 * "fields filled" statistics.
 */
public final class UnstringExample {

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

    /** Simple unstring split by space into two 15-character fields. */
    private static void example1() {
        String source = padRight("Hello World", 30);
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("EX 1 : SIMPLE UNSTRING");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        String[] parts = source.split(" ", 2);
        String part1 = padRight(parts.length > 0 ? parts[0] : "", 15);
        String part2 = padRight(parts.length > 1 ? parts[1].trim() : "", 15);
        System.out.println("PART1: " + part1);
        System.out.println("PART2: " + part2);
    }

    /**
     * Multiple unstrings into the same destination variable via pointer.
     * The COBOL program uses {@code DELIMITED BY ALL SPACES}, which collapses
     * consecutive spaces into a single delimiter. After the second call the
     * pointer lands past the end of the trailing padding and no overflow is
     * reported.
     */
    private static void example2() {
        String source = padRight("Hello World", 30);
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("EX 2 : UNSTRING MULTIPLE TIMES INTO SAME DEST.");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        int pointer = 1; // 1-based, matching COBOL WS-POINTER semantics.
        for (int i = 0; i < 2; i++) {
            UnstringResult result = unstringAll(source, pointer, ' ');
            String part1 = padRight(result.value(), 15);
            if (result.overflow()) {
                System.out.println("ERROR: OVERFLOW");
            } else {
                System.out.println("Successfully unstrung.");
            }
            System.out.println("PART VALUE: " + part1);
            pointer = result.pointer();
            System.out.println("POINTER: " + pointer);
        }
    }

    /** Unstring into explicit fields, pointer ends past source length. */
    private static void example3() {
        String source = padRight("Hello World", 30);
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("EX 3 : UNSTRING INTO EXPLICIT FIELDS");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        String[] parts = source.trim().split("\\s+");
        String part1 = padRight(parts.length > 0 ? parts[0] : "", 15);
        String part2 = padRight(parts.length > 1 ? parts[1] : "", 15);
        int pointer = source.length() + 1; // Past end of data.
        System.out.println("Successfully unstrung.");
        System.out.println("PART1: " + part1);
        System.out.println("PART2: " + part2);
        System.out.println("POINTER: " + pointer);
    }

    /** Multiple delimiters with counts and tallying statistics. */
    private static void example4() {
        String source = "A<B<CD>E%FG!HIJ|KL!MN>OP#QR!ST";
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("EX 4 : UNSTRING WITH MULTIPLE DELIMITERS ");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        Pattern delimiters = Pattern.compile("<+|[>!|]");
        Matcher matcher = delimiters.matcher(source);

        int pointer = 0;
        int fieldsFilled = 0;
        int lastEnd = 0;
        while (matcher.find()) {
            String value = source.substring(lastEnd, matcher.start());
            String delim = matcher.group().substring(0, 1); // All-<: use one
            emitField(value, delim, ++fieldsFilled, matcher.end() + 1);
            lastEnd = matcher.end();
            pointer = matcher.end() + 1;
        }
        if (lastEnd < source.length()) {
            String value = source.substring(lastEnd);
            emitField(value, " ", ++fieldsFilled, source.length() + 1);
        }
    }

    private static void emitField(String value, String delim, int fieldsFilled, int pointer) {
        String padded = padRight(value, 5);
        System.out.println();
        System.out.println("VALUE: " + padded);
        System.out.println("DELIMITER: " + delim);
        System.out.println("CHAR COUNT:" + value.length());
        System.out.println("CURRENT POINTER: " + pointer);
        System.out.println("TOTAL FIELDS FILLED: " + fieldsFilled);
        System.out.println("-------------------------------------------");
    }

    /** Multiple delimiters into multiple destination strings. */
    private static void example5() {
        String source = "A<B<CD>EFG!HIJ|KLMN>O";
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("EX 5 : UNSTRING WITH MULTIPLE DELIMITERS INTO MULTIPLE DESTINATIONS");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        String[] values = new String[6];
        String[] delims = new String[6];
        int[] counts = new int[6];
        for (int i = 0; i < 6; i++) {
            values[i] = "";
            delims[i] = " ";
        }

        Pattern delimiters = Pattern.compile("<+|>+|!|\\|");
        Matcher matcher = delimiters.matcher(source);
        int idx = 0;
        int lastEnd = 0;
        while (matcher.find() && idx < 6) {
            values[idx] = source.substring(lastEnd, matcher.start());
            delims[idx] = matcher.group().substring(0, 1);
            counts[idx] = values[idx].length();
            idx++;
            lastEnd = matcher.end();
        }
        if (idx < 6 && lastEnd < source.length()) {
            values[idx] = source.substring(lastEnd);
            delims[idx] = " ";
            counts[idx] = values[idx].length();
            idx++;
        }

        for (int i = 0; i < 6; i++) {
            System.out.println();
            System.out.println("STRING NUMBER: " + String.format("%02d", i + 1));
            System.out.println("VALUE: " + padRight(values[i], 5));
            System.out.println("DELIMITER: " + delims[i]);
            System.out.println("CHAR COUNT:" + counts[i]);
            System.out.println("-------------------------------------------");
        }

        System.out.println("TOTALS: ");
        System.out.println("FIELDS FILLED: " + String.format("%02d", idx));
    }

    /** Unstring a currency-formatted number by comma and period. */
    private static void example6() {
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("EX 6 : UNSTRING FORMATTED NUMBER");
        System.out.println();

        String source = "$123,456.12";
        System.out.println("SOURCE VALUE: " + source);

        String withoutCurrency = source.substring(1); // Skip '$'.
        String[] parts = withoutCurrency.split("[,.]");
        String p1 = parts.length > 0 ? parts[0] : "";
        String p2 = parts.length > 1 ? parts[1] : "";
        String p3 = parts.length > 2 ? parts[2] : "";
        System.out.println("PART 1: " + String.format("%03d", parseIntSafe(p1)));
        System.out.println("PART 2: " + String.format("%03d", parseIntSafe(p2)));
        System.out.println("PART 3: " + String.format("%03d", parseIntSafe(p3)));
        System.out.println();
    }

    private static int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private record UnstringResult(String value, int pointer, boolean overflow) {
    }

    /**
     * Mirrors COBOL {@code UNSTRING ... DELIMITED BY ALL <delim>}: scans for
     * the first occurrence of {@code delim}, consumes every consecutive
     * occurrence after it, and returns a 1-based pointer to the next unread
     * character (or {@code source.length() + 1} when the source is exhausted).
     * Overflow is true only when more unread source data remains.
     */
    private static UnstringResult unstringAll(String source, int start1Based, char delim) {
        int start = Math.max(0, start1Based - 1);
        if (start >= source.length()) {
            return new UnstringResult("", source.length() + 1, false);
        }
        int idx = source.indexOf(delim, start);
        if (idx < 0) {
            return new UnstringResult(source.substring(start), source.length() + 1, false);
        }
        String value = source.substring(start, idx);
        int next = idx;
        while (next < source.length() && source.charAt(next) == delim) {
            next++;
        }
        int pointer1Based = next + 1;
        boolean overflow = pointer1Based <= source.length();
        return new UnstringResult(value, pointer1Based, overflow);
    }

    private static String padRight(String s, int width) {
        if (s.length() >= width) {
            return s.substring(0, width);
        }
        return s + " ".repeat(width - s.length());
    }
}
