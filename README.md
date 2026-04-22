# COBOL Examples
This is a collection of example and test COBOL programs I've written. I'm currently in the process of updating
each folder with a README.md file and more comments so that the examples are easier to follow along with.


All program were written using [GnuCOBOL](https://gnucobol.sourceforge.io/) in Linux.

## Building

Compile every example program with:

```sh
make all
```

Binaries are written under `build/bin/<module>/<program>`.

## Testing

A lightweight unit test framework lives under `test/`. Each test program is a
regular COBOL program that COPYs the shared helper copybooks
`test/test-utils-data.cpy` (working-storage counters) and `test/test-utils.cpy`
(assertion + summary paragraphs). Assertions bump `PASS` / `FAIL` counters and
the final `test-summary` paragraph exits with status `0` on all-pass or `1`
on any failure.

### Prerequisites

- [GnuCOBOL](https://gnucobol.sourceforge.io/) 3.x (`cobc` on your `PATH`).
  On Ubuntu: `sudo apt-get install -y gnucobol`.
- `make` and `bash`.

### Running the suite

```sh
make test
# or, directly:
bash test/run-tests.sh
```

The runner compiles every `test/test-*.cbl` file into `build/test/`, executes
each resulting binary in its own scratch directory, prints per-assertion
`PASS` / `FAIL` lines, and then reports an overall summary. The script exits
with a non-zero status if any test program fails.

### Coverage

| Test program | What it exercises |
|---|---|
| `test/test-sanity.cbl`          | Smoke test for the framework itself (equality / truthy assertions, summary paragraph). |
| `test/test-comp-conversion.cbl` | COMP (binary) ↔ DISPLAY conversion logic used in `comp_test/`. |
| `test/test-merge-sort.cbl`      | `MERGE` ascending and `SORT` descending, as used in `merge_sort/`. |
| `test/test-numval.cbl`          | `NUMVAL` intrinsic conversions used in `numval_test/`. |
| `test/test-redefines.cbl`       | `REDEFINES` storage overlay semantics used in `redifines/`. |
| `test/test-search.cbl`          | `SEARCH ALL` (binary) and sequential `SEARCH` used in `search/`. |
| `test/test-sub-program.cbl`     | `CALL ... BY CONTENT` / `BY REFERENCE` / `CANCEL` against the real `sub-app` unit in `sub_program/`. |
| `test/test-trim.cbl`            | Intrinsic `TRIM` / `TRIM LEADING` / `TRIM TRAILING` used in `trim/`. |
| `test/test-unstring.cbl`        | `UNSTRING` with multiple delimiters, `TALLYING IN`, and `COUNT IN` metadata used in `unstring/`. |

### Writing a new test

```cobol
       identification division.
       program-id. test-my-feature.
       data division.
       working-storage section.
           copy "test-utils-data.cpy".

       procedure division.
       main-procedure.
           move "2 + 2 = 4" to test-name
           move 4 to test-expected-num
           move 2 to test-actual-num
           add 2 to test-actual-num
           perform assert-num-equal

           perform test-summary
           .

           copy "test-utils.cpy".
       end program test-my-feature.
```

Drop the file into `test/test-*.cbl` and `make test` / `run-tests.sh` will pick
it up automatically.

### Continuous integration

Every push to `main` and every pull request against `main` runs the full test
suite on GitHub Actions (Ubuntu runner, `gnucobol` apt package). See
[`.github/workflows/ci.yml`](.github/workflows/ci.yml).    



