package com.coboldemo.search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Java port of search/search.cbl.
 *
 * Demonstrates two search strategies:
 * <ul>
 *   <li>COBOL {@code SEARCH ALL} (binary search) on a sorted keyed table.</li>
 *   <li>COBOL {@code SEARCH} (sequential) on an unsorted table.</li>
 * </ul>
 */
public final class SearchExample {

    private SearchExample() {
    }

    public static void main(String[] args) {
        List<Item> keyed = new ArrayList<>();
        keyed.add(new Item(1, 101, 500, "test item 1", "2021/01/01"));
        keyed.add(new Item(2, 102, 499, "test item 2", "2021/02/02"));
        keyed.add(new Item(3, 103, 498, "test item 3", "2021/03/03"));
        // Ensure SEARCH ALL preconditions (sorted by primary ascending key).
        keyed.sort(Comparator.comparingInt(Item::getId1));

        List<NoKeyItem> unkeyed = new ArrayList<>();
        unkeyed.add(new NoKeyItem(2, "Value of id 2."));
        unkeyed.add(new NoKeyItem(3, "Value of id 3."));
        unkeyed.add(new NoKeyItem(1, "Value of id 1."));

        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching keyed table using binary search.");
        System.out.print("Enter id-1 to search for: ");
        int id1 = readInt(scanner);

        Item found = binarySearchById1(keyed, id1);
        if (found != null) {
            displayItem(found);
        } else {
            System.out.println("Item not found.");
        }

        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching again with all required ids matching.");

        System.out.print("Enter id-1 to search for: ");
        id1 = readInt(scanner);
        System.out.print("Enter id-2 to search for: ");
        int id2 = readInt(scanner);
        System.out.print("Enter id-3 to search for: ");
        int id3 = readInt(scanner);

        Item match = null;
        Item candidate = binarySearchById1(keyed, id1);
        if (candidate != null
                && candidate.getId2() == id2
                && candidate.getId3() == id3) {
            match = candidate;
        }
        if (match != null) {
            displayItem(match);
        } else {
            System.out.println("Item not found.");
        }

        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching not keyed table using sequential search.");
        System.out.print("Enter id: ");
        int searchId = readInt(scanner);

        NoKeyItem sequentialMatch = null;
        for (NoKeyItem item : unkeyed) {
            if (item.getId() == searchId) {
                sequentialMatch = item;
                break;
            }
        }

        if (sequentialMatch != null) {
            System.out.println(" Record found:");
            System.out.println("---------------");
            System.out.println("   ws-no-key-id: "
                    + String.format("%04d", sequentialMatch.getId()));
            System.out.println("ws-no-key-value: "
                    + sequentialMatch.getValue());
            System.out.println();
        } else {
            System.out.println("Item not found.");
        }

        System.out.println();
    }

    private static int readInt(Scanner scanner) {
        String raw = scanner.hasNextLine() ? scanner.nextLine() : "0";
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private static Item binarySearchById1(List<Item> sorted, int target) {
        int low = 0;
        int high = sorted.size() - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midId = sorted.get(mid).getId1();
            if (midId == target) {
                return sorted.get(mid);
            } else if (midId < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return null;
    }

    private static void displayItem(Item item) {
        System.out.println(" Record found:");
        System.out.println("----------------");
        System.out.println("Item id-1: " + String.format("%04d", item.getId1()));
        System.out.println("Item id-2: " + String.format("%04d", item.getId2()));
        System.out.println("Item id-3: " + String.format("%04d", item.getId3()));
        System.out.println("Item Name: " + item.getName());
        System.out.println("Item Date: " + item.getDate());
        System.out.println();
    }

    public static final class Item {
        private final int id1;
        private final int id2;
        private final int id3;
        private final String name;
        private final String date;

        public Item(int id1, int id2, int id3, String name, String date) {
            this.id1 = id1;
            this.id2 = id2;
            this.id3 = id3;
            this.name = name;
            this.date = date;
        }

        public int getId1() {
            return id1;
        }

        public int getId2() {
            return id2;
        }

        public int getId3() {
            return id3;
        }

        public String getName() {
            return name;
        }

        public String getDate() {
            return date;
        }
    }

    public static final class NoKeyItem {
        private final int id;
        private final String value;

        public NoKeyItem(int id, String value) {
            this.id = id;
            this.value = value;
        }

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }
    }
}
