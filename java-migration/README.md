# COBOL-Demo — Java Migration

An idiomatic Java 17+ port of the 24 GnuCOBOL example programs at the root
of this repository. Each Java class is a standalone, runnable program that
mirrors the behaviour of a corresponding `.cbl` source file.

## Requirements

- Java 17 or newer (the project is built with `--release 17`).
- Maven 3.6+.
- A PostgreSQL server for `SqlExample` only — every other program runs
  without external services.

## Build

```bash
cd java-migration
mvn -q clean compile
```

## Run a program

Any class can be launched with `mvn exec:java` (requires the
`exec-maven-plugin`) or, more simply, via `java` after compilation:

```bash
mvn -q -DskipTests package
java -cp target/classes:$(mvn -q dependency:build-classpath -DincludeScope=runtime -Dmdep.outputFile=/dev/stdout) \
     com.coboldemo.trim.TrimFunctionTest
```

For the small utilities that have no extra dependencies you can just run
them straight from the compiled classes:

```bash
java -cp target/classes com.coboldemo.cmdargs.ReadCmdLineArgs --test
```

## Program mapping

| COBOL source | Java class |
| --- | --- |
| `accept/accept.cbl` | `com.coboldemo.accept.AcceptExample` |
| `accept/accept-secure.cbl` | `com.coboldemo.accept.AcceptSecure` |
| `accept/accept_from.cbl` | `com.coboldemo.accept.AcceptFromExample` |
| `comp_test/comp_test.cbl` | `com.coboldemo.comp.CompConversionTest` |
| `display_test/display-test.cbl` | `com.coboldemo.display.DisplayTest` |
| `display_timing/display_timing.cbl` | `com.coboldemo.display.DisplayTiming` |
| `is_numeric/is_numeric.cbl` | `com.coboldemo.isnumeric.IsNumericTest` |
| `json_generate/json_generate.cbl` | `com.coboldemo.json.JsonGenerateExample` |
| `merge_sort/merge_sort_test.cbl` | `com.coboldemo.mergesort.MergeSortExample` |
| `mouse/mouse_example.cbl` | `com.coboldemo.mouse.MouseExample` |
| `numval_test/numval_test.cbl` | `com.coboldemo.numval.NumvalTest` |
| `read_command_args/read_cmd_line_args.cbl` | `com.coboldemo.cmdargs.ReadCmdLineArgs` |
| `read_command_args/read_specific_cmd_line_args.cbl` | `com.coboldemo.cmdargs.ReadSpecificCmdLineArgs` |
| `redifines/redefines.cbl` | `com.coboldemo.redefines.RedefinesTest` |
| `report_writer/report_test.cbl` | `com.coboldemo.report.ReportTest` |
| `screen_size/get_screen_size.cbl` | `com.coboldemo.screensize.ScreenSizeTest` |
| `search/search.cbl` | `com.coboldemo.search.SearchExample` |
| `sql/sql_example.cbl` | `com.coboldemo.sql.SqlExample` |
| `sub_program/main_app.cbl` | `com.coboldemo.subprogram.MainApp` |
| `sub_program/sub.cbl` | `com.coboldemo.subprogram.SubApp` |
| `trim/trim.cbl` | `com.coboldemo.trim.TrimFunctionTest` |
| `unstring/unstring.cbl` | `com.coboldemo.unstring.UnstringExample` |
| `xml_generate/xml_generate.cbl` | `com.coboldemo.xml.XmlGenerateExample` |

`sql/generated_sql_ex.cbl` is a precompiler output of `sql_example.cbl`
and is intentionally not ported separately.

## Notes about the translation

Several COBOL features rely on terminal screen mode or ncurses and have no
portable Java equivalent. Where relevant, the corresponding Java class
documents the behaviour and falls back to stream-based I/O:

- **Screen positioning (`AT yyxx`, `LINE`, `COLUMN`, `BLANK SCREEN`,
  `ERASE EOL`, `BELL`, foreground/background color).** These behave like
  escape codes driven by GnuCOBOL's screen runtime; the Java ports emit
  plain text (with ANSI colour codes in `DisplayTest`).
- **Mouse events (`mouse_example.cbl`).** Java's standard console cannot
  deliver mouse events. `MouseExample` prints a description rather than
  pulling in a terminal-UI library; swap in Lanterna if you want the
  full behaviour.
- **`CBL_GET_SCR_SIZE`, `ACCEPT FROM LINES/COLUMNS`.** The Java ports
  read the `COLUMNS` / `LINES` environment variables and optionally shell
  out to `tput` on Linux.
- **`REDEFINES` with mismatched types.** COBOL lets the same bytes be
  interpreted as a string or a `COMP-2` double. The Java port uses
  `ByteBuffer` for the byte-level reinterpretation and a small class
  hierarchy for the person/corp customer example.
- **Sub-program `CALL ... BY REFERENCE` and `CANCEL`.** `BY REFERENCE` is
  emulated with mutable holder objects; `CANCEL` is emulated by
  instantiating a new `SubApp` (which resets its "working storage"
  instance fields).

## Dependencies

See `pom.xml`:

- `com.fasterxml.jackson.core:jackson-databind` — JSON serialization for
  `JsonGenerateExample`.
- `jakarta.xml.bind:jakarta.xml.bind-api` plus `org.glassfish.jaxb:jaxb-runtime`
  — XML serialization for `XmlGenerateExample`.
- `org.postgresql:postgresql` — JDBC driver for `SqlExample`.
