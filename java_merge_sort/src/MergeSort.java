import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * MergeSort.java
 * 
 * Implements merge and sort operations for customer records, replicating the functionality
 * of the COBOL MERGE and SORT verbs from merge_sort_test.cbl.
 * 
 * Key operations:
 * 1. mergeFiles() - Merges two input files by ascending customer ID
 * 2. sortFile() - Sorts a file by descending contract ID
 * 
 * Note: COBOL's MERGE verb requires pre-sorted input files, so this implementation
 * pre-sorts both input files before merging them.
 */
public class MergeSort {
    
    private static final String MERGE_OUTPUT_FILE = "merge-output.txt";
    private static final String SORTED_OUTPUT_FILE = "sorted-contract-id.txt";

    /**
     * Merges two input files by ascending customer ID.
     * 
     * This method replicates the COBOL MERGE operation from lines 107-110:
     *   merge fd-sorting-file
     *       on ascending key f-customer-id
     *       of f-customer-record-merged
     *       using fd-test-file-1 fd-test-file-2 giving fd-merged-file
     * 
     * COBOL's MERGE verb requires pre-sorted inputs, so we:
     * 1. Read both input files
     * 2. Sort each by customer ID (ascending)
     * 3. Merge the sorted lists
     * 4. Write to output file
     * 
     * @param inputFile1 First input file name
     * @param inputFile2 Second input file name
     * @throws IOException if file operations fail
     */
    public static void mergeFiles(String inputFile1, String inputFile2) throws IOException {
        System.out.println("Merging and sorting files...");
        
        List<CustomerRecord> records1 = FileHandler.readRecords(inputFile1);
        List<CustomerRecord> records2 = FileHandler.readRecords(inputFile2);
        
        records1.sort(Comparator.comparingInt(CustomerRecord::getCustomerId));
        records2.sort(Comparator.comparingInt(CustomerRecord::getCustomerId));
        
        List<CustomerRecord> mergedRecords = mergeSortedLists(records1, records2);
        
        FileHandler.writeRecords(MERGE_OUTPUT_FILE, mergedRecords);
        
        System.out.println("Files merged successfully to " + MERGE_OUTPUT_FILE);
        
        displayRecords(mergedRecords);
    }

    /**
     * Merges two pre-sorted lists into a single sorted list.
     * Uses the classic two-pointer merge algorithm.
     * 
     * @param list1 First sorted list
     * @param list2 Second sorted list
     * @return Merged sorted list
     */
    private static List<CustomerRecord> mergeSortedLists(
            List<CustomerRecord> list1, List<CustomerRecord> list2) {
        
        List<CustomerRecord> merged = new ArrayList<>();
        int i = 0, j = 0;
        
        while (i < list1.size() && j < list2.size()) {
            if (list1.get(i).getCustomerId() <= list2.get(j).getCustomerId()) {
                merged.add(list1.get(i));
                i++;
            } else {
                merged.add(list2.get(j));
                j++;
            }
        }
        
        while (i < list1.size()) {
            merged.add(list1.get(i));
            i++;
        }
        
        while (j < list2.size()) {
            merged.add(list2.get(j));
            j++;
        }
        
        return merged;
    }

    /**
     * Sorts the merged file by descending contract ID.
     * 
     * This method replicates the COBOL SORT operation from lines 142-145:
     *   sort fd-sorting-file
     *       on descending key f-customer-contract-id
     *       of f-customer-record-sorted-contract-id
     *       using fd-merged-file giving fd-sorted-contract-id
     * 
     * @throws IOException if file operations fail
     */
    public static void sortFile() throws IOException {
        System.out.println("Sorting merged file on descending contract id....");
        
        List<CustomerRecord> records = FileHandler.readRecords(MERGE_OUTPUT_FILE);
        
        records.sort(Comparator.comparingInt(CustomerRecord::getContractId).reversed());
        
        FileHandler.writeRecords(SORTED_OUTPUT_FILE, records);
        
        System.out.println("File sorted successfully to " + SORTED_OUTPUT_FILE);
        
        displayRecords(records);
    }

    /**
     * Displays customer records to console.
     * Matches the COBOL display behavior from the original program.
     * 
     * @param records List of records to display
     */
    private static void displayRecords(List<CustomerRecord> records) {
        for (CustomerRecord record : records) {
            System.out.println(record.toFixedWidthString());
        }
    }

    /**
     * Returns the name of the merge output file.
     */
    public static String getMergeOutputFile() {
        return MERGE_OUTPUT_FILE;
    }

    /**
     * Returns the name of the sorted output file.
     */
    public static String getSortedOutputFile() {
        return SORTED_OUTPUT_FILE;
    }
}
