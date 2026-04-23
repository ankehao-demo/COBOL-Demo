package com.coboldemo.datastructures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Migrated from: search/search.cbl
 *
 * Demonstrates COBOL SEARCH (sequential) and SEARCH ALL (binary) on
 * indexed tables. In Java, binary search maps to Arrays.binarySearch()
 * and sequential search maps to a linear loop or Stream.filter().
 */
public class SearchExample {

    static class KeyedItem implements Comparable<KeyedItem> {
        int id1;
        int id2;
        int id3;
        String name;
        String date;

        KeyedItem(int id1, int id2, int id3, String name, String date) {
            this.id1 = id1;
            this.id2 = id2;
            this.id3 = id3;
            this.name = name;
            this.date = date;
        }

        @Override
        public int compareTo(KeyedItem other) {
            int cmp = Integer.compare(this.id1, other.id1);
            if (cmp != 0) return cmp;
            return Integer.compare(this.id2, other.id2);
        }

        void display() {
            System.out.println(" Record found:");
            System.out.println("----------------");
            System.out.printf("Item id-1: %04d%n", id1);
            System.out.printf("Item id-2: %04d%n", id2);
            System.out.printf("Item id-3: %04d%n", id3);
            System.out.println("Item Name: " + name);
            System.out.println("Item Date: " + date);
            System.out.println();
        }
    }

    static class NoKeyItem {
        int id;
        String value;

        NoKeyItem(int id, String value) {
            this.id = id;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        // Setup test data (keyed table, sorted by id1 ascending)
        KeyedItem[] keyedItems = {
                new KeyedItem(1, 101, 500, "test item 1", "2021/01/01"),
                new KeyedItem(2, 102, 499, "test item 2", "2021/02/02"),
                new KeyedItem(3, 103, 498, "test item 3", "2021/03/03")
        };

        // No-key table (not sorted)
        NoKeyItem[] noKeyItems = {
                new NoKeyItem(2, "Value of id 2."),
                new NoKeyItem(3, "Value of id 3."),
                new NoKeyItem(1, "Value of id 1.")
        };

        Scanner scanner = new Scanner(System.in);

        // Binary search by id-1
        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching keyed table using binary search.");
        System.out.print("Enter id-1 to search for: ");
        int searchId1 = Integer.parseInt(scanner.nextLine().trim());

        int foundIdx = binarySearchById1(keyedItems, searchId1);
        if (foundIdx >= 0) {
            keyedItems[foundIdx].display();
        } else {
            System.out.println("Item not found.");
        }

        // Binary search with all three IDs
        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching again with all required ids matching.");
        System.out.print("Enter id-1 to search for: ");
        searchId1 = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter id-2 to search for: ");
        int searchId2 = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter id-3 to search for: ");
        int searchId3 = Integer.parseInt(scanner.nextLine().trim());

        boolean found = false;
        for (KeyedItem item : keyedItems) {
            if (item.id1 == searchId1 && item.id2 == searchId2 && item.id3 == searchId3) {
                item.display();
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Item not found.");
        }

        // Sequential search on unkeyed table
        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching not keyed table using sequential search.");
        System.out.print("Enter id: ");
        int searchId = Integer.parseInt(scanner.nextLine().trim());

        found = false;
        for (NoKeyItem item : noKeyItems) {
            if (item.id == searchId) {
                System.out.println(" Record found:");
                System.out.println("---------------");
                System.out.printf("   ws-no-key-id: %04d%n", item.id);
                System.out.println("ws-no-key-value: " + item.value);
                System.out.println();
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Item not found.");
        }

        System.out.println();
        scanner.close();
    }

    /**
     * Binary search by id1 on a sorted array.
     * Equivalent to COBOL SEARCH ALL on ascending key ws-item-id-1.
     */
    static int binarySearchById1(KeyedItem[] items, int targetId1) {
        int low = 0;
        int high = items.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (items[mid].id1 == targetId1) return mid;
            if (items[mid].id1 < targetId1) low = mid + 1;
            else high = mid - 1;
        }
        return -1;
    }
}
