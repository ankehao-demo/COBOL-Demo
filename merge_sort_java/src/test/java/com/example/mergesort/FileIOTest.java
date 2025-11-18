package com.example.mergesort;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileIOTest {

    private static final String TEST_FILE = "test-fileio.txt";

    @Before
    public void setUp() {
        cleanupTestFile();
    }

    @After
    public void tearDown() {
        cleanupTestFile();
    }

    private void cleanupTestFile() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testWriteAndReadRecords() throws IOException {
        List<CustomerRecord> records = new ArrayList<>();
        records.add(new CustomerRecord(1, "Smith", "John", 5423, "Comment1"));
        records.add(new CustomerRecord(5, "Doe", "Jane", 1234, "Comment2"));
        records.add(new CustomerRecord(10, "Jones", "Bob", 9999, "Comment3"));

        FileIO.writeRecords(TEST_FILE, records);

        List<CustomerRecord> readRecords = FileIO.readRecords(TEST_FILE);

        assertEquals(3, readRecords.size());
        assertEquals(1, readRecords.get(0).getCustomerId());
        assertEquals("Smith", readRecords.get(0).getLastName());
        assertEquals(5, readRecords.get(1).getCustomerId());
        assertEquals("Doe", readRecords.get(1).getLastName());
        assertEquals(10, readRecords.get(2).getCustomerId());
        assertEquals("Jones", readRecords.get(2).getLastName());
    }

    @Test
    public void testWriteEmptyList() throws IOException {
        List<CustomerRecord> records = new ArrayList<>();
        FileIO.writeRecords(TEST_FILE, records);

        List<CustomerRecord> readRecords = FileIO.readRecords(TEST_FILE);
        assertEquals(0, readRecords.size());
    }

    @Test
    public void testCreateTestData() throws IOException {
        FileIO.createTestData();

        File file1 = new File("test-file-1.txt");
        File file2 = new File("test-file-2.txt");

        assertTrue(file1.exists());
        assertTrue(file2.exists());

        List<CustomerRecord> eastRecords = FileIO.readRecords("test-file-1.txt");
        List<CustomerRecord> westRecords = FileIO.readRecords("test-file-2.txt");

        assertEquals(6, eastRecords.size());
        assertEquals(5, westRecords.size());

        assertEquals(1, eastRecords.get(0).getCustomerId());
        assertEquals(5423, eastRecords.get(0).getContractId());

        assertEquals(999, westRecords.get(0).getCustomerId());
        assertEquals(1610, westRecords.get(0).getContractId());

        file1.delete();
        file2.delete();
    }

    @Test(expected = IOException.class)
    public void testReadNonExistentFile() throws IOException {
        FileIO.readRecords("nonexistent-file.txt");
    }
}
