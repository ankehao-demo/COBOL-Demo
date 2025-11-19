import java.io.IOException;

/**
 * Main.java
 * 
 * Main entry point for the Java COBOL Merge Sort migration application.
 * Orchestrates the complete workflow that replicates the COBOL merge_sort_test.cbl program.
 * 
 * Workflow (matching COBOL main-procedure from lines 91-100):
 * 1. Create test data files (test-file-1.txt, test-file-2.txt)
 * 2. Merge files by ascending customer ID (merge-output.txt)
 * 3. Sort merged file by descending contract ID (sorted-contract-id.txt)
 * 
 * This is a direct Java translation of the COBOL program functionality,
 * maintaining the same file-based I/O workflow and data processing logic.
 * 
 * JIRA Ticket: ANKEDEMO-1
 * Original COBOL: merge_sort/merge_sort_test.cbl
 */
public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("=".repeat(80));
            System.out.println("Java COBOL Merge Sort Migration - ANKEDEMO-1");
            System.out.println("Replicating functionality from merge_sort_test.cbl");
            System.out.println("=".repeat(80));
            System.out.println();

            TestDataGenerator.createTestData();
            System.out.println();

            MergeSort.mergeFiles(
                TestDataGenerator.getTestFile1Name(),
                TestDataGenerator.getTestFile2Name()
            );
            System.out.println();

            MergeSort.sortFile();
            System.out.println();

            System.out.println("Done.");
            System.out.println();
            
            System.out.println("=".repeat(80));
            System.out.println("Generated Files:");
            System.out.println("  - " + TestDataGenerator.getTestFile1Name() + 
                             " (6 records, unsorted)");
            System.out.println("  - " + TestDataGenerator.getTestFile2Name() + 
                             " (5 records, unsorted)");
            System.out.println("  - " + MergeSort.getMergeOutputFile() + 
                             " (11 records, sorted by customer ID ascending)");
            System.out.println("  - " + MergeSort.getSortedOutputFile() + 
                             " (11 records, sorted by contract ID descending)");
            System.out.println("=".repeat(80));

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
