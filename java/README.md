# COBOL Demo - Java Migration

Java migration of the [COBOL-Demo](https://github.com/ankehao-demo/COBOL-Demo) GnuCOBOL example programs. Each COBOL program has been translated to an equivalent Java class with a `main` method, preserving the original behavior as closely as possible.

## Prerequisites

- **Java 17+** (JDK)
- **Apache Maven 3.6+**
- **PostgreSQL** (only for the SQL example)

## Build

```bash
cd java
mvn clean package
```

## Run Individual Examples

Each migrated program can be run individually using the Maven exec plugin:

```bash
# General syntax
mvn exec:java -Dexec.mainClass="com.coboldemo.<package>.<ClassName>"

# Examples:
mvn exec:java -Dexec.mainClass="com.coboldemo.accept.AcceptExample"
mvn exec:java -Dexec.mainClass="com.coboldemo.display.DisplayTest"
mvn exec:java -Dexec.mainClass="com.coboldemo.trim.TrimTest"
mvn exec:java -Dexec.mainClass="com.coboldemo.json.JsonGenerateExample"

# Pass command-line arguments:
mvn exec:java -Dexec.mainClass="com.coboldemo.commandargs.ReadCmdLineArgs" -Dexec.args="--test foo bar"
```

## COBOL-to-Java Mapping Table

| COBOL Source File | Java Class | Package |
|---|---|---|
| `accept/accept.cbl` | `AcceptExample` | `com.coboldemo.accept` |
| `accept/accept-secure.cbl` | `AcceptSecure` | `com.coboldemo.accept` |
| `accept/accept_from.cbl` | `AcceptFrom` | `com.coboldemo.accept` |
| `display_test/display-test.cbl` | `DisplayTest` | `com.coboldemo.display` |
| `display_timing/display_timing.cbl` | `DisplayTiming` | `com.coboldemo.display` |
| `screen_size/get_screen_size.cbl` | `ScreenSizeTest` | `com.coboldemo.screensize` |
| `read_command_args/read_cmd_line_args.cbl` | `ReadCmdLineArgs` | `com.coboldemo.commandargs` |
| `read_command_args/read_specific_cmd_line_args.cbl` | `ReadSpecificCmdLineArgs` | `com.coboldemo.commandargs` |
| `comp_test/comp_test.cbl` | `CompConversionTest` | `com.coboldemo.comp` |
| `is_numeric/is_numeric.cbl` | `IsNumericTest` | `com.coboldemo.isnumeric` |
| `numval_test/numval_test.cbl` | `NumvalTest` | `com.coboldemo.numval` |
| `redifines/redefines.cbl` | `RedefinesTest` | `com.coboldemo.redefines` |
| `trim/trim.cbl` | `TrimTest` | `com.coboldemo.trim` |
| `unstring/unstring.cbl` | `UnstringExample` | `com.coboldemo.unstring` |
| `search/search.cbl` | `SearchExample` | `com.coboldemo.search` |
| `merge_sort/merge_sort_test.cbl` | `MergeSortExample` | `com.coboldemo.mergesort` |
| `report_writer/report_test.cbl` | `ReportTest` | `com.coboldemo.reportwriter` |
| `sql/sql_example.cbl` | `SqlExample` | `com.coboldemo.sql` |
| `json_generate/json_generate.cbl` | `JsonGenerateExample` | `com.coboldemo.json` |
| `xml_generate/xml_generate.cbl` | `XmlGenerateExample` | `com.coboldemo.xml` |
| `sub_program/main_app.cbl` + `sub.cbl` | `MainApp` + `SubApp` | `com.coboldemo.subprogram` |
| `mouse/mouse_example.cbl` | `MouseExample` | `com.coboldemo.mouse` |

## Dependencies

| Library | Version | Purpose |
|---|---|---|
| Jackson Databind | 2.17.0 | JSON serialization (`JsonGenerateExample`) |
| Jackson XML | 2.17.0 | XML serialization (`XmlGenerateExample`) |
| PostgreSQL JDBC | 42.7.3 | Database connectivity (`SqlExample`) |
| Lanterna | 3.1.1 | Terminal UI with mouse support (`MouseExample`) |

## Notes on Behavioral Differences

### Screen Mode Limitations
COBOL screen-mode features (`ACCEPT AT`, `DISPLAY AT LINE/COLUMN`, timeouts, auto-skip) have no direct Java console equivalent. These are approximated using:
- ANSI escape codes for cursor positioning
- `System.console().readPassword()` for no-echo input
- `String.toUpperCase()` for upper-case conversion
- Thread-based timeouts using `Future.get(timeout)`

### Mouse Support
The COBOL mouse example uses terminal escape sequences directly. The Java equivalent uses the **Lanterna** library for portable terminal UI with mouse event handling.

### SQL Example
The SQL example requires a running PostgreSQL instance. Set up the database using the provided script:
```bash
psql -U postgres -f sql/create_test_db.sql
```
The Java class connects to `jdbc:postgresql://localhost:5432/cobol_db_example` by default.

### Terminal Size Detection
Terminal size detection (`ScreenSizeTest`) uses `tput` commands via `ProcessBuilder`, which works on Linux/macOS terminals.

### Sub-program Simulation
COBOL's `CALL` mechanism with `BY REFERENCE` and `BY CONTENT` is simulated using Java method calls with mutable arrays (by-reference) and immutable parameters (by-content). The `CANCEL` verb is simulated with a `reset()` method.

## Files NOT Migrated

- `sql/generated_sql_ex.cbl` - Precompiler-generated artifact, not source code
- `.github/workflows/devin-on-label.yml` - CI workflow, kept as-is
