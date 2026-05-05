package com.coboldemo.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Java port of {@code search/search.cbl}.
 *
 * Demonstrates COBOL's SEARCH ALL (binary search) and SEARCH (sequential)
 * verbs. The keyed table requires the data to be sorted on the indexed
 * key, so we sort the list before binary searching it. The sequential
 * search uses a plain stream filter.
 */
public class SearchExample {

    public static final class Item {
        public final int id1;
        public final int id2;
        public final int id3;
        public final String name;
        public final String date;

        public Item(int id1, int id2, int id3, String name, String date) {
            this.id1 = id1;
            this.id2 = id2;
            this.id3 = id3;
            this.name = name;
            this.date = date;
        }
    }

    public static final class NoKeyItem {
        public final int id;
        public final String value;

        public NoKeyItem(int id, String value) {
            this.id = id;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        List<Item> items = setupTestData();
        List<NoKeyItem> noKeyItems = setupNoKeyData();
        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching keyed table using binary search.");
        System.out.print("Enter id-1 to search for: ");
        int id1 = readInt(scanner);

        // SEARCH ALL = binary search on a sorted, indexed key.
        List<Item> sorted = new ArrayList<>(items);
        sorted.sort(Comparator.comparingInt((Item it) -> it.id1));
        Item probe = new Item(id1, 0, 0, "", "");
        int idx = Collections.binarySearch(sorted, probe,
                Comparator.comparingInt(it -> it.id1));
        if (idx < 0) {
            System.out.println("Item not found.");
        } else {
            displayFoundItem(sorted.get(idx));
        }

        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching again with all required ids matching.");
        System.out.print("Enter id-1 to search for: ");
        int q1 = readInt(scanner);
        System.out.print("Enter id-2 to search for: ");
        int q2 = readInt(scanner);
        System.out.print("Enter id-3 to search for: ");
        int q3 = readInt(scanner);

        Item match = sorted.stream()
                .filter(it -> it.id1 == q1 && it.id2 == q2 && it.id3 == q3)
                .findFirst()
                .orElse(null);
        if (match == null) {
            System.out.println("Item not found.");
        } else {
            displayFoundItem(match);
        }

        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching not keyed table using sequential search.");
        System.out.print("Enter id: ");
        int seqId = readInt(scanner);
        NoKeyItem found = noKeyItems.stream()
                .filter(it -> it.id == seqId)
                .findFirst()
                .orElse(null);
        if (found == null) {
            System.out.println("Item not found.");
        } else {
            System.out.println(" Record found:");
            System.out.println("---------------");
            System.out.println("   ws-no-key-id: " + String.format("%04d", found.id));
            System.out.println("ws-no-key-value: " + found.value);
            System.out.println();
        }
    }

    private static int readInt(Scanner scanner) {
        if (!scanner.hasNextLine()) {
            return 0;
        }
        String line = scanner.nextLine().trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static List<Item> setupTestData() {
        List<Item> items = new ArrayList<>();
        items.add(new Item(1, 101, 500, "test item 1", "2021/01/01"));
        items.add(new Item(2, 102, 499, "test item 2", "2021/02/02"));
        items.add(new Item(3, 103, 498, "test item 3", "2021/03/03"));
        return items;
    }

    private static List<NoKeyItem> setupNoKeyData() {
        List<NoKeyItem> items = new ArrayList<>();
        items.add(new NoKeyItem(2, "Value of id 2."));
        items.add(new NoKeyItem(3, "Value of id 3."));
        items.add(new NoKeyItem(1, "Value of id 1."));
        return items;
    }

    private static void displayFoundItem(Item it) {
        System.out.println(" Record found:");
        System.out.println("----------------");
        System.out.println("Item id-1: " + String.format("%04d", it.id1));
        System.out.println("Item id-2: " + String.format("%04d", it.id2));
        System.out.println("Item id-3: " + String.format("%04d", it.id3));
        System.out.println("Item Name: " + it.name);
        System.out.println("Item Date: " + it.date);
        System.out.println();
    }
}
