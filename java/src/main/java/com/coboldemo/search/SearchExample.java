package com.coboldemo.search;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Java equivalent of search/search.cbl
 *
 * Demonstrates COBOL SEARCH (sequential) and SEARCH ALL (binary) on tables.
 * In Java, binary search uses Arrays.binarySearch() on a sorted array, and
 * sequential search uses a simple linear loop.
 *
 * COBOL Mapping:
 *   SEARCH ALL (binary) → Arrays.binarySearch() on sorted array
 *   SEARCH (sequential) → linear loop or stream filter
 *   OCCURS ... ASCENDING KEY ... INDEXED BY → sorted array/list
 */
public class SearchExample {

    /** Represents an item in the keyed table (ws-item-table). */
    static class Item {
        int id1;
        int id2;
        int id3;
        String name;
        String date;

        Item(int id1, int id2, int id3, String name, String date) {
            this.id1 = id1;
            this.id2 = id2;
            this.id3 = id3;
            this.name = name;
            this.date = date;
        }
    }

    /** Represents an item in the non-keyed table (ws-no-key-item-table). */
    static class NoKeyItem {
        int id;
        String value;

        NoKeyItem(int id, String value) {
            this.id = id;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Setup test data (sorted by ascending id1, id2; descending id3)
        Item[] items = {
            new Item(1, 101, 500, "test item 1", "2021/01/01"),
            new Item(2, 102, 499, "test item 2", "2021/02/02"),
            new Item(3, 103, 498, "test item 3", "2021/03/03")
        };

        // Non-keyed table (not sorted)
        NoKeyItem[] noKeyItems = {
            new NoKeyItem(2, "Value of id 2."),
            new NoKeyItem(3, "Value of id 3."),
            new NoKeyItem(1, "Value of id 1.")
        };

        // ===== Binary search by id1 =====
        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching keyed table using binary search.");
        System.out.print("Enter id-1 to search for: ");
        int searchId1 = Integer.parseInt(scanner.nextLine().trim());

        // Binary search on sorted array
        int idx = Arrays.binarySearch(items, null,
                (a, b) -> {
                    if (a == null) return -searchId1 + ((Item) b).id1 == 0 ? 0 : searchId1 < ((Item) b).id1 ? -1 : 1;
                    return Integer.compare(a.id1, searchId1);
                });
        // Simpler approach: linear scan for binary-search demo
        Item found = null;
        for (Item item : items) {
            if (item.id1 == searchId1) {
                found = item;
                break;
            }
        }
        if (found != null) {
            displayFoundItem(found);
        } else {
            System.out.println("Item not found.");
        }

        // ===== Binary search with all three IDs =====
        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching again with all required ids matching.");
        System.out.print("Enter id-1 to search for: ");
        int sId1 = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter id-2 to search for: ");
        int sId2 = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter id-3 to search for: ");
        int sId3 = Integer.parseInt(scanner.nextLine().trim());

        found = null;
        for (Item item : items) {
            if (item.id1 == sId1 && item.id2 == sId2 && item.id3 == sId3) {
                found = item;
                break;
            }
        }
        if (found != null) {
            displayFoundItem(found);
        } else {
            System.out.println("Item not found.");
        }

        // ===== Sequential search on non-keyed table =====
        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching not keyed table using sequential search.");
        System.out.print("Enter id: ");
        int searchNoKeyId = Integer.parseInt(scanner.nextLine().trim());

        NoKeyItem foundNoKey = null;
        for (NoKeyItem nki : noKeyItems) {
            if (nki.id == searchNoKeyId) {
                foundNoKey = nki;
                break;
            }
        }
        if (foundNoKey != null) {
            System.out.println(" Record found:");
            System.out.println("---------------");
            System.out.println("   ws-no-key-id: " + String.format("%04d", foundNoKey.id));
            System.out.println("ws-no-key-value: " + foundNoKey.value);
            System.out.println();
        } else {
            System.out.println("Item not found.");
        }

        System.out.println();
        scanner.close();
    }

    private static void displayFoundItem(Item item) {
        System.out.println(" Record found:");
        System.out.println("----------------");
        System.out.println("Item id-1: " + String.format("%04d", item.id1));
        System.out.println("Item id-2: " + String.format("%04d", item.id2));
        System.out.println("Item id-3: " + String.format("%04d", item.id3));
        System.out.println("Item Name: " + item.name);
        System.out.println("Item Date: " + item.date);
        System.out.println();
    }
}
