package com.coboldemo.unstring;

/**
 * Migrated from unstring/unstring.cbl
 * Demonstrates UNSTRING field parsing with various delimiter and pointer options.
 */
public class UnstringExample {

    public static void main(String[] args) {
        example1SimpleUnstring();
        example2PointerUnstring();
        example3ExplicitFields();
        example4LoopWithStats();
        example5MultipleDelimiters();
        example6NumericUnstring();
    }

    /**
     * EX 1: Simple UNSTRING - split "Hello World" by space into two parts.
     */
    private static void example1SimpleUnstring() {
        String source = "Hello World";
        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 1 : SIMPLE UNSTRING");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        int spaceIdx = source.indexOf(' ');
        String part1 = spaceIdx >= 0 ? source.substring(0, spaceIdx) : source;
        String part2 = spaceIdx >= 0 ? source.substring(spaceIdx + 1) : "";

        System.out.println("PART1: " + part1);
        System.out.println("PART2: " + part2);
    }

    /**
     * EX 2: UNSTRING multiple times into same destination with POINTER.
     * Demonstrates overflow detection.
     */
    private static void example2PointerUnstring() {
        String source = "Hello World";
        int pointer = 0; // 0-based (COBOL uses 1-based)

        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 2 : UNSTRING MULTIPLE TIMES INTO SAME DEST.");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        for (int i = 0; i < 2; i++) {
            if (pointer >= source.length()) {
                System.out.println("Successfully unstrung.");
                System.out.println("PART VALUE: ");
                System.out.println("POINTER: " + (pointer + 1));
                continue;
            }

            // Skip consecutive spaces (ALL SPACES delimiter)
            while (pointer < source.length() && source.charAt(pointer) == ' ') {
                pointer++;
            }

            // Find next space
            int nextSpace = source.indexOf(' ', pointer);
            String part;
            if (nextSpace >= 0) {
                part = source.substring(pointer, nextSpace);
                pointer = nextSpace + 1;
                // Overflow: more data remains
                System.out.println("ERROR: OVERFLOW");
            } else {
                part = source.substring(pointer);
                pointer = source.length();
                System.out.println("Successfully unstrung.");
            }

            System.out.println("PART VALUE: " + part);
            System.out.println("POINTER: " + (pointer + 1)); // Display as 1-based
        }
    }

    /**
     * EX 3: UNSTRING into explicit fields with pointer tracking.
     * No overflow because all data fits in the destination variables.
     */
    private static void example3ExplicitFields() {
        String source = "Hello World";
        int pointer = 0;

        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 3 : UNSTRING INTO EXPLICIT FIELDS");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        // Skip consecutive spaces
        while (pointer < source.length() && source.charAt(pointer) == ' ') {
            pointer++;
        }
        int nextSpace = source.indexOf(' ', pointer);
        String part1;
        if (nextSpace >= 0) {
            part1 = source.substring(pointer, nextSpace);
            pointer = nextSpace + 1;
        } else {
            part1 = source.substring(pointer);
            pointer = source.length();
        }

        // Skip spaces
        while (pointer < source.length() && source.charAt(pointer) == ' ') {
            pointer++;
        }
        String part2 = source.substring(pointer);
        pointer = source.length();

        // No overflow because pointer is past end of source
        System.out.println("Successfully unstrung.");
        System.out.println("PART1: " + part1);
        System.out.println("PART2: " + part2);
        System.out.println("POINTER: " + (pointer + 1));
    }

    /**
     * EX 4: Loop-based UNSTRING with delimiter-in, count-in, tallying.
     * Source: "aa|bb||dd|ee" with delimiter "|"
     */
    private static void example4LoopWithStats() {
        String source = "aa|bb||dd|ee";
        String delimiter = "|";

        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 4 : UNSTRING WITH STATS (LOOP)");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        int pointer = 0;
        int fieldsFilled = 0;

        System.out.println();
        System.out.printf("%-8s %-8s %-8s %-8s%n", "DEST", "DELIM", "COUNT", "TALLY");
        System.out.println("--------------------------------------");

        while (pointer <= source.length()) {
            int delimIdx = source.indexOf(delimiter, pointer);
            String destStr;
            String foundDelim;
            int charCount;

            if (delimIdx >= 0) {
                destStr = source.substring(pointer, delimIdx);
                foundDelim = delimiter;
                charCount = destStr.length();
                pointer = delimIdx + delimiter.length();
            } else {
                destStr = source.substring(pointer);
                foundDelim = "";
                charCount = destStr.length();
                pointer = source.length() + 1; // past end
            }

            fieldsFilled++;
            System.out.printf("%-8s %-8s %-8d %-8d%n",
                    destStr.isEmpty() ? "(empty)" : destStr,
                    foundDelim.isEmpty() ? "(none)" : foundDelim,
                    charCount, fieldsFilled);
        }
    }

    /**
     * EX 5: Multiple delimiters "|" and ";" with ALL option.
     * Source: "aa| bb;; dd| ee"
     */
    private static void example5MultipleDelimiters() {
        String source = "aa| bb;; dd| ee";
        String[] delimiters = {"|", ";"};

        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 5 : UNSTRING WITH MULTIPLE DELIMITERS");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        int pointer = 0;
        int fieldsFilled = 0;

        System.out.println();
        System.out.printf("%-8s %-8s %-8s %-8s%n", "DEST", "DELIM", "COUNT", "TALLY");
        System.out.println("--------------------------------------");

        while (pointer <= source.length()) {
            // Find the nearest delimiter
            int nearestIdx = source.length();
            String foundDelim = "";

            for (String d : delimiters) {
                int idx = source.indexOf(d, pointer);
                if (idx >= 0 && idx < nearestIdx) {
                    nearestIdx = idx;
                    foundDelim = d;
                }
            }

            String destStr;
            int charCount;

            if (nearestIdx < source.length()) {
                destStr = source.substring(pointer, nearestIdx);
                charCount = destStr.length();
                pointer = nearestIdx + foundDelim.length();
                // Skip consecutive same delimiters (ALL)
                while (pointer < source.length() && String.valueOf(source.charAt(pointer)).equals(foundDelim)) {
                    pointer++;
                }
            } else {
                destStr = source.substring(pointer);
                foundDelim = "";
                charCount = destStr.length();
                pointer = source.length() + 1;
            }

            fieldsFilled++;
            System.out.printf("%-8s %-8s %-8d %-8d%n",
                    destStr.isEmpty() ? "(empty)" : destStr,
                    foundDelim.isEmpty() ? "(none)" : foundDelim,
                    charCount, fieldsFilled);
        }
    }

    /**
     * EX 6: UNSTRING numeric formatted value "$123,456.78".
     */
    private static void example6NumericUnstring() {
        String source = "$123,456.78";

        System.out.println();
        System.out.println("=================================================");
        System.out.println("EX 6 : UNSTRING NUMERIC FORMAT");
        System.out.println();
        System.out.println("SOURCE STRING: " + source);

        // Remove leading $ and split by , and .
        String withoutDollar = source.startsWith("$") ? source.substring(1) : source;
        String[] parts = withoutDollar.split("[,.]");

        for (int i = 0; i < parts.length; i++) {
            try {
                int numPart = Integer.parseInt(parts[i].trim());
                System.out.printf("Part %d: %03d%n", i + 1, numPart);
            } catch (NumberFormatException e) {
                System.out.printf("Part %d: %s%n", i + 1, parts[i]);
            }
        }
    }
}
