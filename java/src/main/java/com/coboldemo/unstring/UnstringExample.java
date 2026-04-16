package com.coboldemo.unstring;

import java.util.ArrayList;
import java.util.List;

/**
 * Java migration of unstring/unstring.cbl
 *
 * COBOL-to-Java mapping:
 *   UNSTRING ... DELIMITED BY ... INTO ...  ->  Manual indexOf / substring logic
 *   WITH POINTER                            ->  An integer index tracking position in the source
 *   DELIMITER IN                            ->  Track which delimiter was matched
 *   COUNT IN                                ->  Track character count of extracted segment
 *   TALLYING IN                             ->  Counter incremented for each field filled
 *   ON OVERFLOW / NOT ON OVERFLOW           ->  Check whether pointer exceeds source length
 *
 *   PIC X(n) fields are emulated with fixed-width right-padded strings where needed.
 *
 * Original author: Erik Eriksen (COBOL version)
 */
public class UnstringExample {

    // ---------------------------------------------------------------
    //  Helper: pad or truncate a string to a fixed width (PIC X(n))
    // ---------------------------------------------------------------
    private static String picX(String value, int width) {
        if (value == null) {
            value = "";
        }
        if (value.length() >= width) {
            return value.substring(0, width);
        }
        return String.format("%-" + width + "s", value);
    }

    // ---------------------------------------------------------------
    //  Helper class to hold per-field unstring results
    // ---------------------------------------------------------------
    private static class UnstringField {
        String value;      // extracted text
        String delimiter;  // delimiter that terminated this field
        int charCount;     // number of characters in the extracted text

        UnstringField(String value, String delimiter, int charCount) {
            this.value = value;
            this.delimiter = delimiter;
            this.charCount = charCount;
        }
    }

    /**
     * Core unstring engine.
     *
     * Scans {@code source} starting at {@code pointer[0]} (1-based, COBOL convention).
     * For each destination slot requested ({@code maxFields}), it looks for the earliest
     * occurrence of any delimiter in {@code delimiters}.  When {@code allFlags[i]} is true
     * the corresponding delimiter may repeat consecutively and the whole run counts as
     * one delimiter occurrence (COBOL "ALL" keyword).
     *
     * After the call, {@code pointer[0]} is updated to the next unscanned position
     * (1-based) and {@code tallying[0]} is incremented by the number of fields filled.
     *
     * @return list of UnstringField results (one per filled destination)
     */
    private static List<UnstringField> unstringCore(
            String source,
            String[] delimiters,
            boolean[] allFlags,
            int maxFields,
            int[] pointer,   // in/out, 1-based
            int[] tallying   // in/out
    ) {
        List<UnstringField> results = new ArrayList<>();
        // Convert to 0-based index for Java
        int pos = pointer[0] - 1;

        for (int fieldNum = 0; fieldNum < maxFields && pos < source.length(); fieldNum++) {
            // Find the earliest delimiter match from the current position
            int bestPos = -1;
            int bestDelimIdx = -1;

            for (int d = 0; d < delimiters.length; d++) {
                int found = source.indexOf(delimiters[d], pos);
                if (found >= 0 && (bestPos < 0 || found < bestPos)) {
                    bestPos = found;
                    bestDelimIdx = d;
                }
            }

            if (bestPos < 0) {
                // No delimiter found - take the rest of the string
                String segment = source.substring(pos);
                results.add(new UnstringField(segment, "", segment.length()));
                tallying[0]++;
                pos = source.length() + 1; // past end (1-based pointer will be length+1)
            } else {
                String segment = source.substring(pos, bestPos);
                String matchedDelim = delimiters[bestDelimIdx];
                int delimEnd = bestPos + matchedDelim.length();

                // If ALL flag is set, consume consecutive repetitions of this delimiter
                if (allFlags[bestDelimIdx]) {
                    while (delimEnd + matchedDelim.length() <= source.length()
                            && source.substring(delimEnd, delimEnd + matchedDelim.length()).equals(matchedDelim)) {
                        delimEnd += matchedDelim.length();
                    }
                }

                results.add(new UnstringField(segment, matchedDelim, segment.length()));
                tallying[0]++;
                pos = delimEnd;
            }
        }

        // Update 1-based pointer
        pointer[0] = pos + 1;
        return results;
    }

    // ===============================================================
    //  EXAMPLE 1: Simple unstring "Hello World" by space into 2 parts
    // ===============================================================
    private static void example1() {
        String wsSourceStr = picX("Hello World", 30);

        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 1 : SIMPLE UNSTRING");
        System.out.println();
        System.out.println("SOURCE STRING: " + wsSourceStr);

        // COBOL: unstring ws-source-str delimited by space into ws-part-1 ws-part-2
        int[] pointer = {1};
        int[] tallying = {0};
        List<UnstringField> fields = unstringCore(
                wsSourceStr,
                new String[]{" "},
                new boolean[]{false},
                2,
                pointer,
                tallying
        );

        String wsPart1 = picX(fields.size() > 0 ? fields.get(0).value : "", 15);
        String wsPart2 = picX(fields.size() > 1 ? fields.get(1).value : "", 15);

        System.out.println("PART1: " + wsPart1);
        System.out.println("PART2: " + wsPart2);
    }

    // ===============================================================
    //  EXAMPLE 2: Unstring multiple times into same dest using pointer
    // ===============================================================
    private static void example2() {
        String wsSourceStr = picX("Hello World", 30);
        int[] pointer = {1};
        int[] tallying = {0};

        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 2 : UNSTRING MULTIPLE TIMES INTO SAME DEST.");

        System.out.println();
        System.out.println("SOURCE STRING: " + wsSourceStr);

        // COBOL: perform 2 times ... unstring ... delimited by ALL spaces ... with pointer
        for (int i = 0; i < 2; i++) {
            int prevPointer = pointer[0];
            List<UnstringField> fields = unstringCore(
                    wsSourceStr,
                    new String[]{" "},
                    new boolean[]{true},  // ALL spaces
                    1,
                    pointer,
                    tallying
            );

            // COBOL overflow: pointer was NOT past end after unstring -> overflow
            // COBOL not overflow: pointer IS past end -> not overflow
            boolean overflow = (pointer[0] <= wsSourceStr.length());
            if (overflow) {
                System.out.println("ERROR: OVERFLOW");
            } else {
                System.out.println("Successfully unstrung.");
            }

            String wsPart1 = picX(fields.size() > 0 ? fields.get(0).value : "", 15);
            System.out.println("PART VALUE: " + wsPart1);
            System.out.println("POINTER: " + String.format("%05d", pointer[0]));
        }
    }

    // ===============================================================
    //  EXAMPLE 3: Unstring into explicit fields with pointer tracking
    // ===============================================================
    private static void example3() {
        String wsSourceStr = picX("Hello World", 30);

        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 3 : UNSTRING INTO EXPLICIT FIELDS");

        int[] pointer = {1};
        int[] tallying = {0};

        System.out.println();
        System.out.println("SOURCE STRING: " + wsSourceStr);

        // COBOL: unstring ... delimited by ALL spaces into ws-part-1 ws-part-2 with pointer
        List<UnstringField> fields = unstringCore(
                wsSourceStr,
                new String[]{" "},
                new boolean[]{true},  // ALL spaces
                2,
                pointer,
                tallying
        );

        boolean overflow = (pointer[0] <= wsSourceStr.length());
        if (overflow) {
            System.out.println("ERROR: OVERFLOW");
        } else {
            System.out.println("Successfully unstrung.");
        }

        String wsPart1 = picX(fields.size() > 0 ? fields.get(0).value : "", 15);
        String wsPart2 = picX(fields.size() > 1 ? fields.get(1).value : "", 15);

        System.out.println("PART1: " + wsPart1);
        System.out.println("PART2: " + wsPart2);
        System.out.println("POINTER: " + String.format("%05d", pointer[0]));
    }

    // ===============================================================
    //  EXAMPLE 4: Unstring with multiple delimiters (<, >, !, |)
    //             into single dest with delimiter-in, count-in, tallying-in
    // ===============================================================
    private static void example4() {
        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 4 : UNSTRING WITH MULTIPLE DELIMITERS ");

        int[] pointer = {1};

        String wsSourceStr = picX("A<B<CD>E%FG!HIJ|KL!MN>OP#QR!ST", 30);

        System.out.println();
        System.out.println("SOURCE STRING: " + wsSourceStr);

        // COBOL: delimited by all "<" or ">" or "!" or ws-delimiter ("|")
        // In the COBOL source, "<" uses ALL keyword
        String[] delimiters = {"<", ">", "!", "|"};
        boolean[] allFlags = {true, false, false, false};

        // COBOL tallying starts at 0 (pic 99 initialized)
        int[] tallying = {0};

        // COBOL: perform until ws-pointer > function length(ws-source-str)
        while (pointer[0] <= wsSourceStr.length()) {
            int prevTally = tallying[0];
            List<UnstringField> fields = unstringCore(
                    wsSourceStr,
                    delimiters,
                    allFlags,
                    1,
                    pointer,
                    tallying
            );

            if (!fields.isEmpty()) {
                UnstringField f = fields.get(0);
                System.out.println();
                System.out.println("VALUE: " + picX(f.value, 5));
                System.out.println("DELIMITER: " + (f.delimiter.isEmpty() ? " " : f.delimiter));
                System.out.println("CHAR COUNT:" + f.charCount);
                System.out.println("CURRENT POINTER: " + String.format("%05d", pointer[0]));
                System.out.println("TOTAL FIELDS FILLED: " + String.format("%02d", tallying[0]));
                System.out.println("-------------------------------------------");
            }
        }
    }

    // ===============================================================
    //  EXAMPLE 5: Unstring with multiple delimiters into multiple
    //             destinations (6 fields)
    // ===============================================================
    private static void example5() {
        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 5 : UNSTRING WITH MULTIPLE DELIMITERS INTO MULTIPLE DESTINATIONS");

        String wsSourceStr = picX("A<B<CD>EFG!HIJ|KLMN>O", 30);

        System.out.println();
        System.out.println("SOURCE STRING: " + wsSourceStr);

        // COBOL: delimited by all "<" or all ">" or "!" or ws-delimiter ("|")
        String[] delimiters = {"<", ">", "!", "|"};
        boolean[] allFlags = {true, true, false, false};

        int[] pointer = {1};
        int[] tallying = {0};

        List<UnstringField> fields = unstringCore(
                wsSourceStr,
                delimiters,
                allFlags,
                6,
                pointer,
                tallying
        );

        // Display results for each of the 6 destination slots
        for (int i = 0; i < 6; i++) {
            System.out.println();
            System.out.println("STRING NUMBER: " + String.format("%02d", i + 1));
            if (i < fields.size()) {
                UnstringField f = fields.get(i);
                System.out.println("VALUE: " + picX(f.value, 5));
                System.out.println("DELIMITER: " + (f.delimiter.isEmpty() ? " " : f.delimiter));
                System.out.println("CHAR COUNT:" + f.charCount);
            } else {
                System.out.println("VALUE: " + picX("", 5));
                System.out.println("DELIMITER:  ");
                System.out.println("CHAR COUNT:0");
            }
            System.out.println("-------------------------------------------");
        }

        System.out.println("TOTALS: ");
        System.out.println("FIELDS FILLED: " + String.format("%02d", tallying[0]));
    }

    // ===============================================================
    //  EXAMPLE 6: Unstring formatted number "$123,456.12"
    //             by comma and period
    // ===============================================================
    private static void example6() {
        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 6 : UNSTRING FORMATTED NUMBER");
        System.out.println();

        // COBOL: move 123456.12 to ws-source-num  (pic $999,999.99 -> "$123,456.12")
        String wsSourceNum = "$123,456.12";
        System.out.println("SOURCE VALUE: " + wsSourceNum);

        // COBOL: unstring ws-source-num(2:)  -- skip the '$' (start at position 2)
        String numWithoutDollar = wsSourceNum.substring(1);

        // COBOL: delimited by ',' or '.' into ws-dest-num(1) ws-dest-num(2) ws-dest-num(3)
        int[] pointer = {1};
        int[] tallying = {0};
        List<UnstringField> fields = unstringCore(
                numWithoutDollar,
                new String[]{",", "."},
                new boolean[]{false, false},
                3,
                pointer,
                tallying
        );

        // COBOL: ws-dest-num pic 999 -- 3-digit numeric fields
        for (int i = 0; i < 3; i++) {
            String val = (i < fields.size()) ? fields.get(i).value : "";
            // Format as 3-digit number (PIC 999), left-padded with zeros
            String formatted;
            try {
                int num = Integer.parseInt(val.trim());
                formatted = String.format("%03d", num);
            } catch (NumberFormatException e) {
                formatted = picX(val, 3);
            }
            System.out.println("PART " + (i + 1) + ": " + formatted);
        }
        System.out.println();
    }

    // ===============================================================
    //  MAIN
    // ===============================================================
    public static void main(String[] args) {
        example1();
        example2();
        example3();
        example4();
        example5();
        example6();
    }
}
