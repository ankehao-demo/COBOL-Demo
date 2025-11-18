# COBOL Merge-Sort Example and Java Migration

This directory contains a COBOL program demonstrating MERGE and SORT operations on customer data files, along with a Java migration of the same functionality.

## COBOL Version

### Overview

The COBOL program (`merge_sort_test.cbl`) demonstrates:
- Creating test data files with customer records
- Merging two input files sorted by ascending customer ID
- Sorting the merged result by descending contract ID
- File handling with status checking

### Data Structure

Customer records contain:
- Customer ID (5-digit numeric)
- Last Name (50 characters)
- First Name (50 characters)
- Contract ID (5-digit numeric)
- Comment (25 characters)

### Operations

1. **Test Data Creation**: Creates two test files (`test-file-1.txt` and `test-file-2.txt`) with sample customer records
2. **Merge Operation**: Merges the two input files sorted by ascending customer ID, outputs to `merge-output.txt`
3. **Sort Operation**: Sorts the merged file by descending contract ID, outputs to `sorted-contract-id.txt`

### Running the COBOL Program

```bash
cd merge_sort
cobc -x merge_sort_test.cbl -o merge_sort_test
./merge_sort_test
```

## Java Version

### Overview

The Java program (`MergeSortExample.java`) is a direct migration of the COBOL program, implementing the same functionality using Java's standard libraries.

### Migration Details

#### Phase 1: Data Model Migration
- Created `CustomerRecord` class with fields matching the COBOL record structure
- Used `int` for numeric fields (customer ID, contract ID)
- Used `String` for text fields (names, comment)

#### Phase 2: File I/O Migration
- Replaced COBOL file handling with Java's `BufferedReader`/`BufferedWriter`
- Used `Files.newBufferedReader()` and `Files.newBufferedWriter()` from `java.nio.file`
- Implemented `toFileString()` method to format records for file output (fixed-width format matching COBOL)
- Implemented `fromFileString()` method to parse records from file input

#### Phase 3: Sorting Logic Migration
- Replaced COBOL's `MERGE` statement with Java's `List.sort()` using `Comparator.comparingInt(CustomerRecord::getCustomerId)` for ascending customer ID sort
- Replaced COBOL's `SORT` statement with Java's `List.sort()` using `Comparator.comparingInt(CustomerRecord::getContractId).reversed()` for descending contract ID sort

#### Phase 4: Control Flow Migration
- Converted COBOL paragraphs to Java methods:
  - `create-test-data` → `createTestData()`
  - `merge-and-display-files` → `mergeAndDisplayFiles()`
  - `sort-and-display-file` → `sortAndDisplayFile()`
- Replaced COBOL's `PERFORM` statements with direct method calls in `main()`

#### Phase 5: Error Handling Migration
- Replaced COBOL's file status checking with Java's exception handling
- Used try-catch blocks to handle `IOException`
- Error messages mirror COBOL's error reporting

### Running the Java Program

```bash
cd merge_sort
javac MergeSortExample.java
java MergeSortExample
```

### Output

Both programs produce the same output:
1. Creates two test data files
2. Displays merged records sorted by customer ID (ascending)
3. Displays sorted records by contract ID (descending)

### Key Differences

- **COBOL** uses native `MERGE` and `SORT` verbs that operate directly on files
- **Java** reads files into memory, sorts using `Collections` framework, then writes back to files
- **COBOL** uses fixed-width file format automatically
- **Java** explicitly formats strings to match COBOL's fixed-width format for compatibility

### Files Generated

Both programs create the following files:
- `test-file-1.txt` - First test data file (6 records)
- `test-file-2.txt` - Second test data file (5 records)
- `merge-output.txt` - Merged result sorted by customer ID
- `sorted-contract-id.txt` - Final result sorted by contract ID

### Test Data

**File 1 (East)**: Customer IDs 1, 5, 10, 25, 50, 75
**File 2 (West)**: Customer IDs 3, 24, 30, 85, 999

**Merged (by customer ID ascending)**: 1, 3, 5, 10, 24, 25, 30, 50, 75, 85, 999

**Sorted (by contract ID descending)**: 12323, 8765, 7725, 5423, 5050, 4567, 3331, 1610, 1175, 653, 247
