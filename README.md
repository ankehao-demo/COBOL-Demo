# COBOL Examples
This is a collection of example and test COBOL programs I've written. I'm currently in the process of updating
each folder with a README.md file and more comments so that the examples are easier to follow along with.

All program were written using [GnuCOBOL](https://gnucobol.sourceforge.io/) in Linux.

---

## Java Migration

This project also contains Java equivalents of all 24 GnuCOBOL example programs, preserving the same directory-per-topic structure and functional behavior. Each example is a standalone runnable class.

### Prerequisites

- **Java 17+** (JDK)
- **Apache Maven 3.8+**
- **PostgreSQL** (only for the SQL/database example)

### Build

```bash
mvn clean package
```

### Run Examples

Each class has a `main` method and can be run individually:

```bash
# Copy dependencies for standalone execution
mvn dependency:copy-dependencies -DoutputDirectory=target/dependency

# General pattern
java -cp target/classes:target/dependency/* com.coboldemo.<package>.<ClassName>

# Examples
java -cp target/classes:target/dependency/* com.coboldemo.io.AcceptFromExample --test
java -cp target/classes:target/dependency/* com.coboldemo.strings.TrimExample
java -cp target/classes:target/dependency/* com.coboldemo.serialization.JsonGenerateExample
```

### Run Tests

```bash
mvn test
```

### Modules

| # | Package | Classes | COBOL Source |
|---|---------|---------|--------------|
| 1 | `com.coboldemo.io` | AcceptExample, AcceptFromExample, AcceptSecure, DisplayTest, DisplayTiming, ReadCommandArgs, ReadSpecificCommandArgs, GetScreenSize | `accept/`, `display_test/`, `display_timing/`, `read_command_args/`, `screen_size/` |
| 2 | `com.coboldemo.datatypes` | CompConversionTest, IsNumericExample, NumvalTest | `comp_test/`, `is_numeric/`, `numval_test/` |
| 3 | `com.coboldemo.datastructures` | RedefinesExample, SearchExample | `redifines/`, `search/` |
| 4 | `com.coboldemo.strings` | TrimExample, UnstringExample | `trim/`, `unstring/` |
| 5 | `com.coboldemo.modular` | MainApp, SubApp | `sub_program/` |
| 6 | `com.coboldemo.fileio` | MergeSortExample, ReportWriterExample | `merge_sort/`, `report_writer/` |
| 7 | `com.coboldemo.database` | SqlExample | `sql/` |
| 8 | `com.coboldemo.serialization` | JsonGenerateExample, XmlGenerateExample | `json_generate/`, `xml_generate/` |
| 9 | `com.coboldemo.tui` | MouseExample | `mouse/` |

### COBOL-to-Java Mapping Summary

| COBOL Feature | Java Equivalent |
|---------------|-----------------|
| `ACCEPT` | `Scanner(System.in)` |
| `ACCEPT FROM COMMAND-LINE` | `String.join(" ", args)` |
| `ACCEPT FROM DATE/TIME` | `java.time.LocalDate.now()` / `LocalTime.now()` |
| `ACCEPT ... SECURE` | `Console.readPassword()` |
| `DISPLAY` | `System.out.println()` |
| `PIC 999 COMP` | `int` |
| `IS NUMERIC` | `Character.isDigit()` / regex |
| `NUMVAL` | `Double.parseDouble()` |
| `REDEFINES` | Inheritance / type discriminator |
| `SEARCH ALL` | `Arrays.binarySearch()` |
| `SEARCH` | Linear loop / `Stream.filter()` |
| `FUNCTION TRIM` | `String.strip()` / `stripLeading()` / `stripTrailing()` |
| `UNSTRING` | Custom utility with pointer tracking |
| `CALL BY CONTENT` | Pass value copies |
| `CALL BY REFERENCE` | Pass mutable array/wrapper |
| `CANCEL` | Create new instance |
| `SORT` / `MERGE` | `List.sort()` with `Comparator` |
| Report Writer | Imperative `PrintWriter` with headers |
| `EXEC SQL` | JDBC `PreparedStatement` / `ResultSet` |
| `JSON GENERATE` | Jackson `ObjectMapper` |
| `XML GENERATE` | JAXB marshalling |
| Mouse/screen mode | Lanterna `Terminal` |

### Database Setup (SQL Example Only)

The SQL example requires a PostgreSQL database. Use the provided script:

```bash
psql -U postgres -f sql/create_test_db.sql
```

Set connection details via environment variables:

```bash
export JDBC_URL=jdbc:postgresql://localhost:5432/cobol_db_example
export DB_USER=postgres
export DB_PASSWORD=password
```

### Original COBOL Files

The original GnuCOBOL source files are preserved in their original directories for reference.    



