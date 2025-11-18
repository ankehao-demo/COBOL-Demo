# Java Implementation of COBOL Merge-Sort

This directory contains a Java implementation that replicates the functionality of the COBOL merge-sort program located at `../merge_sort/merge_sort_test.cbl`.

## Overview

This Java implementation demonstrates the same merge and sort operations as the original COBOL program:

1. **Create Test Data**: Generates two input files with customer records (test-file-1.txt and test-file-2.txt)
2. **Merge and Sort**: Merges the two files and sorts by ascending customer ID, writing to merge-output.txt
3. **Re-sort**: Sorts the merged file by descending contract ID, writing to sorted-contract-id.txt

## Customer Record Structure

Each customer record contains five fields:
- **Customer ID**: 5-digit integer
- **Last Name**: String (up to 50 characters)
- **First Name**: String (up to 50 characters)
- **Contract ID**: 5-digit integer
- **Comment**: String (up to 25 characters)

## Project Structure

```
merge_sort_java/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── example/
│   │               └── mergesort/
│   │                   ├── CustomerRecord.java      # Data model with Comparable interface
│   │                   ├── FileIO.java              # File I/O and test data generation
│   │                   ├── MergeSort.java           # Merge and sort operations
│   │                   └── MergeSortExample.java    # Main program
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── mergesort/
│                       ├── CustomerRecordTest.java  # Unit tests for CustomerRecord
│                       ├── FileIOTest.java          # Unit tests for FileIO
│                       └── MergeSortTest.java       # Unit tests for merge/sort operations
└── README.md
```

## Compilation

### Compile Main Classes

```bash
cd merge_sort_java
find src/main/java -name "*.java" | xargs javac -d .
```

### Compile Tests (requires JUnit 4)

```bash
javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar -d . src/test/java/com/example/mergesort/*.java
```

## Running the Program

After compilation, run the main program:

```bash
cd merge_sort_java
java com.example.mergesort.MergeSortExample
```

### Expected Output

The program will:
1. Display "Creating test data files..."
2. Display "Merging and sorting files..." followed by 11 customer records sorted by ascending customer ID
3. Display "Sorting merged file on descending contract id...." followed by the same 11 records sorted by descending contract ID
4. Display "Done."

### Generated Files

The program creates the following files:
- `test-file-1.txt` - East region customer records (6 records)
- `test-file-2.txt` - West region customer records (5 records)
- `merge-output.txt` - Merged and sorted by customer ID (11 records)
- `sorted-contract-id.txt` - Sorted by descending contract ID (11 records)

## Running Tests

To run the unit tests (requires JUnit 4):

```bash
java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore com.example.mergesort.CustomerRecordTest
java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore com.example.mergesort.FileIOTest
java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore com.example.mergesort.MergeSortTest
```

## Implementation Details

### Phase 1: Data Model
- `CustomerRecord` class implements `Comparable<CustomerRecord>` for natural ordering by customer ID
- Includes constructors, getters, setters, and `toString()` method
- Provides `fromString()` static method for parsing fixed-width format records

### Phase 2: File I/O
- `FileIO` class handles reading and writing customer records
- `readRecords()` reads records from a file into a List
- `writeRecords()` writes a List of records to a file
- `createTestData()` generates the same test data as the COBOL program

### Phase 3: Merge Operation
- `mergeAndDisplayFiles()` reads two input files, merges them, and sorts by ascending customer ID
- Uses Java's `Collections.sort()` with the natural ordering defined in `CustomerRecord`
- Displays all merged records to console

### Phase 4: Sort Operation
- `sortAndDisplayFile()` reads the merged file and sorts by descending contract ID
- Uses `Collections.sort()` with a custom `Comparator`
- Displays all sorted records to console

### Phase 5: Main Program
- `MergeSortExample` orchestrates the execution flow
- Calls methods in sequence: create test data → merge files → sort merged file
- Includes exception handling and error reporting

### Phase 6: Testing
- Comprehensive unit tests for all components
- Tests cover normal operations, edge cases, and error conditions
- Validates round-trip serialization/deserialization

## Differences from COBOL Implementation

While the Java implementation produces equivalent output, there are some implementation differences:

1. **Sorting Mechanism**: COBOL uses built-in `MERGE` and `SORT` statements that work directly with files, while Java uses in-memory collections with `Collections.sort()`
2. **Memory Model**: COBOL works with file descriptors and work files, while Java loads records into memory
3. **Type System**: COBOL uses fixed-width fields with PIC clauses, while Java uses standard data types with formatting in `toString()`
4. **Error Handling**: Java uses exceptions, while COBOL uses file status codes

## Comparison with Original COBOL Program

The original COBOL program is located at `../merge_sort/merge_sort_test.cbl` and was authored by Erik Eriksen on 2021-09-19.

Both implementations:
- Create identical test data (6 east records + 5 west records)
- Merge and sort by ascending customer ID
- Re-sort by descending contract ID
- Display results to console
- Generate the same output files

## Author

This Java implementation was created as a migration of the original COBOL merge-sort example, maintaining functional equivalence while demonstrating modern Java programming practices.
