package com.coboldemo.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Port of {@code search/search.cbl} — illustrates three kinds of COBOL
 * searches:
 * <ul>
 *     <li>{@code SEARCH ALL} on a keyed, sorted table (binary search),</li>
 *     <li>{@code SEARCH ALL} with multi-key match, and</li>
 *     <li>Sequential {@code SEARCH} on an unsorted table.</li>
 * </ul>
 */
public final class SearchExample {

    private SearchExample() {
    }

    /** Item with three sortable IDs plus descriptive fields. */
    public record Item(int id1, int id2, int id3, String name, String date) {
    }

    /** Item for the unsorted sequential search table. */
    public record NoKeyItem(int id, String value) {
    }

    public static void main(String[] args) {
        List<Item> items = setupSortedItems();
        List<NoKeyItem> unsorted = setupUnsortedItems();

        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching keyed table using binary search.");
        System.out.print("Enter id-1 to search for: ");
        int id1 = readInt(scanner);
        int idx = Collections.binarySearch(items, new Item(id1, 0, 0, "", ""),
                Comparator.comparingInt(Item::id1));
        if (idx >= 0) {
            displayFoundItem(items.get(idx));
        } else {
            System.out.println("Item not found.");
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
        Item found = items.stream()
                .filter(i -> i.id1() == q1 && i.id2() == q2 && i.id3() == q3)
                .findFirst()
                .orElse(null);
        if (found != null) {
            displayFoundItem(found);
        } else {
            System.out.println("Item not found.");
        }

        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching not keyed table using sequential search.");
        System.out.print("Enter id: ");
        int seqId = readInt(scanner);
        NoKeyItem hit = null;
        for (NoKeyItem item : unsorted) {
            if (item.id() == seqId) {
                hit = item;
                break;
            }
        }
        if (hit != null) {
            System.out.println(" Record found:");
            System.out.println("---------------");
            System.out.println("   ws-no-key-id: " + hit.id());
            System.out.println("ws-no-key-value: " + hit.value());
        } else {
            System.out.println("Item not found.");
        }
        System.out.println();
    }

    private static List<Item> setupSortedItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item(1, 101, 500, "test item 1", "2021/01/01"));
        items.add(new Item(2, 102, 499, "test item 2", "2021/02/02"));
        items.add(new Item(3, 103, 498, "test item 3", "2021/03/03"));
        return items;
    }

    private static List<NoKeyItem> setupUnsortedItems() {
        List<NoKeyItem> items = new ArrayList<>();
        items.add(new NoKeyItem(2, "Value of id 2."));
        items.add(new NoKeyItem(3, "Value of id 3."));
        items.add(new NoKeyItem(1, "Value of id 1."));
        return items;
    }

    private static int readInt(Scanner scanner) {
        String line = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
        if (line.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private static void displayFoundItem(Item item) {
        System.out.println(" Record found:");
        System.out.println("----------------");
        System.out.println("Item id-1: " + item.id1());
        System.out.println("Item id-2: " + item.id2());
        System.out.println("Item id-3: " + item.id3());
        System.out.println("Item Name: " + item.name());
        System.out.println("Item Date: " + item.date());
        System.out.println();
    }
}
