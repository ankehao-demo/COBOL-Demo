package com.coboldemo.fileio;

import com.coboldemo.fileio.MergeSortExample.CustomerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MergeSortExampleTest {

    @TempDir
    Path tempDir;

    @Test
    void customerRecordFixedWidthRoundTrip() {
        CustomerRecord original = new CustomerRecord(1, "last-1", "first-1", 5423, "comment-1");
        String fixed = original.toFixedWidth();
        assertEquals(135, fixed.length());

        CustomerRecord parsed = CustomerRecord.fromFixedWidth(fixed);
        assertEquals(original.id, parsed.id);
        assertEquals(original.contractId, parsed.contractId);
    }

    @Test
    void writeAndReadRecords() throws IOException {
        Path file = tempDir.resolve("test.txt");
        List<CustomerRecord> records = List.of(
                new CustomerRecord(1, "last-1", "first-1", 100, "c1"),
                new CustomerRecord(2, "last-2", "first-2", 200, "c2")
        );

        MergeSortExample.writeRecords(file, records);
        List<CustomerRecord> read = MergeSortExample.readRecords(file);

        assertEquals(2, read.size());
        assertEquals(1, read.get(0).id);
        assertEquals(2, read.get(1).id);
    }

    @Test
    void mergeAndSortIntegration() throws IOException {
        Path file1 = tempDir.resolve("east.txt");
        Path file2 = tempDir.resolve("west.txt");
        Path merged = tempDir.resolve("merged.txt");

        MergeSortExample.writeRecords(file1, List.of(
                new CustomerRecord(1, "a", "a", 100, "c"),
                new CustomerRecord(5, "b", "b", 200, "c")
        ));
        MergeSortExample.writeRecords(file2, List.of(
                new CustomerRecord(3, "c", "c", 150, "c"),
                new CustomerRecord(10, "d", "d", 50, "c")
        ));

        MergeSortExample.mergeAndDisplayFiles(file1, file2, merged);
        List<CustomerRecord> mergedRecords = MergeSortExample.readRecords(merged);

        assertEquals(4, mergedRecords.size());
        assertEquals(1, mergedRecords.get(0).id);
        assertEquals(3, mergedRecords.get(1).id);
        assertEquals(5, mergedRecords.get(2).id);
        assertEquals(10, mergedRecords.get(3).id);
    }

    @Test
    void sortByContractIdDescending() throws IOException {
        Path input = tempDir.resolve("input.txt");
        Path sorted = tempDir.resolve("sorted.txt");

        MergeSortExample.writeRecords(input, List.of(
                new CustomerRecord(1, "a", "a", 100, "c"),
                new CustomerRecord(2, "b", "b", 300, "c"),
                new CustomerRecord(3, "c", "c", 200, "c")
        ));

        MergeSortExample.sortAndDisplayFile(input, sorted);
        List<CustomerRecord> sortedRecords = MergeSortExample.readRecords(sorted);

        assertEquals(3, sortedRecords.size());
        assertEquals(300, sortedRecords.get(0).contractId);
        assertEquals(200, sortedRecords.get(1).contractId);
        assertEquals(100, sortedRecords.get(2).contractId);
    }
}
