package com.coboldemo.unstring;

/**
 * Java equivalent of unstring/unstring.cbl
 *
 * Demonstrates the COBOL UNSTRING verb with various delimiter, pointer,
 * overflow, tallying, and count options. Java equivalents use String.split(),
 * String.indexOf(), and manual parsing logic.
 *
 * COBOL Mapping:
 *   UNSTRING src DELIMITED BY space INTO p1 p2 → src.split("\\s+")
 *   WITH POINTER                                → manual index tracking
 *   DELIMITER IN / COUNT IN                     → custom parsing returning metadata
 *   TALLYING IN                                 → counter for fields filled
 *   ON OVERFLOW / NOT ON OVERFLOW               → check if pointer < source length
 */
public class UnstringExample {

    public static void main(String[] args) {

        // ========== EXAMPLE 1: Simple unstring ==========
        String source = padRight("Hello World", 30);
        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 1 : SIMPLE UNSTRING");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        String[] parts = source.trim().split("\\s+", 2);
        String part1 = parts.length > 0 ? padRight(parts[0], 15) : padRight("", 15);
        String part2 = parts.length > 1 ? padRight(parts[1], 15) : padRight("", 15);

        System.out.println("PART1: " + part1);
        System.out.println("PART2: " + part2);

        // ========== EXAMPLE 2: Unstring multiple times into same dest with pointer ==========
        int pointer = 0; // 0-based (COBOL uses 1-based)
        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 2 : UNSTRING MULTIPLE TIMES INTO SAME DEST.");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        for (int i = 0; i < 2; i++) {
            // Skip leading spaces (DELIMITED BY ALL SPACES)
            while (pointer < source.length() && source.charAt(pointer) == ' ') {
                pointer++;
            }
            int end = source.indexOf(' ', pointer);
            if (end == -1) end = source.length();

            String value = padRight(source.substring(pointer, end), 15);
            pointer = end;

            // Overflow: pointer has not passed end of data
            boolean overflow = pointer < source.trim().length();
            if (overflow) {
                System.out.println("ERROR: OVERFLOW");
            } else {
                System.out.println("Successfully unstrung.");
            }
            System.out.println("PART VALUE: " + value);
            System.out.println("POINTER: " + (pointer + 1)); // Display as 1-based
        }

        // ========== EXAMPLE 3: Unstring into explicit fields ==========
        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 3 : UNSTRING INTO EXPLICIT FIELDS");

        pointer = 0;
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        String[] allParts = source.trim().split("\\s+", 2);
        part1 = allParts.length > 0 ? padRight(allParts[0], 15) : padRight("", 15);
        part2 = allParts.length > 1 ? padRight(allParts[1], 15) : padRight("", 15);
        pointer = source.trim().length();

        System.out.println("Successfully unstrung.");
        System.out.println("PART1: " + part1);
        System.out.println("PART2: " + part2);
        System.out.println("POINTER: " + (pointer + 1));

        // ========== EXAMPLE 4: Unstring with multiple delimiters ==========
        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 4 : UNSTRING WITH MULTIPLE DELIMITERS ");

        pointer = 0;
        source = "A<B<CD>E%FG!HIJ|KL!MN>OP#QR!ST";
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        String delimiters = "<>!|";
        int fieldsFilled = 0;

        while (pointer < source.length()) {
            int nextDelimPos = source.length();
            char foundDelimiter = ' ';

            // Find the nearest delimiter
            for (char d : delimiters.toCharArray()) {
                int pos = source.indexOf(d, pointer);
                if (pos != -1 && pos < nextDelimPos) {
                    nextDelimPos = pos;
                    foundDelimiter = d;
                }
            }

            String value;
            int charCount;
            if (nextDelimPos < source.length()) {
                value = source.substring(pointer, nextDelimPos);
                charCount = value.length();
                pointer = nextDelimPos + 1;
            } else {
                value = source.substring(pointer);
                charCount = value.length();
                pointer = source.length();
                foundDelimiter = ' ';
            }
            fieldsFilled++;

            System.out.println();
            System.out.println("VALUE: " + padRight(value, 5));
            System.out.println("DELIMITER: " + foundDelimiter);
            System.out.println("CHAR COUNT:" + charCount);
            System.out.println("CURRENT POINTER: " + (pointer + 1));
            System.out.println("TOTAL FIELDS FILLED: " + String.format("%02d", fieldsFilled));
            System.out.println("-------------------------------------------");
        }

        // ========== EXAMPLE 5: Unstring with multiple delimiters into multiple dests ==========
        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 5 : UNSTRING WITH MULTIPLE DELIMITERS INTO MULTIPLE DESTINATIONS");

        source = "A<B<CD>EFG!HIJ|KLMN>O";
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        String[] destStrs = new String[6];
        char[] destDelimiters = new char[6];
        int[] destCounts = new int[6];
        pointer = 0;
        fieldsFilled = 0;

        for (int i = 0; i < 6 && pointer < source.length(); i++) {
            int nextDelimPos = source.length();
            char fd = ' ';

            for (char d : delimiters.toCharArray()) {
                int pos = source.indexOf(d, pointer);
                if (pos != -1 && pos < nextDelimPos) {
                    nextDelimPos = pos;
                    fd = d;
                }
            }

            if (nextDelimPos < source.length()) {
                destStrs[i] = source.substring(pointer, nextDelimPos);
                destDelimiters[i] = fd;
                destCounts[i] = destStrs[i].length();
                pointer = nextDelimPos + 1;
                // Skip consecutive same delimiters (ALL "<")
                while (pointer < source.length() && source.charAt(pointer) == fd) {
                    pointer++;
                }
            } else {
                destStrs[i] = source.substring(pointer);
                destDelimiters[i] = ' ';
                destCounts[i] = destStrs[i].length();
                pointer = source.length();
            }
            fieldsFilled++;
        }

        for (int i = 0; i < 6; i++) {
            System.out.println();
            System.out.println("STRING NUMBER: " + (i + 1));
            System.out.println("VALUE: " + padRight(destStrs[i] != null ? destStrs[i] : "", 5));
            System.out.println("DELIMITER: " + destDelimiters[i]);
            System.out.println("CHAR COUNT:" + destCounts[i]);
            System.out.println("-------------------------------------------");
        }
        System.out.println("TOTALS: ");
        System.out.println("FIELDS FILLED: " + String.format("%02d", fieldsFilled));

        // ========== EXAMPLE 6: Unstring formatted number ==========
        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 6 : UNSTRING FORMATTED NUMBER");
        System.out.println();

        // PIC $999,999.99 with value 123456.12 → "$123,456.12"
        double sourceNum = 123456.12;
        String formattedNum = String.format(java.util.Locale.US, "$%,.2f", sourceNum);
        System.out.println("SOURCE VALUE: " + formattedNum);

        // Strip leading '$' then split on ',' and '.'
        String withoutDollar = formattedNum.substring(1);
        String[] numParts = withoutDollar.split("[,.]");
        for (int i = 0; i < Math.min(numParts.length, 3); i++) {
            System.out.println("PART " + (i + 1) + ": " + String.format("%03d", Integer.parseInt(numParts[i])));
        }
        System.out.println();
    }

    private static String padRight(String s, int length) {
        if (s == null) return String.format("%-" + length + "s", "");
        if (s.length() >= length) return s.substring(0, length);
        return String.format("%-" + length + "s", s);
    }
}
