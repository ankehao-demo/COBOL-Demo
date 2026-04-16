package com.coboldemo.unstring;

/**
 * Migrated from: unstring/unstring.cbl
 * Original author: Erik Eriksen (2022-02-11)
 * Purpose: Demonstrates UNSTRING with 6 examples covering delimiters, pointers, and statistics.
 */
public class UnstringExample {

    public static void main(String[] args) {
        System.out.println("UNSTRING Examples");
        System.out.println("=================");

        example1();
        example2();
        example3();
        example4();
        example5();
        example6();
    }

    /**
     * Example 1: Simple UNSTRING with space delimiter
     * UNSTRING ws-source DELIMITED BY SPACE INTO ws-dest-1 ws-dest-2 ws-dest-3
     */
    private static void example1() {
        System.out.println();
        System.out.println("Example 1: Simple UNSTRING with space delimiter");
        System.out.println("------------------------------------------------");

        String wsSource = "Hello World Test";
        String[] parts = wsSource.split(" ", 3);

        String wsDest1 = parts.length > 0 ? parts[0] : "";
        String wsDest2 = parts.length > 1 ? parts[1] : "";
        String wsDest3 = parts.length > 2 ? parts[2] : "";

        System.out.println("Source:  '" + wsSource + "'");
        System.out.println("Dest 1:  '" + wsDest1 + "'");
        System.out.println("Dest 2:  '" + wsDest2 + "'");
        System.out.println("Dest 3:  '" + wsDest3 + "'");
    }

    /**
     * Example 2: UNSTRING with pointer and overflow
     * UNSTRING ws-source DELIMITED BY SPACE INTO ws-dest
     *   WITH POINTER ws-ptr ON OVERFLOW ...
     */
    private static void example2() {
        System.out.println();
        System.out.println("Example 2: UNSTRING with pointer");
        System.out.println("---------------------------------");

        String wsSource = "One Two Three Four Five";
        int wsPtr = 0; // COBOL pointer is 1-based; we use 0-based here

        System.out.println("Source: '" + wsSource + "'");
        System.out.println();

        // Iterate using pointer
        int fieldCount = 0;
        while (wsPtr < wsSource.length()) {
            int nextSpace = wsSource.indexOf(' ', wsPtr);
            String field;
            if (nextSpace == -1) {
                field = wsSource.substring(wsPtr);
                wsPtr = wsSource.length();
            } else {
                field = wsSource.substring(wsPtr, nextSpace);
                wsPtr = nextSpace + 1;
            }
            fieldCount++;
            System.out.println("Field " + fieldCount + ": '" + field + "' (pointer: " + wsPtr + ")");
        }
    }

    /**
     * Example 3: UNSTRING into explicit fields
     * UNSTRING ws-csv DELIMITED BY "," INTO ws-field-1 ws-field-2 ws-field-3 ws-field-4
     */
    private static void example3() {
        System.out.println();
        System.out.println("Example 3: UNSTRING with comma delimiter");
        System.out.println("-----------------------------------------");

        String wsCsv = "John,Smith,42,New York";
        String[] fields = wsCsv.split(",", 4);

        String wsFirstName = fields.length > 0 ? fields[0] : "";
        String wsLastName = fields.length > 1 ? fields[1] : "";
        String wsAge = fields.length > 2 ? fields[2] : "";
        String wsCity = fields.length > 3 ? fields[3] : "";

        System.out.println("Source:     '" + wsCsv + "'");
        System.out.println("First Name: '" + wsFirstName + "'");
        System.out.println("Last Name:  '" + wsLastName + "'");
        System.out.println("Age:        '" + wsAge + "'");
        System.out.println("City:       '" + wsCity + "'");
    }

    /**
     * Example 4: UNSTRING with multiple delimiters and statistics
     * UNSTRING ws-source DELIMITED BY "," OR ";" INTO ws-dest-1 ws-dest-2 ws-dest-3
     *   DELIMITER IN ws-delim-1 ws-delim-2 ws-delim-3
     *   COUNT IN ws-count-1 ws-count-2 ws-count-3
     *   TALLYING IN ws-tally
     */
    private static void example4() {
        System.out.println();
        System.out.println("Example 4: UNSTRING with multiple delimiters and statistics");
        System.out.println("------------------------------------------------------------");

        String wsSource = "Apple,Banana;Cherry,Date";
        int wsPtr = 0;
        int wsTally = 0;

        System.out.println("Source: '" + wsSource + "'");
        System.out.println();

        // Parse with tracking delimiters and counts
        while (wsPtr < wsSource.length()) {
            int nextComma = wsSource.indexOf(',', wsPtr);
            int nextSemicolon = wsSource.indexOf(';', wsPtr);

            int nextDelim;
            String delimiter;

            if (nextComma == -1 && nextSemicolon == -1) {
                // No more delimiters
                String field = wsSource.substring(wsPtr);
                wsTally++;
                System.out.println("Field " + wsTally + ": '" + field + "' (count: " + field.length() + ", delimiter: none)");
                break;
            } else if (nextComma == -1) {
                nextDelim = nextSemicolon;
                delimiter = ";";
            } else if (nextSemicolon == -1) {
                nextDelim = nextComma;
                delimiter = ",";
            } else {
                if (nextComma < nextSemicolon) {
                    nextDelim = nextComma;
                    delimiter = ",";
                } else {
                    nextDelim = nextSemicolon;
                    delimiter = ";";
                }
            }

            String field = wsSource.substring(wsPtr, nextDelim);
            wsTally++;
            System.out.println("Field " + wsTally + ": '" + field + "' (count: " + field.length() + ", delimiter: '" + delimiter + "')");
            wsPtr = nextDelim + 1;
        }

        System.out.println("Tally: " + wsTally);
    }

    /**
     * Example 5: UNSTRING into multiple destination with ALL delimiter
     * UNSTRING ws-source DELIMITED BY ALL SPACES INTO ws-dest-1 ws-dest-2 ws-dest-3
     */
    private static void example5() {
        System.out.println();
        System.out.println("Example 5: UNSTRING with ALL SPACES delimiter");
        System.out.println("----------------------------------------------");

        String wsSource = "Hello    World     Test";
        // DELIMITED BY ALL SPACES: treat consecutive spaces as one delimiter
        String[] parts = wsSource.split("\\s+", 3);

        String wsDest1 = parts.length > 0 ? parts[0] : "";
        String wsDest2 = parts.length > 1 ? parts[1] : "";
        String wsDest3 = parts.length > 2 ? parts[2] : "";

        System.out.println("Source:  '" + wsSource + "'");
        System.out.println("Dest 1:  '" + wsDest1 + "'");
        System.out.println("Dest 2:  '" + wsDest2 + "'");
        System.out.println("Dest 3:  '" + wsDest3 + "'");
    }

    /**
     * Example 6: UNSTRING formatted number
     * UNSTRING ws-formatted-num DELIMITED BY "," INTO parts
     */
    private static void example6() {
        System.out.println();
        System.out.println("Example 6: UNSTRING formatted number");
        System.out.println("-------------------------------------");

        String wsFormattedNum = "1,234,567.89";

        // Remove commas to get raw number
        String rawNum = wsFormattedNum.replace(",", "");

        System.out.println("Formatted: '" + wsFormattedNum + "'");
        System.out.println("Raw:       '" + rawNum + "'");

        // Parse as double
        double value = Double.parseDouble(rawNum);
        System.out.printf("Numeric:    %.2f%n", value);
    }
}
