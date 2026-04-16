package com.coboldemo.search;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Java migration of search/search.cbl
 *
 * COBOL-to-Java mapping:
 *   SEARCH ALL (binary search on sorted, keyed table) -> Arrays.binarySearch() with Comparator
 *   SEARCH     (sequential search, no key required)   -> Linear loop / scan
 *
 *   OCCURS ... ASCENDING KEY ... INDEXED BY  -> Sorted array with Comparator
 *   OCCURS ... (no key) INDEXED BY           -> Plain array with linear scan
 *
 *   PIC 9(4)  -> int (displayed zero-padded to 4 digits)
 *   PIC X(16) -> String (fixed-width 16 chars, right-padded)
 *   PIC X(25) -> String (fixed-width 25 chars, right-padded)
 *
 * The COBOL program uses interactive ACCEPT for user input.  This Java version
 * demonstrates the same search logic with hard-coded search keys so it can run
 * without interactive input, mirroring the original output format.
 *
 * Original author: Erik Eriksen (COBOL version)
 */
public class SearchExample {

    // ---------------------------------------------------------------
    //  Record for the keyed item table (ws-item-table)
    //  COBOL: ascending key is ws-item-id-1, ws-item-id-2
    //         descending key is ws-item-id-3
    // ---------------------------------------------------------------
    private static class Item {
        int id1;   // PIC 9(4)
        int id2;   // PIC 9(4)
        int id3;   // PIC 9(4)
        String name;  // PIC X(16)
        String date;  // ws-item-date: year/month/day formatted

        Item(int id1, int id2, int id3, String name, String date) {
            this.id1 = id1;
            this.id2 = id2;
            this.id3 = id3;
            this.name = padRight(name, 16);
            this.date = date;
        }
    }

    // ---------------------------------------------------------------
    //  Record for the no-key item table (ws-no-key-item-table)
    // ---------------------------------------------------------------
    private static class NoKeyItem {
        int id;       // PIC 9(4)
        String value; // PIC X(25)

        NoKeyItem(int id, String value) {
            this.id = id;
            this.value = padRight(value, 25);
        }
    }

    /** Pad or truncate a string to a fixed width (emulates PIC X(n)). */
    private static String padRight(String s, int width) {
        if (s == null) s = "";
        if (s.length() >= width) return s.substring(0, width);
        return String.format("%-" + width + "s", s);
    }

    /** Format an int as zero-padded 4-digit string (emulates PIC 9(4)). */
    private static String fmt4(int n) {
        return String.format("%04d", n);
    }

    // ---------------------------------------------------------------
    //  Display a found item (mirrors COBOL display-found-item paragraph)
    // ---------------------------------------------------------------
    private static void displayFoundItem(Item item) {
        System.out.println(" Record found:");
        System.out.println("----------------");
        System.out.println("Item id-1: " + fmt4(item.id1));
        System.out.println("Item id-2: " + fmt4(item.id2));
        System.out.println("Item id-3: " + fmt4(item.id3));
        System.out.println("Item Name: " + item.name);
        System.out.println("Item Date: " + item.date);
        System.out.println();
    }

    // ---------------------------------------------------------------
    //  Binary search (SEARCH ALL) by id1 only
    //  COBOL: search all ws-item-table when ws-item-id-1(idx) = value
    // ---------------------------------------------------------------
    private static Item binarySearchById1(Item[] table, int searchId1) {
        // The table is sorted ascending by id1 (primary key).
        // Use Arrays.binarySearch with a comparator on id1.
        int index = Arrays.binarySearch(
                table,
                null, // dummy key element — we use the comparator below
                (a, b) -> {
                    // When b is null, it's the "key" placeholder; compare a.id1 to searchId1
                    if (b == null) return Integer.compare(a.id1, searchId1);
                    return Integer.compare(a.id1, b.id1);
                }
        );
        return (index >= 0) ? table[index] : null;
    }

    // ---------------------------------------------------------------
    //  Binary search (SEARCH ALL) by composite key (id1, id2, id3)
    //  COBOL: search all ws-item-table when id1=x and id2=y and id3=z
    // ---------------------------------------------------------------
    private static Item binarySearchByCompositeKey(Item[] table, int id1, int id2, int id3) {
        // Linear scan through the sorted table matching all three keys.
        // COBOL SEARCH ALL with composite keys performs a binary search
        // on the primary ascending keys and then checks descending key.
        // For a small table we match the semantics with a simple scan.
        for (Item item : table) {
            if (item.id1 == id1 && item.id2 == id2 && item.id3 == id3) {
                return item;
            }
        }
        return null;
    }

    // ---------------------------------------------------------------
    //  Sequential search (SEARCH) by id
    //  COBOL: search ws-no-key-item-table when ws-no-key-id(idx-2) = value
    // ---------------------------------------------------------------
    private static NoKeyItem sequentialSearchById(NoKeyItem[] table, int searchId) {
        for (NoKeyItem item : table) {
            if (item.id == searchId) {
                return item;
            }
        }
        return null;
    }

    // ---------------------------------------------------------------
    //  Setup test data (mirrors COBOL setup-test-data paragraph)
    // ---------------------------------------------------------------
    private static Item[] setupKeyedTable() {
        return new Item[] {
                new Item(1, 101, 500, "test item 1", "2021/01/01"),
                new Item(2, 102, 499, "test item 2", "2021/02/02"),
                new Item(3, 103, 498, "test item 3", "2021/03/03"),
        };
    }

    private static NoKeyItem[] setupNoKeyTable() {
        return new NoKeyItem[] {
                new NoKeyItem(2, "Value of id 2."),
                new NoKeyItem(3, "Value of id 3."),
                new NoKeyItem(1, "Value of id 1."),
        };
    }

    // ===============================================================
    //  MAIN
    // ===============================================================
    public static void main(String[] args) {
        Item[] keyedTable = setupKeyedTable();
        NoKeyItem[] noKeyTable = setupNoKeyTable();

        // --- Binary search by id-1 only ---
        // COBOL prompts: "Enter id-1 to search for: "
        // We demonstrate with a known value (0001) and a missing value (9999).

        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching keyed table using binary search.");

        int searchId1 = 1;
        System.out.println("Enter id-1 to search for: " + fmt4(searchId1));

        Item found = binarySearchById1(keyedTable, searchId1);
        if (found == null) {
            System.out.println("Item not found.");
        } else {
            displayFoundItem(found);
        }

        // --- Binary search by composite key (id-1, id-2, id-3) ---
        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching again with all required ids matching.");

        int compId1 = 2, compId2 = 102, compId3 = 499;
        System.out.println("Enter id-1 to search for: " + fmt4(compId1));
        System.out.println("Enter id-2 to search for: " + fmt4(compId2));
        System.out.println("Enter id-3 to search for: " + fmt4(compId3));

        found = binarySearchByCompositeKey(keyedTable, compId1, compId2, compId3);
        if (found == null) {
            System.out.println("Item not found.");
        } else {
            displayFoundItem(found);
        }

        // --- Demonstrate a not-found composite key search ---
        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching again with all required ids matching.");

        compId1 = 1; compId2 = 102; compId3 = 500;
        System.out.println("Enter id-1 to search for: " + fmt4(compId1));
        System.out.println("Enter id-2 to search for: " + fmt4(compId2));
        System.out.println("Enter id-3 to search for: " + fmt4(compId3));

        found = binarySearchByCompositeKey(keyedTable, compId1, compId2, compId3);
        if (found == null) {
            System.out.println("Item not found.");
        } else {
            displayFoundItem(found);
        }

        // --- Sequential search by id ---
        // COBOL prompts: "Enter id: "
        // Demonstrate with a known value (3) and a missing value (5).

        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching not keyed table using sequential search.");

        int seqSearchId = 3;
        System.out.println("Enter id: " + fmt4(seqSearchId));

        NoKeyItem nkFound = sequentialSearchById(noKeyTable, seqSearchId);
        if (nkFound == null) {
            System.out.println("Item not found.");
        } else {
            System.out.println(" Record found:");
            System.out.println("---------------");
            System.out.println("   ws-no-key-id: " + fmt4(nkFound.id));
            System.out.println("ws-no-key-value: " + nkFound.value);
            System.out.println();
        }

        // --- Sequential search not found case ---
        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching not keyed table using sequential search.");

        seqSearchId = 5;
        System.out.println("Enter id: " + fmt4(seqSearchId));

        nkFound = sequentialSearchById(noKeyTable, seqSearchId);
        if (nkFound == null) {
            System.out.println("Item not found.");
        } else {
            System.out.println(" Record found:");
            System.out.println("---------------");
            System.out.println("   ws-no-key-id: " + fmt4(nkFound.id));
            System.out.println("ws-no-key-value: " + nkFound.value);
            System.out.println();
        }

        System.out.println();
    }
}
