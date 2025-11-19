import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * FileHandler.java
 * 
 * Handles file I/O operations for customer records.
 * Reads and writes customer records in fixed-width format (135 characters per record)
 * matching the COBOL file format.
 * 
 * Provides methods for:
 * - Reading customer records from files
 * - Writing customer records to files
 * - Error handling for file operations
 */
public class FileHandler {

    /**
     * Reads customer records from a file in fixed-width format.
     * 
     * @param filename Name of the file to read
     * @return List of CustomerRecord objects
     * @throws IOException if file reading fails
     */
    public static List<CustomerRecord> readRecords(String filename) throws IOException {
        List<CustomerRecord> records = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    CustomerRecord record = CustomerRecord.fromFixedWidthString(line);
                    records.add(record);
                } catch (IllegalArgumentException e) {
                    throw new IOException(
                        String.format("Error parsing line %d in file '%s': %s", 
                                    lineNumber, filename, e.getMessage()), e);
                }
            }
        } catch (IOException e) {
            throw new IOException(
                String.format("Error reading file '%s': %s", filename, e.getMessage()), e);
        }
        
        return records;
    }

    /**
     * Writes customer records to a file in fixed-width format.
     * Each record is written as a 135-character line.
     * 
     * @param filename Name of the file to write
     * @param records List of CustomerRecord objects to write
     * @throws IOException if file writing fails
     */
    public static void writeRecords(String filename, List<CustomerRecord> records) 
            throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (CustomerRecord record : records) {
                writer.write(record.toFixedWidthString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IOException(
                String.format("Error writing to file '%s': %s", filename, e.getMessage()), e);
        }
    }

    /**
     * Displays the contents of a file by reading and printing all records.
     * Used for debugging and verification purposes.
     * 
     * @param filename Name of the file to display
     * @throws IOException if file reading fails
     */
    public static void displayFile(String filename) throws IOException {
        List<CustomerRecord> records = readRecords(filename);
        
        System.out.println("\nContents of " + filename + ":");
        System.out.println("=" .repeat(80));
        
        for (CustomerRecord record : records) {
            System.out.println(record.toFixedWidthString());
        }
        
        System.out.println("=" .repeat(80));
        System.out.println("Total records: " + records.size());
    }

    /**
     * Checks if a file exists and is readable.
     * 
     * @param filename Name of the file to check
     * @return true if file exists and is readable, false otherwise
     */
    public static boolean fileExists(String filename) {
        java.io.File file = new java.io.File(filename);
        return file.exists() && file.canRead();
    }

    /**
     * Deletes a file if it exists.
     * Used for cleanup operations.
     * 
     * @param filename Name of the file to delete
     * @return true if file was deleted, false otherwise
     */
    public static boolean deleteFile(String filename) {
        java.io.File file = new java.io.File(filename);
        return file.delete();
    }
}
