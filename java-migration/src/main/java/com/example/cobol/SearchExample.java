package com.example.cobol;

import com.example.cobol.util.SearchUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class SearchExample {
    
    static class Item {
        int id1;
        int id2;
        int id3;
        String name;
        String date;
        
        public Item(int id1, int id2, int id3, String name, String date) {
            this.id1 = id1;
            this.id2 = id2;
            this.id3 = id3;
            this.name = name;
            this.date = date;
        }
        
        @Override
        public String toString() {
            return String.format("Item id-1: %04d\nItem id-2: %04d\nItem id-3: %04d\nItem Name: %s\nItem Date: %s",
                    id1, id2, id3, name, date);
        }
    }
    
    static class NoKeyItem {
        int id;
        String value;
        
        public NoKeyItem(int id, String value) {
            this.id = id;
            this.value = value;
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        List<Item> itemTable = setupTestData();
        List<NoKeyItem> noKeyItemTable = setupNoKeyTestData();
        
        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching keyed table using binary search.");
        System.out.print("Enter id-1 to search for: ");
        int acceptId1 = scanner.nextInt();
        
        Item found = SearchUtil.binarySearchFind(itemTable, new Item(acceptId1, 0, 0, "", ""),
                Comparator.comparingInt(item -> item.id1));
        
        if (found != null) {
            displayFoundItem(found);
        } else {
            System.out.println("Item not found.");
        }
        
        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching again with all required ids matching.");
        
        System.out.print("Enter id-1 to search for: ");
        final int searchId1 = scanner.nextInt();
        
        System.out.print("Enter id-2 to search for: ");
        final int searchId2 = scanner.nextInt();
        
        System.out.print("Enter id-3 to search for: ");
        final int searchId3 = scanner.nextInt();
        
        found = SearchUtil.sequentialSearch(itemTable, item ->
                item.id1 == searchId1 && item.id2 == searchId2 && item.id3 == searchId3);
        
        if (found != null) {
            displayFoundItem(found);
        } else {
            System.out.println("Item not found.");
        }
        
        System.out.println();
        System.out.println("==================================================");
        System.out.println("Searching not keyed table using sequential search.");
        System.out.print("Enter id: ");
        int searchId = scanner.nextInt();
        
        NoKeyItem noKeyFound = SearchUtil.sequentialSearch(noKeyItemTable, item -> item.id == searchId);
        
        if (noKeyFound != null) {
            System.out.println(" Record found:");
            System.out.println("---------------");
            System.out.println("   ws-no-key-id: " + noKeyFound.id);
            System.out.println("ws-no-key-value: " + noKeyFound.value);
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
        System.out.println(item);
        System.out.println();
    }
    
    private static List<Item> setupTestData() {
        List<Item> items = new ArrayList<>();
        items.add(new Item(1, 101, 500, "test item 1", "2021/01/01"));
        items.add(new Item(2, 102, 499, "test item 2", "2021/02/02"));
        items.add(new Item(3, 103, 498, "test item 3", "2021/03/03"));
        return items;
    }
    
    private static List<NoKeyItem> setupNoKeyTestData() {
        List<NoKeyItem> items = new ArrayList<>();
        items.add(new NoKeyItem(2, "Value of id 2."));
        items.add(new NoKeyItem(3, "Value of id 3."));
        items.add(new NoKeyItem(1, "Value of id 1."));
        return items;
    }
}
