# Java Migration of the GnuCOBOL Example Suite

This directory is a Java port of the 24 COBOL programs in the parent repository.
The original COBOL sources are preserved unchanged; this Maven project is added
alongside them and can be built and run independently.

## Prerequisites

- Java 17 or newer (the project is configured for `--release 17`)
- Apache Maven 3.6+
- (Optional) PostgreSQL with the `cobol_db_example` schema for `SqlExample`

## Build

```sh
cd java-migration
mvn clean package
```

This compiles every example into `target/classes` and produces a jar at
`target/java-migration-1.0.0.jar`.

## Running an example

Each Java class has its own `public static void main(String[] args)` entry
point and can be run directly. After `mvn package`, run any example with the
project classes plus the resolved dependency classpath:

```sh
mvn -q exec:java -Dexec.mainClass=com.coboldemo.accept.AcceptExample
```

Or with a plain `java -cp`:

```sh
mvn -q dependency:build-classpath -Dmdep.outputFile=cp.txt
java -cp "target/classes:$(cat cp.txt)" com.coboldemo.accept.AcceptExample
```

For examples that don't depend on Jackson / JDBC / Lanterna you can simply do:

```sh
java -cp target/classes com.coboldemo.trim.TrimFunctionTest
```

## Mapping: COBOL source -> Java class

| COBOL file                                              | Java class                                            |
| ------------------------------------------------------- | ----------------------------------------------------- |
| `accept/accept.cbl`                                     | `com.coboldemo.accept.AcceptExample`                  |
| `accept/accept_from.cbl`                                | `com.coboldemo.accept.AcceptFromExample`              |
| `accept/accept-secure.cbl`                              | `com.coboldemo.accept.AcceptSecureExample`            |
| `comp_test/comp_test.cbl`                               | `com.coboldemo.comp.CompConversionTest`               |
| `display_test/display-test.cbl`                         | `com.coboldemo.display.DisplayTest`                   |
| `display_timing/display_timing.cbl`                     | `com.coboldemo.display.DisplayTiming`                 |
| `is_numeric/is_numeric.cbl`                             | `com.coboldemo.isnumeric.IsNumericTest`               |
| `json_generate/json_generate.cbl`                       | `com.coboldemo.json.JsonGenerateExample`              |
| `merge_sort/merge_sort_test.cbl`                        | `com.coboldemo.mergesort.MergeSortExample`            |
| `mouse/mouse_example.cbl`                               | `com.coboldemo.mouse.MouseExample`                    |
| `numval_test/numval_test.cbl`                           | `com.coboldemo.numval.NumvalTest`                     |
| `read_command_args/read_cmd_line_args.cbl`              | `com.coboldemo.cmdargs.ReadCmdLineArgs`               |
| `read_command_args/read_specific_cmd_line_args.cbl`     | `com.coboldemo.cmdargs.ReadSpecificCmdLineArgs`       |
| `redifines/redefines.cbl`                               | `com.coboldemo.redefines.RedefinesExample`            |
| `report_writer/report_test.cbl`                         | `com.coboldemo.report.ReportWriterTest`               |
| `screen_size/get_screen_size.cbl`                       | `com.coboldemo.screensize.ScreenSizeTest`             |
| `search/search.cbl`                                     | `com.coboldemo.search.SearchExample`                  |
| `sql/sql_example.cbl`                                   | `com.coboldemo.sql.SqlExample`                        |
| `sub_program/main_app.cbl`                              | `com.coboldemo.subprogram.MainApp`                    |
| `sub_program/sub.cbl`                                   | `com.coboldemo.subprogram.SubApp`                     |
| `trim/trim.cbl`                                         | `com.coboldemo.trim.TrimFunctionTest`                 |
| `unstring/unstring.cbl`                                 | `com.coboldemo.unstring.UnstringExample`              |
| `xml_generate/xml_generate.cbl`                         | `com.coboldemo.xml.XmlGenerateExample`                |

## Behavioral differences vs the original COBOL

- **Screen mode (`AT yyxx`, `BLANK SCREEN`, `BACKGROUND-COLOR`, etc.)** is a
  GnuCOBOL/ncurses feature. Java's standard library has no direct equivalent,
  so positioning is emulated with ANSI escape codes (`\u001b[row;colH`) when
  appropriate, and the demos otherwise fall back to plain sequential
  `System.out` output. True full-screen TUI behavior requires a library like
  Lanterna or JLine.
- **`ACCEPT ... TIMEOUT`** is approximated with `Future.get(timeout)` running
  a `Scanner` on a background thread.
- **`ACCEPT ... NO-ECHO` / `SECURE`** uses `System.console().readPassword()`,
  which only works on a real terminal (not from inside an IDE).
- **`SET ENVIRONMENT "X" TO ...`** has no equivalent in pure Java; we use
  `System.setProperty` and document the difference.
- **`REDEFINES`** (overlaying memory with different field types) does not
  exist in Java. The two redefines demos are translated to (a) class
  hierarchy with `instanceof` for the person/corp shape, and (b) a tagged
  union holding either a String or a `double` for the second case.
- **`COMP` / `COMP-2`** are mapped to `int` and `double`. PIC clauses such as
  `999` are reproduced with `String.format("%03d", ...)`.
- **`MERGE` / `SORT`** of files is implemented with an in-memory `List` plus
  `Comparator` after reading both source files.
- **Embedded SQL (`EXEC SQL ... END-EXEC`)** is replaced with JDBC
  `PreparedStatement` / `ResultSet`. Cursors map to `ResultSet` iteration.
- **`ACCEPT ... FROM LINES/COLUMNS`** uses `tput lines` / `tput cols` via
  `Runtime.exec`, falling back to `LINES` / `COLUMNS` env vars.
- **Sub-program `CALL ... BY CONTENT` vs `BY REFERENCE`** is shown by passing
  `String` (immutable copy) versus a one-element `String[]` (mutable holder).
  `CANCEL` is modeled by instantiating a fresh `SubApp`.

## Notes per module

- `JsonGenerateExample` and `XmlGenerateExample` use Jackson and suppress
  empty fields with `@JsonInclude(NON_EMPTY)`.
- `MergeSortExample` writes test files into the current working directory:
  `test-file-1.txt`, `test-file-2.txt`, `merge-output.txt`, and
  `sorted-contract-id.txt`.
- `ReportWriterTest` reads `input.txt` from the current working directory
  (the original lives at `report_writer/input.txt` in the repo root) and
  writes `report.txt` next to it.
- `SqlExample` connection details default to the same `localhost:5432` /
  `cobol_db_example` / `postgres / password` values as the COBOL original
  and can be overridden with the system properties
  `db.url`, `db.user`, `db.password`.
- `MouseExample` prefers Lanterna when it is on the classpath; otherwise it
  falls back to a tiny Swing paint window so it is always runnable.
