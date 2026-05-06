# COBOL Examples — Java Edition

A collection of small example programs that started life in
[GnuCOBOL](https://gnucobol.sourceforge.io/) and have been ported to Java to make
them runnable on a modern JDK without a COBOL toolchain. Each demo is a
self-contained Java class that mirrors the behaviour of the original `.cbl` source
as closely as the language permits, with notes in the Javadoc explaining how
specific COBOL constructs were translated.

## Requirements

* JDK 17 or later
* Maven 3.6+

## Build

```bash
mvn -B verify
```

This compiles the project, runs the JUnit tests, and shades a runnable jar to
`target/cobol-demo.jar`.

## Run a demo

The shaded jar exposes every demo through a top-level launcher; the first
argument selects which one to run, and the rest are forwarded to the demo's
`main(String[] args)`:

```bash
java -jar target/cobol-demo.jar <demo-name> [demo-args...]
```

Run with no arguments to see the full list, or run a single demo directly:

```bash
java -jar target/cobol-demo.jar json-generate
java -jar target/cobol-demo.jar read-cmd-args --test
```

You can also run a demo via Maven without packaging:

```bash
mvn -q exec:java -Dexec.args="trim"
```

## Demos

| Demo name | Original COBOL source | What it shows |
|---|---|---|
| `accept` | `accept/accept.cbl` | Variations of `ACCEPT` (basic, timeout, auto-skip, no-echo, upper) |
| `accept-secure` | `accept/accept-secure.cbl` | Hidden password input via `System.console().readPassword()` |
| `accept-from` | `accept/accept_from.cbl` | `ACCEPT FROM …` sources: command line, env vars, date, time, day-of-week, lines/columns |
| `comp` | `comp_test/comp_test.cbl` | `COMP` vs zero-padded `DISPLAY` formatting |
| `display` | `display_test/display-test.cbl` | Positioned `DISPLAY` with colors, blank-line, erase-EOL, bell (ANSI escapes) |
| `display-timing` | `display_timing/display_timing.cbl` | Timing benchmark for repeatedly redrawing a 20×80 cell grid |
| `is-numeric` | `is_numeric/is_numeric.cbl` | Three approaches to checking that input is numeric |
| `json-generate` | `json_generate/json_generate.cbl` | `JSON GENERATE` via Jackson `ObjectMapper` |
| `merge-sort` | `merge_sort/merge_sort_test.cbl` | File-based MERGE + SORT of fixed-width customer records |
| `mouse` | `mouse/mouse_example.cbl` | Tiny Swing paint program (replaces ncurses paint) |
| `numval` | `numval_test/numval_test.cbl` | `NUMVAL` → `Double.parseDouble` |
| `read-cmd-args` | `read_command_args/read_cmd_line_args.cbl` | Inspecting the full command line |
| `read-specific-cmd-args` | `read_command_args/read_specific_cmd_line_args.cbl` | Iterating individual args |
| `redefines` | `redifines/redefines.cbl` | `REDEFINES` modeled with two views over a `byte[]` and a class union |
| `report-writer` | `report_writer/report_test.cbl` | Manual paginated report writer (header, detail lines, page counter) |
| `screen-size` | `screen_size/get_screen_size.cbl` | Detect terminal size from env vars / `stty size` |
| `search` | `search/search.cbl` | `SEARCH ALL` (binary search) and `SEARCH` (linear scan) |
| `sql` | `sql/sql_example.cbl` | PostgreSQL access via JDBC (interactive menu over `accounts` table) |
| `sub-program` | `sub_program/main_app.cbl` + `sub.cbl` | `CALL … BY CONTENT` vs `BY REFERENCE`, `CANCEL`, `WORKING-STORAGE` vs `LOCAL-STORAGE` |
| `trim` | `trim/trim.cbl` | `FUNCTION TRIM` → `String.strip / stripLeading / stripTrailing` |
| `unstring` | `unstring/unstring.cbl` | `UNSTRING` with delimiters, pointer tracking, multiple destinations |
| `xml-generate` | `xml_generate/xml_generate.cbl` | `XML GENERATE` via Jackson `XmlMapper` |

## Project Layout

```
src/main/java/com/example/cobol/
├── Launcher.java               # entry point used by the shaded jar
├── AnsiTerm.java               # tiny helper for the screen-positioned DISPLAYs
├── accept/                     # ACCEPT* demos
├── compdemo/                   # COMP/DISPLAY conversion
├── displaytest/                # positioned DISPLAY
├── displaytiming/              # display-timing benchmark
├── isnumeric/                  # IS NUMERIC checks
├── jsongenerate/               # JSON GENERATE
├── mergesort/                  # MERGE + SORT
├── mouse/                      # mouse-paint demo (Swing)
├── numval/                     # NUMVAL
├── readcommandargs/            # command-line argument demos
├── redefines/                  # REDEFINES
├── reportwriter/               # report-writer demo
├── screensize/                 # terminal-size detection
├── search/                     # SEARCH ALL / SEARCH
├── sql/                        # JDBC + PostgreSQL
├── subprogram/                 # CALL/CANCEL + WS/LS demonstration
├── trim/                       # TRIM functions
├── unstring/                   # UNSTRING
└── xmlgenerate/                # XML GENERATE

src/main/resources/
├── reportwriter/input.txt      # sample input for the report-writer demo
└── sql/create_test_db.sql      # schema + sample data for the sql demo
```

## Running the SQL demo

The SQL demo expects a running PostgreSQL with the schema in
`src/main/resources/sql/create_test_db.sql` applied. Override the connection
settings with environment variables:

```bash
export PGURL=jdbc:postgresql://localhost:5432/cobol_db_example
export PGUSER=postgres
export PGPASSWORD=password
psql "$PGURL" -f src/main/resources/sql/create_test_db.sql
java -jar target/cobol-demo.jar sql
```

## License

The original COBOL examples were released under the Apache 2.0 license — the
Java port keeps the same license. See [LICENSE](./LICENSE).
