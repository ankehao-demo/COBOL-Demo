# COBOL-Demo Java Migration

Java 17+ migration of the [COBOL-Demo](https://github.com/ankehao-demo/COBOL-Demo) example programs, originally written in GnuCOBOL.

## Prerequisites

- Java 17 or higher
- Apache Maven 3.6+
- PostgreSQL (for the SQL example only)

## Build

```bash
cd java
mvn clean package
```

## Running Examples

Each migrated program has a `main` method and can be run individually:

```bash
# From the java/ directory
mvn exec:java -Dexec.mainClass="com.coboldemo.accept.AcceptExample"
mvn exec:java -Dexec.mainClass="com.coboldemo.accept.AcceptSecure"
mvn exec:java -Dexec.mainClass="com.coboldemo.accept.AcceptFrom"
mvn exec:java -Dexec.mainClass="com.coboldemo.display.DisplayTest"
mvn exec:java -Dexec.mainClass="com.coboldemo.display.DisplayTiming"
mvn exec:java -Dexec.mainClass="com.coboldemo.screensize.ScreenSizeTest"
mvn exec:java -Dexec.mainClass="com.coboldemo.commandargs.ReadCmdLineArgs" -Dexec.args="--test foo bar"
mvn exec:java -Dexec.mainClass="com.coboldemo.commandargs.ReadSpecificCmdLineArgs" -Dexec.args="arg1 arg2 arg3"
mvn exec:java -Dexec.mainClass="com.coboldemo.comp.CompConversionTest"
mvn exec:java -Dexec.mainClass="com.coboldemo.isnumeric.IsNumericTest"
mvn exec:java -Dexec.mainClass="com.coboldemo.numval.NumvalTest"
mvn exec:java -Dexec.mainClass="com.coboldemo.redefines.RedefinesTest"
mvn exec:java -Dexec.mainClass="com.coboldemo.trim.TrimTest"
mvn exec:java -Dexec.mainClass="com.coboldemo.unstring.UnstringExample"
mvn exec:java -Dexec.mainClass="com.coboldemo.search.SearchExample"
mvn exec:java -Dexec.mainClass="com.coboldemo.mergesort.MergeSortExample"
mvn exec:java -Dexec.mainClass="com.coboldemo.reportwriter.ReportTest"
mvn exec:java -Dexec.mainClass="com.coboldemo.sql.SqlExample"
mvn exec:java -Dexec.mainClass="com.coboldemo.json.JsonGenerateExample"
mvn exec:java -Dexec.mainClass="com.coboldemo.xml.XmlGenerateExample"
mvn exec:java -Dexec.mainClass="com.coboldemo.subprogram.MainApp"
mvn exec:java -Dexec.mainClass="com.coboldemo.mouse.MouseExample"
```

## SQL Example Setup

The SQL example requires a running PostgreSQL instance. Create the test database using:

```bash
psql -U postgres -f src/main/resources/create_test_db.sql
```

## COBOL to Java Mapping

| COBOL Source File | Java Class | Description |
|---|---|---|
| `accept/accept.cbl` | `com.coboldemo.accept.AcceptExample` | Basic ACCEPT verb forms (input, timeout, no-echo, upper) |
| `accept/accept-secure.cbl` | `com.coboldemo.accept.AcceptSecure` | Secure password input |
| `accept/accept_from.cbl` | `com.coboldemo.accept.AcceptFrom` | ACCEPT FROM (command-line, environment, date/time, etc.) |
| `display_test/display-test.cbl` | `com.coboldemo.display.DisplayTest` | Screen-mode display with positioning and colors |
| `display_timing/display_timing.cbl` | `com.coboldemo.display.DisplayTiming` | Display benchmark timing |
| `screen_size/get_screen_size.cbl` | `com.coboldemo.screensize.ScreenSizeTest` | Terminal screen size detection |
| `read_command_args/read_cmd_line_args.cbl` | `com.coboldemo.commandargs.ReadCmdLineArgs` | Full command line args with INSPECT TALLYING |
| `read_command_args/read_specific_cmd_line_args.cbl` | `com.coboldemo.commandargs.ReadSpecificCmdLineArgs` | Individual command line argument iteration |
| `comp_test/comp_test.cbl` | `com.coboldemo.comp.CompConversionTest` | COMP (binary) data type conversions |
| `is_numeric/is_numeric.cbl` | `com.coboldemo.isnumeric.IsNumericTest` | Three approaches to numeric validation |
| `numval_test/numval_test.cbl` | `com.coboldemo.numval.NumvalTest` | NUMVAL function (string-to-number) |
| `redifines/redefines.cbl` | `com.coboldemo.redefines.RedefinesTest` | REDEFINES for unions and type reinterpretation |
| `trim/trim.cbl` | `com.coboldemo.trim.TrimTest` | FUNCTION TRIM (both, leading, trailing) |
| `unstring/unstring.cbl` | `com.coboldemo.unstring.UnstringExample` | UNSTRING with delimiters, pointers, and tallying |
| `search/search.cbl` | `com.coboldemo.search.SearchExample` | SEARCH ALL (binary) and SEARCH (sequential) |
| `merge_sort/merge_sort_test.cbl` | `com.coboldemo.mergesort.MergeSortExample` | File-based merge and sort operations |
| `report_writer/report_test.cbl` | `com.coboldemo.reportwriter.ReportTest` | Report writer with page formatting |
| `sql/sql_example.cbl` | `com.coboldemo.sql.SqlExample` | JDBC database operations with menu system |
| `json_generate/json_generate.cbl` | `com.coboldemo.json.JsonGenerateExample` | JSON generation using Jackson |
| `xml_generate/xml_generate.cbl` | `com.coboldemo.xml.XmlGenerateExample` | XML generation using Jackson XML |
| `sub_program/main_app.cbl` | `com.coboldemo.subprogram.MainApp` | Main program calling sub-program |
| `sub_program/sub.cbl` | `com.coboldemo.subprogram.SubApp` | Sub-program with working/local storage |
| `mouse/mouse_example.cbl` | `com.coboldemo.mouse.MouseExample` | Terminal paint program with mouse support |

## Behavioral Differences

- **Screen-mode positioning**: COBOL uses native screen positioning (`AT RRCC`). Java approximates this with ANSI escape codes (`\033[row;colH`). Terminal support may vary.
- **Timeout input**: COBOL's `ACCEPT WITH TIMEOUT` is approximated using `ExecutorService` with `Future.get(timeout)`.
- **Auto-skip**: COBOL automatically submits input after N characters in screen mode. Java requires the user to press Enter (raw terminal mode would be needed for true auto-skip).
- **REDEFINES**: COBOL shares memory at the byte level. Java uses separate typed fields to demonstrate the concept.
- **Mouse input**: COBOL uses GnuCOBOL's mouse extensions. Java uses the Lanterna terminal UI library for mouse and color support.
- **Terminal size**: COBOL has direct `ACCEPT FROM LINES/COLUMNS`. Java uses `tput` or `stty` via `ProcessBuilder` (Linux-specific).
- **Environment variables**: COBOL can set and get environment variables at runtime. Java can only read them (`System.getenv()`), not set them.
- **Report Writer**: COBOL's REPORT SECTION with INITIATE/GENERATE/TERMINATE is approximated with manual formatting using `PrintWriter`.

## Files NOT Migrated

- `sql/generated_sql_ex.cbl` — Precompiler-generated artifact, not source code
- `.github/workflows/devin-on-label.yml` — CI workflow, kept as-is
