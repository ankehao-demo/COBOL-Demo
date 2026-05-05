package com.coboldemo.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Migrated from search/search.cbl
 * Demonstrates SEARCH (sequential) and SEARCH ALL (binary) search.
 */
public class SearchExample {

    static class Item implements Comparable<Item> {
        int id1;
        int id2;
        int id3;
        String name;
        String date; // YYYY/MM/DD

        Item(int id1, int id2, int id3, String name, String date) {
            this.id1 = id1;
            this.id2 = id2;
            this.id3 = id3;
            this.name = name;
            this.date = date;
        }

        @Override
        public int compareTo(Item other) {
            return Integer.compare(this.id1, other.id1);
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
        Scanner scanner = new Scanner(System.in);

        // Setup keyed table (sorted by id1 ascending for binary search)
        List<Item> items = new ArrayList<>();
        items.add(new Item(1, 1234, 9999, "Test Item 1     ", "2020/01/31"));
        items.add(new Item(2, 2, 50, "Test Item 2     ", "2021/06/15"));
        items.add(new Item(3, 5678, 1, "Test Item 3     ", "2022/12/25"));
        Collections.sort(items);

        // Binary search by id1 only
        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching keyed table using binary search.");
        System.out.print("Enter id-1 to search for: ");
        int searchId1 = scanner.nextInt();

        int idx = Collections.binarySearch(items, new Item(searchId1, 0, 0, "", ""));
        if (idx >= 0) {
            displayFoundItem(items.get(idx));
        } else {
            System.out.println("Item not found.");
        }

        // Binary search with all keys matching
        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching again with all required ids matching.");

        System.out.print("Enter id-1 to search for: ");
        searchId1 = scanner.nextInt();
        System.out.print("Enter id-2 to search for: ");
        int searchId2 = scanner.nextInt();
        System.out.print("Enter id-3 to search for: ");
        int searchId3 = scanner.nextInt();

        idx = Collections.binarySearch(items, new Item(searchId1, 0, 0, "", ""));
        if (idx >= 0) {
            Item found = items.get(idx);
            if (found.id2 == searchId2 && found.id3 == searchId3) {
                displayFoundItem(found);
            } else {
                System.out.println("Item not found.");
            }
        } else {
            System.out.println("Item not found.");
        }

        // Sequential search on unkeyed table
        List<NoKeyItem> noKeyItems = new ArrayList<>();
        noKeyItems.add(new NoKeyItem(1234, "First no key val         "));
        noKeyItems.add(new NoKeyItem(4321, "Second no key val        "));
        noKeyItems.add(new NoKeyItem(9876, "Third no key val         "));

        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching not keyed table using sequential search.");
        System.out.print("Enter id: ");
        int searchId = scanner.nextInt();

        boolean found = false;
        for (NoKeyItem item : noKeyItems) {
            if (item.id == searchId) {
                System.out.println(" Record found:");
                System.out.println("---------------");
                System.out.println("   ws-no-key-id: " + String.format("%04d", item.id));
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
