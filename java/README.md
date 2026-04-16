# COBOL Demo - Java Migration

Java migration of the GnuCOBOL demo programs. Each COBOL program has been converted to an equivalent Java class with a `main` method, preserving original behavior as closely as possible.

## Requirements

- Java 11 or higher
- Maven 3.6+
- PostgreSQL (only for the SQL example)

## Build

```bash
cd java
mvn clean package
```

## Run Examples

Each example can be run individually using the Maven exec plugin:

```bash
mvn exec:java -Dexec.mainClass="com.coboldemo.accept.AcceptExample"
```

Or after packaging, run directly with Java:

```bash
java -cp target/cobol-demo-java-1.0-SNAPSHOT.jar com.coboldemo.accept.AcceptExample
```

## Program Mapping

| # | COBOL Source | Java Class | Description |
|---|-------------|------------|-------------|
| 1 | `accept/accept.cbl` | `com.coboldemo.accept.AcceptExample` | Various ACCEPT verb forms (timeout, no-echo, upper) |
| 2 | `accept/accept-secure.cbl` | `com.coboldemo.accept.AcceptSecure` | Secure (password) input |
| 3 | `accept/accept_from.cbl` | `com.coboldemo.accept.AcceptFrom` | ACCEPT FROM (command-line, env, date, time, screen) |
| 4 | `display_test/display-test.cbl` | `com.coboldemo.display.DisplayTest` | Display options (positioning, colors, bell) |
| 5 | `display_timing/display_timing.cbl` | `com.coboldemo.display.DisplayTiming` | Screen write speed benchmark |
| 6 | `screen_size/get_screen_size.cbl` | `com.coboldemo.screensize.ScreenSizeTest` | Terminal screen dimensions |
| 7 | `read_command_args/read_cmd_line_args.cbl` | `com.coboldemo.commandargs.ReadCmdLineArgs` | Read full command line, count occurrences |
| 8 | `read_command_args/read_specific_cmd_line_args.cbl` | `com.coboldemo.commandargs.ReadSpecificCmdLineArgs` | Iterate individual arguments |
| 9 | `comp_test/comp_test.cbl` | `com.coboldemo.comp.CompConversionTest` | COMP (binary) data type arithmetic |
| 10 | `is_numeric/is_numeric.cbl` | `com.coboldemo.isnumeric.IsNumericTest` | Numeric validation (3 approaches) |
| 11 | `numval_test/numval_test.cbl` | `com.coboldemo.numval.NumvalTest` | NUMVAL alphanumeric-to-numeric conversion |
| 12 | `redifines/redefines.cbl` | `com.coboldemo.redefines.RedefinesTest` | REDEFINES for union-like data structures |
| 13 | `trim/trim.cbl` | `com.coboldemo.trim.TrimTest` | TRIM with LEADING and TRAILING variants |
| 14 | `unstring/unstring.cbl` | `com.coboldemo.unstring.UnstringExample` | UNSTRING with 6 examples (delimiters, pointers, stats) |
| 15 | `search/search.cbl` | `com.coboldemo.search.SearchExample` | SEARCH (sequential) and SEARCH ALL (binary) |
| 16 | `merge_sort/merge_sort_test.cbl` | `com.coboldemo.mergesort.MergeSortExample` | MERGE and SORT on file records |
| 17 | `report_writer/report_test.cbl` | `com.coboldemo.reportwriter.ReportTest` | REPORT WRITER with page formatting |
| 18 | `sql/sql_example.cbl` | `com.coboldemo.sql.SqlExample` | Database connectivity (JDBC/PostgreSQL) |
| 19 | `json_generate/json_generate.cbl` | `com.coboldemo.json.JsonGenerateExample` | JSON GENERATE using Jackson |
| 20 | `xml_generate/xml_generate.cbl` | `com.coboldemo.xml.XmlGenerateExample` | XML GENERATE using Jackson XML |
| 21 | `sub_program/main_app.cbl` | `com.coboldemo.subprogram.MainApp` | Main program (CALL BY CONTENT/REFERENCE) |
| 22 | `sub_program/sub.cbl` | `com.coboldemo.subprogram.SubApp` | Subprogram (working-storage persistence) |
| 23 | `mouse/mouse_example.cbl` | `com.coboldemo.mouse.MouseExample` | Terminal paint program with mouse support |

### Files NOT migrated

| File | Reason |
|------|--------|
| `sql/generated_sql_ex.cbl` | Precompiler-generated artifact of `sql_example.cbl` |
| `.github/workflows/devin-on-label.yml` | CI workflow, not COBOL source |

## Behavioral Differences

### Screen Mode
COBOL's screen mode (coordinate-based terminal I/O via ncurses) has no direct Java equivalent. The Java versions use:
- **ANSI escape codes** (`\033[row;colH`) for positioning in `DisplayTest` and `DisplayTiming`
- **Lanterna library** for full terminal UI with mouse support in `MouseExample`
- **`tput` commands** via `ProcessBuilder` for screen dimensions in `ScreenSizeTest`

### ACCEPT Features
- **Timeout**: Approximated with `Future.get(timeout)` using `ExecutorService`
- **No-echo**: Uses `System.console().readPassword()` when available
- **Auto-skip**: Truncates input to N characters (true auto-skip requires raw terminal mode)
- **Upper**: Calls `.toUpperCase()` on input

### REDEFINES
Java does not have memory-level REDEFINES. The Java version uses class hierarchies with a type discriminator field to represent union-like behavior.

### SQL
- COBOL embedded SQL (ESQL/ODBC) is replaced with JDBC
- Requires PostgreSQL running on `localhost:5432`
- Run `sql/create_test_db.sql` to set up the database
- Connection can be customized with JVM properties: `-Ddb.url=...`, `-Ddb.user=...`, `-Ddb.password=...`

### Mouse Support
The COBOL version uses GnuCOBOL's built-in mouse event handling. The Java version uses the [Lanterna](https://github.com/mabe02/lanterna) library (v3.1.1) for terminal UI with mouse support.

## Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| `jackson-databind` | 2.15.3 | JSON serialization |
| `jackson-dataformat-xml` | 2.15.3 | XML serialization |
| `postgresql` | 42.6.0 | JDBC driver for SQL example |
| `lanterna` | 3.1.1 | Terminal UI with mouse support |
