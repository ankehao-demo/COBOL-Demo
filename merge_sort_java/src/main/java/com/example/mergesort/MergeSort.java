package com.example.mergesort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MergeSort {

    public static void mergeAndDisplayFiles(String file1, String file2, String outputFile) throws IOException {
        System.out.println("Merging and sorting files...");
        
        List<CustomerRecord> records1 = FileIO.readRecords(file1);
        List<CustomerRecord> records2 = FileIO.readRecords(file2);
        
        List<CustomerRecord> mergedRecords = new ArrayList<>();
        mergedRecords.addAll(records1);
        mergedRecords.addAll(records2);
        
        Collections.sort(mergedRecords);
        
        FileIO.writeRecords(outputFile, mergedRecords);
        
        for (CustomerRecord record : mergedRecords) {
            System.out.println(record);
        }
    }

    public static void sortAndDisplayFile(String inputFile, String outputFile) throws IOException {
        System.out.println("Sorting merged file on descending contract id....");
        
        List<CustomerRecord> records = FileIO.readRecords(inputFile);
        
        Collections.sort(records, new Comparator<CustomerRecord>() {
            @Override
            public int compare(CustomerRecord r1, CustomerRecord r2) {
                return Integer.compare(r2.getContractId(), r1.getContractId());
            }
        });
        
        FileIO.writeRecords(outputFile, records);
        
        for (CustomerRecord record : records) {
            System.out.println(record);
        }
    }
}
