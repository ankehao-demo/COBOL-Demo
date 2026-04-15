# COBOL Demo — Java Migration

Java equivalents of 24 GnuCOBOL demo programs, preserving the original folder-per-topic organization. Each COBOL program has been faithfully translated to idiomatic Java 17, with detailed comments explaining the COBOL-to-Java mapping.

## Prerequisites

- **JDK 17+** (Oracle JDK, OpenJDK, or Eclipse Temurin)
- **Apache Maven 3.8+**
- **PostgreSQL** (only required for the SQL example)

## Build

```bash
cd java/
mvn clean package
```

## Running Examples

Each example has its own `main` method and can be run individually:

```bash
# General pattern:
java -cp target/classes com.coboldemo.<package>.<ClassName>

# Examples:
java -cp target/classes com.coboldemo.accept.AcceptExample
java -cp target/classes com.coboldemo.trim.TrimExample
java -cp target/classes com.coboldemo.mergesort.MergeSortExample
java -cp target/classes com.coboldemo.json.JsonGenerateExample

# Programs with command-line arguments:
java -cp target/classes com.coboldemo.commandargs.ReadCmdLineArgs --test --verbose
java -cp target/classes com.coboldemo.commandargs.ReadSpecificCmdLineArgs arg1 arg2 arg3
java -cp target/classes com.coboldemo.accept.AcceptFromExample hello world

# SQL example (requires PostgreSQL with test DB):
java -cp "target/classes:target/dependency/*" com.coboldemo.sql.SqlExample

# JSON/XML examples (require Jackson/JAXB on classpath):
java -cp "target/classes:target/dependency/*" com.coboldemo.json.JsonGenerateExample
java -cp "target/classes:target/dependency/*" com.coboldemo.xml.XmlGenerateExample
```

To copy dependencies for classpath-based execution:

```bash
mvn dependency:copy-dependencies -DoutputDirectory=target/dependency
```

## SQL Example Setup

The SQL example requires a PostgreSQL database. To set it up:

```bash
# Create the database and populate test data
psql -U postgres -f src/main/resources/create_test_db.sql
```

Default connection: `jdbc:postgresql://localhost:5432/cobol_db_example`.

Configure via environment variables:

```bash
export COBOL_DB_URL=jdbc:postgresql://localhost:5432/cobol_db_example
export COBOL_DB_USER=postgres
export COBOL_DB_PASSWORD=yourpassword
```

## COBOL-to-Java Mapping Table

| # | COBOL Source | Java Class | Package | Description |
|---|---|---|---|---|
| 1 | `accept/accept.cbl` | `AcceptExample` | `accept` | Basic ACCEPT verb: input, timeout, auto-skip, no-echo, uppercase |
| 2 | `accept/accept-secure.cbl` | `AcceptSecureExample` | `accept` | ACCEPT SECURE: masked password input |
| 3 | `accept/accept_from.cbl` | `AcceptFromExample` | `accept` | ACCEPT FROM: date, time, user, env vars, command-line args |
| 4 | `display_test/display-test.cbl` | `DisplayTest` | `display` | DISPLAY options: positioning, blank line, bell, colors |
| 5 | `display_timing/display_timing.cbl` | `DisplayTimingTest` | `display` | Screen-writing speed benchmark |
| 6 | `comp_test/comp_test.cbl` | `CompConversionTest` | `comp` | COMP to DISPLAY numeric conversion |
| 7 | `is_numeric/is_numeric.cbl` | `IsNumericTest` | `isnumeric` | IS NUMERIC validation with spaces handling |
| 8 | `numval_test/numval_test.cbl` | `NumvalTest` | `numval` | FUNCTION NUMVAL: string-to-number conversion |
| 9 | `trim/trim.cbl` | `TrimExample` | `trim` | FUNCTION TRIM: leading, trailing, both |
| 10 | `unstring/unstring.cbl` | `UnstringExample` | `unstring` | UNSTRING: delimiters, pointer, tallying, overflow |
| 11 | `search/search.cbl` | `SearchExample` | `search` | SEARCH / SEARCH ALL: sequential and binary table search |
| 12 | `read_command_args/read_cmd_line_args.cbl` | `ReadCmdLineArgs` | `commandargs` | Full command-line reading with INSPECT TALLYING |
| 13 | `read_command_args/read_specific_cmd_line_args.cbl` | `ReadSpecificCmdLineArgs` | `commandargs` | Individual argument iteration |
| 14 | `redifines/redefines.cbl` | `RedefinesExample` | `redefines` | REDEFINES: union-type memory overlay pattern |
| 15 | `merge_sort/merge_sort_test.cbl` | `MergeSortExample` | `mergesort` | SORT/MERGE on fixed-width record files |
| 16 | `report_writer/report_test.cbl` | `ReportWriterExample` | `reportwriter` | Report Writer: page headers, detail lines, page breaks |
| 17 | `sql/sql_example.cbl` | `SqlExample` | `sql` | Embedded SQL with PostgreSQL via JDBC |
| 18 | `json_generate/json_generate.cbl` | `JsonGenerateExample` | `json` | JSON GENERATE using Jackson |
| 19 | `xml_generate/xml_generate.cbl` | `XmlGenerateExample` | `xml` | XML GENERATE using JAXB |
| 20 | `sub_program/main_app.cbl` | `MainApp` | `subprogram` | Main program: CALL BY CONTENT / BY REFERENCE / CANCEL |
| 21 | `sub_program/sub.cbl` | `SubApp` | `subprogram` | Sub-program: WORKING-STORAGE vs LOCAL-STORAGE |
| 22 | `screen_size/get_screen_size.cbl` | `ScreenSizeExample` | `screensize` | Terminal dimensions via Lanterna |
| 23 | `mouse/mouse_example.cbl` | `MouseExample` | `mouse` | Terminal paint program with mouse events via Lanterna |

## Key COBOL-to-Java Concepts

| COBOL Concept | Java Equivalent |
|---|---|
| `ACCEPT` | `Scanner.nextLine()` / `System.console().readPassword()` |
| `DISPLAY` | `System.out.println()` / ANSI escape codes / Lanterna |
| `PIC 999 COMP` | `int` |
| `PIC X(n)` | `String` (padded to length n) |
| `COMP-2` | `double` |
| `IS NUMERIC` | `Integer.parseInt()` in try-catch or regex |
| `FUNCTION NUMVAL()` | `Double.parseDouble()` |
| `FUNCTION TRIM()` | `String.trim()` / `stripLeading()` / `stripTrailing()` |
| `UNSTRING ... DELIMITED BY` | `String.split()` / manual parsing |
| `SEARCH ALL` (binary) | `Arrays.binarySearch()` |
| `SEARCH` (sequential) | Linear loop |
| `REDEFINES` | Discriminated union (class with type field) |
| `SORT / MERGE` | `List.sort(Comparator)` |
| `REPORT SECTION` | Manual `PrintWriter` formatting |
| `EXEC SQL` | JDBC (`Connection`, `PreparedStatement`, `ResultSet`) |
| `JSON GENERATE` | Jackson `ObjectMapper` |
| `XML GENERATE` | JAXB `Marshaller` |
| `CALL ... BY CONTENT` | Pass copies (`new StringBuilder(original)`) |
| `CALL ... BY REFERENCE` | Pass shared mutable objects (`StringBuilder`) |
| `CANCEL` | Re-instantiate object (`new SubApp()`) |
| `WORKING-STORAGE` | Instance fields |
| `LOCAL-STORAGE` | Local variables |
| `CBL_GET_SCR_SIZE` | Lanterna `terminal.getTerminalSize()` |

## Dependencies

| Dependency | Purpose |
|---|---|
| `org.postgresql:postgresql` | JDBC driver for SQL example |
| `com.fasterxml.jackson.core:jackson-databind` | JSON generation |
| `javax.xml.bind:jaxb-api` + `org.glassfish.jaxb:jaxb-runtime` | XML generation |
| `com.googlecode.lanterna:lanterna` | Terminal UI for screen size and mouse examples |

## Project Structure

```
java/
├── pom.xml
├── README.md
├── src/main/java/com/coboldemo/
│   ├── accept/          — Console I/O (ACCEPT verb)
│   ├── commandargs/     — Command-line argument reading
│   ├── comp/            — COMP/DISPLAY numeric conversion
│   ├── display/         — DISPLAY statement options & timing
│   ├── isnumeric/       — IS NUMERIC validation
│   ├── json/            — JSON GENERATE
│   ├── mergesort/       — SORT/MERGE file operations
│   ├── mouse/           — Mouse event handling (Lanterna)
│   ├── numval/          — FUNCTION NUMVAL conversion
│   ├── redefines/       — REDEFINES union-type pattern
│   ├── reportwriter/    — Report Writer
│   ├── screensize/      — Terminal screen size (Lanterna)
│   ├── search/          — SEARCH / SEARCH ALL
│   ├── sql/             — Embedded SQL (JDBC)
│   ├── subprogram/      — Sub-program CALL conventions
│   ├── trim/            — FUNCTION TRIM
│   ├── unstring/        — UNSTRING parsing
│   └── xml/             — XML GENERATE
└── src/main/resources/
    ├── report_input.txt — Input data for report writer
    └── create_test_db.sql — PostgreSQL setup script
```
