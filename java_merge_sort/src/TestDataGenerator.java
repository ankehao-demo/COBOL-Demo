import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TestDataGenerator.java
 * 
 * Generates test data files matching the exact data from the COBOL merge_sort_test.cbl program.
 * Creates two test files with customer records that will be used for merging and sorting operations.
 * 
 * Test data replicates the create-test-data paragraph from lines 173-338 of merge_sort_test.cbl
 */
public class TestDataGenerator {
    
    private static final String TEST_FILE_1 = "test-file-1.txt";
    private static final String TEST_FILE_2 = "test-file-2.txt";

    /**
     * Creates both test data files with the exact data from the COBOL program.
     * 
     * @throws IOException if file writing fails
     */
    public static void createTestData() throws IOException {
        System.out.println("Creating test data files...");
        
        createTestFile1();
        createTestFile2();
        
        System.out.println("Test data files created successfully.");
    }

    /**
     * Creates test-file-1.txt with 6 customer records.
     * Data from COBOL lines 185-259 (fd-test-file-1 / f-customer-record-east)
     * 
     * @throws IOException if file writing fails
     */
    private static void createTestFile1() throws IOException {
        List<CustomerRecord> records = new ArrayList<>();
        
        records.add(new CustomerRecord(1, "last-1", "first-1", 5423, "comment-1"));
        
        records.add(new CustomerRecord(5, "last-5", "first-5", 12323, "comment-5"));
        
        records.add(new CustomerRecord(10, "last-10", "first-10", 653, "comment-10"));
        
        records.add(new CustomerRecord(50, "last-50", "first-50", 5050, "comment-50"));
        
        records.add(new CustomerRecord(25, "last-25", "first-25", 7725, "comment-25"));
        
        records.add(new CustomerRecord(75, "last-75", "first-75", 1175, "comment-75"));
        
        FileHandler.writeRecords(TEST_FILE_1, records);
    }

    /**
     * Creates test-file-2.txt with 5 customer records.
     * Data from COBOL lines 272-333 (fd-test-file-2 / f-customer-record-west)
     * 
     * @throws IOException if file writing fails
     */
    private static void createTestFile2() throws IOException {
        List<CustomerRecord> records = new ArrayList<>();
        
        records.add(new CustomerRecord(999, "last-999", "first-999", 1610, "comment-99"));
        
        records.add(new CustomerRecord(3, "last-03", "first-03", 3331, "comment-03"));
        
        records.add(new CustomerRecord(30, "last-30", "first-30", 8765, "comment-30"));
        
        records.add(new CustomerRecord(85, "last-85", "first-85", 4567, "comment-85"));
        
        records.add(new CustomerRecord(24, "last-24", "first-24", 247, "comment-24"));
        
        FileHandler.writeRecords(TEST_FILE_2, records);
    }

    /**
     * Returns the name of test file 1.
     */
    public static String getTestFile1Name() {
        return TEST_FILE_1;
    }

    /**
     * Returns the name of test file 2.
     */
    public static String getTestFile2Name() {
        return TEST_FILE_2;
    }
}
