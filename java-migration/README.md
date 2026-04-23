# COBOL Demo — Java Migration

A Java 17 port of the GnuCOBOL Example Suite that lives in the parent
directory of this project. Every COBOL source file in that suite (except the
precompiler-generated `sql/generated_sql_ex.cbl`) has a direct counterpart
here under `src/main/java/com/coboldemo/`.

## Build

```bash
mvn -q -f java-migration/pom.xml package
```

This produces `target/cobol-demo-java-1.0.0.jar` inside `java-migration/`.

## Running an example

Each demo is a standalone class with a `main` method. The easiest way to run
one is `mvn exec:java`:

```bash
mvn -q -f java-migration/pom.xml compile exec:java \
    -Dexec.mainClass=com.coboldemo.json.JsonGenerateExample
```

Or, once you have packaged the jar:

```bash
java -cp java-migration/target/cobol-demo-java-1.0.0.jar:$(mvn -q -f \
    java-migration/pom.xml dependency:build-classpath -Dmdep.outputFile=/dev/stdout) \
    com.coboldemo.json.JsonGenerateExample
```

Any program that shells out to the terminal (e.g. screen-mode demos) is best
run under a real TTY rather than inside an IDE console.

## COBOL → Java mapping

| COBOL source | Java class |
|---|---|
| `accept/accept.cbl` | `com.coboldemo.accept.AcceptExample` |
| `accept/accept-secure.cbl` | `com.coboldemo.accept.AcceptSecure` |
| `accept/accept_from.cbl` | `com.coboldemo.accept.AcceptFromExample` |
| `comp_test/comp_test.cbl` | `com.coboldemo.comp.CompConversionTest` |
| `display_test/display-test.cbl` | `com.coboldemo.display.DisplayTest` |
| `display_timing/display_timing.cbl` | `com.coboldemo.displaytiming.DisplayTiming` |
| `is_numeric/is_numeric.cbl` | `com.coboldemo.isnumeric.IsNumericTest` |
| `json_generate/json_generate.cbl` | `com.coboldemo.json.JsonGenerateExample` |
| `merge_sort/merge_sort_test.cbl` | `com.coboldemo.mergesort.MergeSortExample` |
| `mouse/mouse_example.cbl` | `com.coboldemo.mouse.MouseExample` (stub) |
| `numval_test/numval_test.cbl` | `com.coboldemo.numval.NumvalTest` |
| `read_command_args/read_cmd_line_args.cbl` | `com.coboldemo.commandargs.ReadCmdLineArgs` |
| `read_command_args/read_specific_cmd_line_args.cbl` | `com.coboldemo.commandargs.ReadSpecificCmdLineArgs` |
| `redifines/redefines.cbl` | `com.coboldemo.redefines.RedefinesTest` |
| `report_writer/report_test.cbl` | `com.coboldemo.reportwriter.ReportTest` |
| `screen_size/get_screen_size.cbl` | `com.coboldemo.screensize.ScreenSizeTest` |
| `search/search.cbl` | `com.coboldemo.search.SearchExample` |
| `sql/sql_example.cbl` | `com.coboldemo.sql.SqlExample` |
| `sub_program/main_app.cbl` | `com.coboldemo.subprogram.MainApp` |
| `sub_program/sub.cbl` | `com.coboldemo.subprogram.SubApp` |
| `trim/trim.cbl` | `com.coboldemo.trim.TrimFunctionTest` |
| `unstring/unstring.cbl` | `com.coboldemo.unstring.UnstringExample` |
| `xml_generate/xml_generate.cbl` | `com.coboldemo.xml.XmlGenerateExample` |

`sql/generated_sql_ex.cbl` is intentionally **not** ported — it is an artifact
emitted by the esqlOC precompiler from `sql_example.cbl`.

## Features without a clean Java equivalent

Some COBOL features rely on environment behaviour that isn't available on the
JVM. The ports document each case, but briefly:

- **Screen mode** (`AT 0101`, `LINE x COLUMN y`, colours, BELL, ERASE EOL,
  BLANK SCREEN). Implemented with ANSI escape sequences. Terminals without
  ANSI support will see the raw escape characters.
- **Mouse events** (`COB-AUTO-MOUSE-HANDLING`, etc.). The Java standard
  library has no terminal mouse support. `MouseExample` ships as a stub that
  explains the gap and suggests JLine3 / Lanterna / Swing as alternatives.
- **`SET ENVIRONMENT ... TO ...`**. Java cannot mutate its own process
  environment, so `AcceptFromExample` uses a system property as the closest
  analogue.
- **`ACCEPT ... NO-ECHO / SECURE`**. Uses `java.io.Console#readPassword` when
  available, falling back to a visible `Scanner` read in contexts where
  `System.console()` returns `null` (IDE, piped input, etc.).
- **`ACCEPT ... TIMEOUT n`**. Implemented with a single-threaded executor and
  `Future#get(timeout)`.
- **`REDEFINES`**. Java has no union types; the ports use polymorphism
  (for the customer case) and explicit sibling fields (for the display-vs-COMP
  case).
- **`JSON GENERATE` / `XML GENERATE`**. Replaced by Jackson and JAXB
  respectively. Feature modifiers map to annotations (see the per-file
  comments for specifics).
- **`SEARCH ALL` / `SEARCH`**. Replaced with a hand-rolled binary search and
  a linear scan so the behaviour is visible in the source.
- **Embedded SQL (esqlOC)**. Replaced with plain JDBC + PreparedStatements.
  Connection settings can be overridden with the
  `COBOL_DEMO_JDBC_URL`, `COBOL_DEMO_JDBC_USER`, and
  `COBOL_DEMO_JDBC_PASSWORD` environment variables.
