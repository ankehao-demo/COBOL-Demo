# Java COBOL Merge Sort Migration

**JIRA Ticket:** ANKEDEMO-1

## Overview

This Java application migrates the COBOL merge sort functionality from the `merge_sort/merge_sort_test.cbl` program. It performs file-based merging and sorting of customer records, maintaining the same workflow and data format as the original COBOL implementation.

## Background

The original COBOL program creates test data, merges two files by customer ID (ascending), then sorts the merged result by contract ID (descending). This Java implementation replicates that exact functionality while maintaining compatibility with the COBOL fixed-width file format.

## Functionality

The application performs the following operations in sequence:

1. **Test Data Generation**: Creates two input files with customer records
   - `test-file-1.txt` (6 records)
   - `test-file-2.txt` (5 records)

2. **Merge Operation**: Merges the two input files by ascending customer ID
   - Pre-sorts both input files (COBOL MERGE requirement)
   - Merges into `merge-output.txt` (11 records)

3. **Sort Operation**: Sorts the merged file by descending contract ID
   - Outputs to `sorted-contract-id.txt` (11 records)

## Architecture

The application consists of 5 Java classes:

### 1. `CustomerRecord.java` - Data Model
- Represents a customer record with 5 fields
- Provides fixed-width format conversion (135 characters)
- Handles parsing and formatting of COBOL-compatible records

**Record Structure:**
- Customer ID: 5 digits (positions 1-5)
- Last Name: 50 characters (positions 6-55)
- First Name: 50 characters (positions 56-105)
- Contract ID: 5 digits (positions 106-110)
- Comment: 25 characters (positions 111-135)

### 2. `TestDataGenerator.java` - Test Data Creation
- Generates exact test data from the COBOL program
- Creates `test-file-1.txt` and `test-file-2.txt`
- Replicates the `create-test-data` paragraph from the COBOL source

### 3. `FileHandler.java` - I/O Operations
- Reads customer records from fixed-width format files
- Writes customer records to fixed-width format files
- Provides error handling and file utilities

### 4. `MergeSort.java` - Core Business Logic
- `mergeFiles()`: Merges two files by customer ID (ascending)
- `sortFile()`: Sorts file by contract ID (descending)
- Replicates COBOL MERGE and SORT verb behavior

### 5. `Main.java` - Workflow Orchestration
- Entry point for the application
- Orchestrates the complete workflow
- Provides progress output and error handling

## Design Decisions

### File Format
**Decision:** Maintain COBOL fixed-width format (135 characters per record)

**Rationale:** Ensures exact functional equivalence with the COBOL program and allows for potential interoperability between Java and COBOL versions.

### Input Handling
**Decision:** Pre-sort input files before merging

**Rationale:** COBOL's MERGE verb requires pre-sorted inputs. This implementation follows that semantic requirement.

### Error Handling
**Decision:** Use Java exceptions with meaningful error messages

**Rationale:** Leverages Java's exception handling while providing clear error information.

### Performance Strategy
**Decision:** In-memory sorting (load all records into memory)

**Rationale:** Simple and efficient for the small test dataset (11 records). Can be easily extended for larger datasets if needed.

## How to Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Command-line access

### Compilation

Navigate to the `java_merge_sort` directory and compile all Java files:

```bash
cd java_merge_sort
javac src/*.java
```

### Execution

Run the Main class from the `java_merge_sort` directory:

```bash
cd java_merge_sort
java -cp src Main
```

### Expected Output

The application will:
1. Display progress messages for each operation
2. Show the contents of merged and sorted files
3. Create 4 output files in the `java_merge_sort` directory

### Generated Files

After execution, the following files will be created:

- `test-file-1.txt` - First input file (6 records, unsorted)
- `test-file-2.txt` - Second input file (5 records, unsorted)
- `merge-output.txt` - Merged file (11 records, sorted by customer ID ascending)
- `sorted-contract-id.txt` - Final sorted file (11 records, sorted by contract ID descending)

## Test Data

The application generates the exact test data from the COBOL program:

### File 1 (6 records):
- Customer IDs: 1, 5, 10, 50, 25, 75
- Contract IDs: 5423, 12323, 653, 5050, 7725, 1175

### File 2 (5 records):
- Customer IDs: 999, 3, 30, 85, 24
- Contract IDs: 1610, 3331, 8765, 4567, 247

### Expected Merge Result (sorted by customer ID ascending):
1, 3, 5, 10, 24, 25, 30, 50, 75, 85, 999

### Expected Sort Result (sorted by contract ID descending):
12323, 8765, 7725, 5423, 5050, 4567, 3331, 1610, 1175, 653, 247

## Verification

To verify the Java implementation produces the same results as the COBOL program:

1. Run the COBOL program:
   ```bash
   cd ../merge_sort
   cobc -x merge_sort_test.cbl -o merge_sort_test
   ./merge_sort_test
   ```

2. Run the Java program:
   ```bash
   cd ../java_merge_sort
   java -cp src Main
   ```

3. Compare the output files:
   ```bash
   diff merge_sort/merge-output.txt java_merge_sort/merge-output.txt
   diff merge_sort/sorted-contract-id.txt java_merge_sort/sorted-contract-id.txt
   ```

The files should be identical (or functionally equivalent with minor whitespace differences).

## Future Enhancements

Potential improvements for production use:

1. **Configurable File Paths**: Use properties file or command-line arguments
2. **Scalability**: Implement external sorting for large datasets
3. **Format Flexibility**: Support CSV, JSON, or other formats alongside fixed-width
4. **Validation**: Add data validation and constraint checking
5. **Logging**: Implement structured logging instead of console output
6. **Unit Tests**: Add comprehensive unit tests for all classes

## References

- Original COBOL source: `merge_sort/merge_sort_test.cbl`
- COBOL MERGE verb: Lines 107-110
- COBOL SORT verb: Lines 142-145
- Test data generation: Lines 173-338
