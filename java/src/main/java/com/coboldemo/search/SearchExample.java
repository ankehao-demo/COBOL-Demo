package com.coboldemo.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Migrated from: search/search.cbl
 * Original author: Erik Eriksen (2022-03-17)
 * Purpose: Demonstrates SEARCH (sequential) and SEARCH ALL (binary) on tables.
 *
 * Mapping:
 * - SEARCH ALL -> Collections.binarySearch() on a sorted list
 * - SEARCH     -> simple loop or List.stream().filter()
 */
public class SearchExample {

    // Record class mirroring the COBOL table entry
    static class Item {
        int id1;       // ascending key
        int id2;       // ascending key
        int id3;       // descending key
        String name;
        String date;

        Item(int id1, int id2, int id3, String name, String date) {
            this.id1 = id1;
            this.id2 = id2;
            this.id3 = id3;
            this.name = name;
            this.date = date;
        }

        @Override
        public String toString() {
            return String.format("id1=%d, id2=%d, id3=%d, name='%s', date='%s'",
                    id1, id2, id3, name, date);
        }
    }

    public static void main(String[] args) {
        // Populate the table (matching COBOL data)
        List<Item> table = new ArrayList<>();
        table.add(new Item(1, 10, 90, "Item-A", "2022-01-01"));
        table.add(new Item(2, 20, 80, "Item-B", "2022-02-01"));
        table.add(new Item(3, 30, 70, "Item-C", "2022-03-01"));
        table.add(new Item(4, 40, 60, "Item-D", "2022-04-01"));
        table.add(new Item(5, 50, 50, "Item-E", "2022-05-01"));
        table.add(new Item(6, 60, 40, "Item-F", "2022-06-01"));
        table.add(new Item(7, 70, 30, "Item-G", "2022-07-01"));
        table.add(new Item(8, 80, 20, "Item-H", "2022-08-01"));
        table.add(new Item(9, 90, 10, "Item-I", "2022-09-01"));

        System.out.println("SEARCH Example");
        System.out.println("==============");
        System.out.println();

        // Display all items
        System.out.println("Table contents:");
        for (int i = 0; i < table.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + table.get(i));
        }
        System.out.println();

        // --- SEARCH ALL (binary search) by id1 ---
        System.out.println("SEARCH ALL by id1 = 5:");
        int searchId1 = 5;
        // Table is sorted ascending by id1
        int index = Collections.binarySearch(table, new Item(searchId1, 0, 0, "", ""),
                Comparator.comparingInt(a -> a.id1));
        if (index >= 0) {
            System.out.println("  Found: " + table.get(index));
        } else {
            System.out.println("  Not found");
        }
        System.out.println();

        // --- SEARCH ALL with multiple key conditions ---
        System.out.println("SEARCH ALL by id1 = 3 AND id2 = 30:");
        int searchId1b = 3;
        int searchId2 = 30;
        boolean found = false;
        // Binary search on primary key, then verify secondary
        index = Collections.binarySearch(table, new Item(searchId1b, 0, 0, "", ""),
                Comparator.comparingInt(a -> a.id1));
        if (index >= 0 && table.get(index).id2 == searchId2) {
            System.out.println("  Found: " + table.get(index));
            found = true;
        }
        if (!found) {
            System.out.println("  Not found");
        }
        System.out.println();

        // --- SEARCH (sequential) - find first item where id3 < 50 ---
        System.out.println("SEARCH (sequential) for first item where id3 < 50:");
        Item result = table.stream()
                .filter(item -> item.id3 < 50)
                .findFirst()
                .orElse(null);
        if (result != null) {
            System.out.println("  Found: " + result);
        } else {
            System.out.println("  Not found");
        }
        System.out.println();

        // --- SEARCH (sequential) - find by name ---
        System.out.println("SEARCH (sequential) for name = 'Item-G':");
        String searchName = "Item-G";
        result = null;
        for (Item item : table) {
            if (searchName.equals(item.name)) {
                result = item;
                break;
            }
        }
        if (result != null) {
            System.out.println("  Found: " + result);
        } else {
            System.out.println("  Not found");
        }
    }
}
