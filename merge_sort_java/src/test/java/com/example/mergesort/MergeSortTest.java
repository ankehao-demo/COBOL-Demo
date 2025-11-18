package com.example.mergesort;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MergeSortTest {

    private static final String TEST_FILE_1 = "test-merge-1.txt";
    private static final String TEST_FILE_2 = "test-merge-2.txt";
    private static final String MERGE_OUTPUT = "test-merge-output.txt";
    private static final String SORT_OUTPUT = "test-sort-output.txt";

    @Before
    public void setUp() throws IOException {
        cleanupTestFiles();
        createTestFiles();
    }

    @After
    public void tearDown() {
        cleanupTestFiles();
    }

    private void cleanupTestFiles() {
        new File(TEST_FILE_1).delete();
        new File(TEST_FILE_2).delete();
        new File(MERGE_OUTPUT).delete();
        new File(SORT_OUTPUT).delete();
    }

    private void createTestFiles() throws IOException {
        java.util.List<CustomerRecord> records1 = new java.util.ArrayList<>();
        records1.add(new CustomerRecord(5, "last-5", "first-5", 100, "comment-5"));
        records1.add(new CustomerRecord(1, "last-1", "first-1", 300, "comment-1"));
        records1.add(new CustomerRecord(10, "last-10", "first-10", 200, "comment-10"));

        java.util.List<CustomerRecord> records2 = new java.util.ArrayList<>();
        records2.add(new CustomerRecord(3, "last-3", "first-3", 500, "comment-3"));
        records2.add(new CustomerRecord(7, "last-7", "first-7", 400, "comment-7"));

        FileIO.writeRecords(TEST_FILE_1, records1);
        FileIO.writeRecords(TEST_FILE_2, records2);
    }

    @Test
    public void testMergeAndDisplayFiles() throws IOException {
        MergeSort.mergeAndDisplayFiles(TEST_FILE_1, TEST_FILE_2, MERGE_OUTPUT);

        File outputFile = new File(MERGE_OUTPUT);
        assertTrue(outputFile.exists());

        List<CustomerRecord> mergedRecords = FileIO.readRecords(MERGE_OUTPUT);
        assertEquals(5, mergedRecords.size());

        assertEquals(1, mergedRecords.get(0).getCustomerId());
        assertEquals(3, mergedRecords.get(1).getCustomerId());
        assertEquals(5, mergedRecords.get(2).getCustomerId());
        assertEquals(7, mergedRecords.get(3).getCustomerId());
        assertEquals(10, mergedRecords.get(4).getCustomerId());
    }

    @Test
    public void testSortAndDisplayFile() throws IOException {
        MergeSort.mergeAndDisplayFiles(TEST_FILE_1, TEST_FILE_2, MERGE_OUTPUT);
        MergeSort.sortAndDisplayFile(MERGE_OUTPUT, SORT_OUTPUT);

        File outputFile = new File(SORT_OUTPUT);
        assertTrue(outputFile.exists());

        List<CustomerRecord> sortedRecords = FileIO.readRecords(SORT_OUTPUT);
        assertEquals(5, sortedRecords.size());

        assertEquals(500, sortedRecords.get(0).getContractId());
        assertEquals(400, sortedRecords.get(1).getContractId());
        assertEquals(300, sortedRecords.get(2).getContractId());
        assertEquals(200, sortedRecords.get(3).getContractId());
        assertEquals(100, sortedRecords.get(4).getContractId());
    }

    @Test
    public void testMergePreservesAllRecords() throws IOException {
        MergeSort.mergeAndDisplayFiles(TEST_FILE_1, TEST_FILE_2, MERGE_OUTPUT);

        List<CustomerRecord> records1 = FileIO.readRecords(TEST_FILE_1);
        List<CustomerRecord> records2 = FileIO.readRecords(TEST_FILE_2);
        List<CustomerRecord> mergedRecords = FileIO.readRecords(MERGE_OUTPUT);

        assertEquals(records1.size() + records2.size(), mergedRecords.size());
    }

    @Test
    public void testSortPreservesAllRecords() throws IOException {
        MergeSort.mergeAndDisplayFiles(TEST_FILE_1, TEST_FILE_2, MERGE_OUTPUT);
        
        List<CustomerRecord> beforeSort = FileIO.readRecords(MERGE_OUTPUT);
        
        MergeSort.sortAndDisplayFile(MERGE_OUTPUT, SORT_OUTPUT);
        
        List<CustomerRecord> afterSort = FileIO.readRecords(SORT_OUTPUT);

        assertEquals(beforeSort.size(), afterSort.size());
    }
}
