package com.example.cobol.unstring;

/**
 * Java port of {@code unstring/unstring.cbl}.
 *
 * <p>The COBOL {@code UNSTRING} verb breaks a source string apart on a set
 * of delimiters and writes each piece into a series of destinations. Java
 * has no direct equivalent, so we reimplement the splitter using a small
 * iterator that walks the source while tracking pointer offsets, delimiter
 * matches and per-field character counts.
 */
public final class UnstringExample {

    /** Result of a single UNSTRING into one destination field. */
    public static final class Field {
        public String value = "";
        public char delimiter = ' ';
        public int charCount = 0;
    }

    private UnstringExample() {}

    public static void main(String[] args) {
        // -------- Example 1: simple unstring on space --------
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("EX 1 : SIMPLE UNSTRING");
        System.out.println();

        String source1 = "Hello World";
        System.out.println("SOURCE STRING: " + source1);

        String[] parts = source1.split(" ", -1);
        String part1 = parts.length > 0 ? padTo15(parts[0]) : padTo15("");
        String part2 = parts.length > 1 ? padTo15(parts[1]) : padTo15("");
        System.out.println("PART1: " + part1);
        System.out.println("PART2: " + part2);

        // -------- Example 2: unstring multiple times into same dest --------
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("EX 2 : UNSTRING MULTIPLE TIMES INTO SAME DEST.");

        int[] pointer = new int[] {0};
        System.out.println();
        System.out.println("SOURCE STRING: " + source1);
        for (int i = 0; i < 2; i++) {
            Field f = unstringOne(source1, pointer, " |<>!");
            // First iteration consumes "Hello", second hits end of string.
            if (pointer[0] >= source1.length() && f.value.isEmpty()) {
                System.out.println("ERROR: OVERFLOW");
            } else {
                System.out.println("Successfully unstrung.");
            }
            System.out.println("PART VALUE: " + padTo15(f.value));
            System.out.println("POINTER: " + pointer[0]);
        }

        // -------- Example 3: unstring into explicit fields --------
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("EX 3 : UNSTRING INTO EXPLICIT FIELDS");

        pointer[0] = 0;
        System.out.println();
        System.out.println("SOURCE STRING: " + source1);
        Field f1 = unstringOne(source1, pointer, " ");
        Field f2 = unstringOne(source1, pointer, " ");
        if (pointer[0] < source1.length()) {
            System.out.println("ERROR: OVERFLOW");
        } else {
            System.out.println("Successfully unstrung.");
        }
        System.out.println("PART1: " + padTo15(f1.value));
        System.out.println("PART2: " + padTo15(f2.value));
        System.out.println("POINTER: " + pointer[0]);

        // -------- Example 4: multi-delimiter split into single dest in a loop --------
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("EX 4 : UNSTRING WITH MULTIPLE DELIMITERS");

        String source4 = "A<B<CD>E%FG!HIJ|KL!MN>OP#QR!ST";
        System.out.println();
        System.out.println("SOURCE STRING: " + source4);

        pointer[0] = 0;
        int totalFields = 0;
        while (pointer[0] < source4.length()) {
            Field f = unstringOne(source4, pointer, "<>!|");
            totalFields++;
            System.out.println();
            System.out.println("VALUE: " + padTo5(f.value));
            System.out.println("DELIMITER: " + f.delimiter);
            System.out.println("CHAR COUNT:" + f.charCount);
            System.out.println("CURRENT POINTER: " + pointer[0]);
            System.out.println("TOTAL FIELDS FILLED: " + totalFields);
            System.out.println("-------------------------------------------");
        }

        // -------- Example 5: multi-delim into multiple destinations --------
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("EX 5 : UNSTRING WITH MULTIPLE DELIMITERS INTO MULTIPLE DESTINATIONS");
        String source5 = "A<B<CD>EFG!HIJ|KLMN>O";
        System.out.println();
        System.out.println("SOURCE STRING: " + source5);

        pointer[0] = 0;
        Field[] dest = new Field[6];
        int multiFilled = 0;
        for (int i = 0; i < dest.length; i++) {
            if (pointer[0] >= source5.length()) {
                dest[i] = new Field();
                continue;
            }
            dest[i] = unstringOne(source5, pointer, "<>!|");
            multiFilled++;
        }
        for (int i = 0; i < dest.length; i++) {
            System.out.println();
            System.out.println("STRING NUMBER: " + (i + 1));
            System.out.println("VALUE: " + padTo5(dest[i].value));
            System.out.println("DELIMITER: " + dest[i].delimiter);
            System.out.println("CHAR COUNT:" + dest[i].charCount);
            System.out.println("-------------------------------------------");
        }
        System.out.println("TOTALS: ");
        System.out.println("FIELDS FILLED: " + multiFilled);

        // -------- Example 6: unstring a formatted number on , and . --------
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("EX 6 : UNSTRING FORMATTED NUMBER");
        System.out.println();

        String source6 = "$123,456.12";
        System.out.println("SOURCE VALUE: " + source6);
        // Skip leading '$'
        String trimmed = source6.substring(1);
        String[] numParts = trimmed.split("[,.]", -1);
        String n1 = numParts.length > 0 ? padNum3(numParts[0]) : "000";
        String n2 = numParts.length > 1 ? padNum3(numParts[1]) : "000";
        String n3 = numParts.length > 2 ? padNum3(numParts[2]) : "000";
        System.out.println("PART 1: " + n1);
        System.out.println("PART 2: " + n2);
        System.out.println("PART 3: " + n3);
        System.out.println();
    }

    /**
     * Read one field from {@code src} starting at {@code pointer[0]}, stopping
     * at the next character contained in {@code delimiters}. Updates the
     * pointer past the consumed delimiter (mimicking COBOL's UNSTRING
     * pointer behaviour).
     */
    private static Field unstringOne(String src, int[] pointer, String delimiters) {
        Field f = new Field();
        int start = pointer[0];
        int i = start;
        while (i < src.length() && delimiters.indexOf(src.charAt(i)) < 0) {
            i++;
        }
        f.value = src.substring(start, i);
        f.charCount = f.value.length();
        if (i < src.length()) {
            f.delimiter = src.charAt(i);
            pointer[0] = i + 1;
        } else {
            f.delimiter = ' ';
            pointer[0] = i;
        }
        return f;
    }

    private static String padTo15(String s) {
        if (s.length() >= 15) {
            return s.substring(0, 15);
        }
        return String.format("%-15s", s);
    }

    private static String padTo5(String s) {
        if (s.length() >= 5) {
            return s.substring(0, 5);
        }
        return String.format("%-5s", s);
    }

    private static String padNum3(String s) {
        if (s.length() >= 3) {
            return s.substring(0, 3);
        }
        return String.format("%-3s", s).replace(' ', '0');
    }
}
