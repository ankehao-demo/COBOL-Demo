package com.coboldemo.unstring;

/**
 * Java port of {@code unstring/unstring.cbl}.
 *
 * Reproduces the six UNSTRING examples from the COBOL original. The
 * COBOL UNSTRING verb is implemented here with a small helper that
 * scans the source string from a "pointer" position and yields the next
 * (value, delimiter, charCount) tuple, mirroring the COBOL semantics for
 * {@code DELIMITER IN}, {@code COUNT IN}, {@code WITH POINTER}, and
 * {@code TALLYING IN}.
 */
public class UnstringExample {

    private static final int PART_WIDTH = 15;
    private static final int SINGLE_DEST_WIDTH = 5;

    /** Result of one UNSTRING step. */
    private static final class UnstrungField {
        final String value;
        final char delimiter;
        final int charCount;
        final int newPointer;

        UnstrungField(String value, char delimiter, int charCount, int newPointer) {
            this.value = value;
            this.delimiter = delimiter;
            this.charCount = charCount;
            this.newPointer = newPointer;
        }
    }

    public static void main(String[] args) {
        example1();
        example2();
        example3();
        example4();
        example5();
        example6();
    }

    /** EX 1: simple unstring on space delimiter. */
    private static void example1() {
        String source = padTo("Hello World", 30);
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("EX 1 : SIMPLE UNSTRING");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        String[] parts = source.trim().split(" ", 2);
        String part1 = padTo(parts.length > 0 ? parts[0] : "", PART_WIDTH);
        String part2 = padTo(parts.length > 1 ? parts[1] : "", PART_WIDTH);
        System.out.println("PART1: " + part1);
        System.out.println("PART2: " + part2);
    }

    /** EX 2: UNSTRING the same destination twice with WITH POINTER. */
    private static void example2() {
        String source = padTo("Hello World", 30);
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("EX 2 : UNSTRING MULTIPLE TIMES INTO SAME DEST.");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        int pointer = 1;
        String part1 = padTo("", PART_WIDTH);
        for (int call = 0; call < 2; call++) {
            UnstrungField f = unstringNext(source, pointer, " ", true);
            part1 = padTo(f.value, PART_WIDTH);
            pointer = f.newPointer;
            // The first call still has data to consume so the COBOL ON OVERFLOW
            // branch fires; on the second call the source is exhausted.
            if (pointer <= source.length()) {
                System.out.println("ERROR: OVERFLOW");
            } else {
                System.out.println("Successfully unstrung.");
            }
            System.out.println("PART VALUE: " + part1);
            System.out.println("POINTER: " + String.format("%05d", pointer));
        }
    }

    /** EX 3: UNSTRING into two explicit fields, no overflow. */
    private static void example3() {
        String source = padTo("Hello World", 30);
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("EX 3 : UNSTRING INTO EXPLICIT FIELDS");

        int pointer = 1;
        UnstrungField f1 = unstringNext(source, pointer, " ", true);
        pointer = f1.newPointer;
        UnstrungField f2 = unstringNext(source, pointer, " ", true);
        pointer = f2.newPointer;

        System.out.println();
        System.out.println("SOURCE STRING: " + source);
        // No overflow because we have enough destination fields.
        System.out.println("Successfully unstrung.");
        System.out.println("PART1: " + padTo(f1.value, PART_WIDTH));
        System.out.println("PART2: " + padTo(f2.value, PART_WIDTH));
        System.out.println("POINTER: " + String.format("%05d", pointer));
    }

    /** EX 4: UNSTRING with multiple delimiters in a loop. */
    private static void example4() {
        String source = padTo("A<B<CD>E%FG!HIJ|KL!MN>OP#QR!ST", 30);
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("EX 4 : UNSTRING WITH MULTIPLE DELIMITERS ");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        int pointer = 1;
        int totalFields = 0;
        String delims = "<>!|";
        while (pointer <= source.length()) {
            UnstrungField f = unstringNext(source, pointer, delims, true);
            String value = padTo(f.value, SINGLE_DEST_WIDTH);
            char delimiter = f.delimiter;
            pointer = f.newPointer;
            totalFields++;

            System.out.println();
            System.out.println("VALUE: " + value);
            System.out.println("DELIMITER: " + delimiter);
            System.out.println("CHAR COUNT:" + f.charCount);
            System.out.println("CURRENT POINTER: " + String.format("%05d", pointer));
            System.out.println("TOTAL FIELDS FILLED: " + String.format("%02d", totalFields));
            System.out.println("-------------------------------------------");
        }
    }

    /** EX 5: UNSTRING with multiple delimiters into multiple destinations. */
    private static void example5() {
        String source = padTo("A<B<CD>EFG!HIJ|KLMN>O", 30);
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("EX 5 : UNSTRING WITH MULTIPLE DELIMITERS "
                + "INTO MULTIPLE DESTINATIONS");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        String[] dest = new String[6];
        char[] delim = new char[6];
        int[] count = new int[6];
        int pointer = 1;
        int filled = 0;
        for (int i = 0; i < dest.length && pointer <= source.length(); i++) {
            UnstrungField f = unstringNext(source, pointer, "<>!|", true);
            dest[i] = padTo(f.value, SINGLE_DEST_WIDTH);
            delim[i] = f.delimiter;
            count[i] = f.charCount;
            pointer = f.newPointer;
            filled++;
        }
        for (int i = 0; i < dest.length; i++) {
            System.out.println();
            System.out.println("STRING NUMBER: " + String.format("%05d", i + 1));
            System.out.println("VALUE: " + (dest[i] == null ? padTo("", SINGLE_DEST_WIDTH) : dest[i]));
            System.out.println("DELIMITER: " + (delim[i] == 0 ? ' ' : delim[i]));
            System.out.println("CHAR COUNT:" + count[i]);
            System.out.println("-------------------------------------------");
        }

        System.out.println("TOTALS: ");
        System.out.println("FIELDS FILLED: " + String.format("%02d", filled));
    }

    /** EX 6: UNSTRING a formatted currency value into 3 numeric parts. */
    private static void example6() {
        String formatted = "$123,456.12";
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("EX 6 : UNSTRING FORMATTED NUMBER");
        System.out.println();
        System.out.println("SOURCE VALUE: " + formatted);

        String stripped = formatted.substring(1); // skip leading '$'
        String[] parts = stripped.split("[,.]");
        String[] padded = new String[3];
        for (int i = 0; i < padded.length; i++) {
            padded[i] = i < parts.length ? padThreeDigits(parts[i]) : "000";
        }
        System.out.println("PART 1: " + padded[0]);
        System.out.println("PART 2: " + padded[1]);
        System.out.println("PART 3: " + padded[2]);
        System.out.println();
    }

    /**
     * Approximation of UNSTRING that returns the next field starting at
     * {@code pointer1Based}. Delimiter chars are any character in
     * {@code delimChars}. {@code allDelims} = true mirrors COBOL
     * {@code DELIMITED BY ALL <chars>} which collapses runs of the same
     * delimiter into one boundary.
     */
    private static UnstrungField unstringNext(
            String source, int pointer1Based, String delimChars, boolean allDelims) {
        int idx = pointer1Based - 1;
        if (idx >= source.length()) {
            return new UnstrungField("", ' ', 0, source.length() + 1);
        }
        StringBuilder sb = new StringBuilder();
        while (idx < source.length() && delimChars.indexOf(source.charAt(idx)) < 0) {
            sb.append(source.charAt(idx));
            idx++;
        }
        char delimiter = ' ';
        if (idx < source.length()) {
            delimiter = source.charAt(idx);
            idx++;
            if (allDelims) {
                while (idx < source.length() && source.charAt(idx) == delimiter) {
                    idx++;
                }
            }
        }
        return new UnstrungField(sb.toString(), delimiter, sb.length(), idx + 1);
    }

    private static String padTo(String s, int width) {
        if (s == null) {
            return " ".repeat(width);
        }
        if (s.length() >= width) {
            return s.substring(0, width);
        }
        return String.format("%-" + width + "s", s);
    }

    private static String padThreeDigits(String raw) {
        String digits = raw.replaceAll("\\D", "");
        if (digits.length() >= 3) {
            return digits.substring(digits.length() - 3);
        }
        return String.format("%3s", digits).replace(' ', '0');
    }
}
