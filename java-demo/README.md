# java-demo

Java conversion of COBOL demo programs &mdash; **Phase 1: Console I/O & Data Types**.

This module is a side-by-side modern Java translation of a subset of
the GnuCOBOL examples that live at the root of this repository. The
original `.cbl` files are kept intact; each Java class in this module
documents the COBOL idiom it replaces and produces output that mirrors
the original program.

## Requirements

- JDK 17 or newer
- Maven 3.6+

## Build

```bash
cd java-demo
mvn compile
```

## Run an example

Each class has a `public static void main(String[] args)` entry point
so it can be launched standalone. The exec plugin makes that easy:

```bash
mvn exec:java -Dexec.mainClass="com.coboldemo.accept.AcceptExample"
mvn exec:java -Dexec.mainClass="com.coboldemo.display.DisplayTest"
mvn exec:java -Dexec.mainClass="com.coboldemo.display.DisplayTiming"
mvn exec:java -Dexec.mainClass="com.coboldemo.numeric.CompTest"
mvn exec:java -Dexec.mainClass="com.coboldemo.numeric.NumvalTest"
mvn exec:java -Dexec.mainClass="com.coboldemo.numeric.IsNumeric"
mvn exec:java -Dexec.mainClass="com.coboldemo.args.ReadCommandArgs" \
    -Dexec.args="alpha beta --test"
```

To pass program arguments, use `-Dexec.args="arg1 arg2"`.

## Phase 1 modules

| COBOL source | Java class | What it demonstrates |
| --- | --- | --- |
| `accept/accept_from.cbl` | `com.coboldemo.accept.AcceptExample` | All flavors of `ACCEPT ... FROM ...`: command-line args, environment variables, console input, system date / time / day / day-of-week, current user. |
| `display_test/display-test.cbl` | `com.coboldemo.display.DisplayTest` | `System.out.println` / `printf` formatting plus ANSI escape sequences for cursor positioning, color, bell, and clear-to-end-of-line (the closest portable Java analogue to GnuCOBOL screen-mode `DISPLAY ... AT row,col`). |
| `display_timing/display_timing.cbl` | `com.coboldemo.display.DisplayTiming` | Two timing workloads measured with `System.nanoTime()` for direct comparison &mdash; the same shape as the original COBOL benchmark. |
| `comp_test/comp_test.cbl` | `com.coboldemo.numeric.CompTest` | How COBOL `USAGE COMP / COMP-3 / COMP-5` and `PIC` clauses map to Java `short`, `int`, `long`, and `BigDecimal`. |
| `numval_test/numval_test.cbl` | `com.coboldemo.numeric.NumvalTest` | `FUNCTION NUMVAL` / `NUMVAL-C` modeled with `Double.parseDouble`, `BigDecimal`, and a small currency-stripping helper. |
| `is_numeric/is_numeric.cbl` | `com.coboldemo.numeric.IsNumeric` | `IF ws-variable IS NUMERIC` modeled with both `Long.parseLong` (try/catch) and a precompiled regex. |
| `read_command_args/read_cmd_line_args.cbl` and `read_specific_cmd_line_args.cbl` | `com.coboldemo.args.ReadCommandArgs` | `ACCEPT FROM COMMAND-LINE / ARGUMENT-NUMBER / ARGUMENT-VALUE` mapped to the standard `String[] args` parameter. |

## Notes on portability

A handful of original COBOL features rely on GnuCOBOL screen mode
(ncurses) and have no portable JDK counterpart &mdash; for example
`ACCEPT FROM LINES / COLUMNS`, `CALL "CBL_GET_SCR_SIZE"`, or
`DISPLAY ... WITH BLANK LINE / ERASE EOL / BELL`. Where this comes up,
the corresponding Java class either uses ANSI escape sequences when
stdout is a real TTY, or prints a short message explaining why the
feature was skipped. The behavior is otherwise identical to the
original program.
