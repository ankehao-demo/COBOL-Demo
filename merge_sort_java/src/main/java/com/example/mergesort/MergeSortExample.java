package com.example.mergesort;

import java.io.IOException;

public class MergeSortExample {

    public static void main(String[] args) {
        try {
            FileIO.createTestData();
            
            MergeSort.mergeAndDisplayFiles("test-file-1.txt", "test-file-2.txt", "merge-output.txt");
            
            MergeSort.sortAndDisplayFile("merge-output.txt", "sorted-contract-id.txt");
            
            System.out.println("Done.");
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
