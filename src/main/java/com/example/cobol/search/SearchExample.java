package com.example.cobol.search;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Java port of {@code search/search.cbl}.
 *
 * <p>Demonstrates COBOL {@code SEARCH ALL} (binary search, requires a sorted
 * key) and {@code SEARCH} (linear scan, no sort required). The Java
 * equivalents are {@link Arrays#binarySearch(Object[], Object, Comparator)}
 * and a manual loop respectively.
 */
public final class SearchExample {

    /** Item with three int IDs, a name, and a date. */
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

    /** Item with a single id + value, used for the linear-search demo. */
    public static final class NoKeyItem {
        public final int id;
        public final String value;

        public NoKeyItem(int id, String value) {
            this.id = id;
            this.value = value;
        }
    }

    private SearchExample() {}

    public static void main(String[] args) {
        Item[] keyed = new Item[] {
            new Item(1, 101, 500, "test item 1", "2021/01/01"),
            new Item(2, 102, 499, "test item 2", "2021/02/02"),
            new Item(3, 103, 498, "test item 3", "2021/03/03"),
        };
        // SEARCH ALL requires the table to be sorted by the key. Java
        // binarySearch enforces the same precondition.
        Arrays.sort(keyed, Comparator.comparingInt((Item it) -> it.id1));

        NoKeyItem[] unkeyed = new NoKeyItem[] {
            new NoKeyItem(2, "Value of id 2."),
            new NoKeyItem(3, "Value of id 3."),
            new NoKeyItem(1, "Value of id 1."),
        };

        try (Scanner in = new Scanner(System.in)) {
            System.out.println();
            System.out.println("==================================================");
            System.out.println("Searching keyed table using binary search.");
            System.out.print("Enter id-1 to search for: ");
            int id1 = readInt(in);
            int idx = Arrays.binarySearch(keyed,
                    new Item(id1, 0, 0, "", ""),
                    Comparator.comparingInt(it -> it.id1));
            if (idx < 0) {
                System.out.println("Item not found.");
            } else {
                displayFoundItem(keyed[idx]);
            }

            System.out.println();
            System.out.println("==================================================");
            System.out.println("Searching again with all required ids matching.");
            System.out.print("Enter id-1 to search for: ");
            id1 = readInt(in);
            System.out.print("Enter id-2 to search for: ");
            int id2 = readInt(in);
            System.out.print("Enter id-3 to search for: ");
            int id3 = readInt(in);

            // First narrow with binary search by id1, then check secondary keys.
            int candidate = Arrays.binarySearch(keyed,
                    new Item(id1, 0, 0, "", ""),
                    Comparator.comparingInt(it -> it.id1));
            Item match = null;
            if (candidate >= 0
                    && keyed[candidate].id2 == id2
                    && keyed[candidate].id3 == id3) {
                match = keyed[candidate];
            }
            if (match == null) {
                System.out.println("Item not found.");
            } else {
                displayFoundItem(match);
            }

            // Linear search over the unkeyed table.
            System.out.println();
            System.out.println("==================================================");
            System.out.println("Searching not keyed table using sequential search.");
            System.out.print("Enter id: ");
            int searchId = readInt(in);

            NoKeyItem hit = null;
            for (NoKeyItem item : unkeyed) {
                if (item.id == searchId) {
                    hit = item;
                    break;
                }
            }
            if (hit == null) {
                System.out.println("Item not found.");
            } else {
                System.out.println(" Record found:");
                System.out.println("---------------");
                System.out.println("   ws-no-key-id: " + hit.id);
                System.out.println("ws-no-key-value: " + hit.value);
                System.out.println();
            }
        }
    }

    private static void displayFoundItem(Item it) {
        System.out.println(" Record found:");
        System.out.println("----------------");
        System.out.println("Item id-1: " + it.id1);
        System.out.println("Item id-2: " + it.id2);
        System.out.println("Item id-3: " + it.id3);
        System.out.println("Item Name: " + it.name);
        System.out.println("Item Date: " + it.date);
        System.out.println();
    }

    private static int readInt(Scanner in) {
        if (!in.hasNextLine()) {
            return 0;
        }
        try {
            return Integer.parseInt(in.nextLine().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
