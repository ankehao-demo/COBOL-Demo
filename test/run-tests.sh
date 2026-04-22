#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# Test runner for the COBOL-Demo unit test suite.
#
# Compiles every test-*.cbl program in the test/ directory with cobc and
# executes each resulting binary. Aggregates pass/fail counts and exits
# non-zero if any test program returns a non-zero status.
#
# Usage:
#   bash test/run-tests.sh
# ---------------------------------------------------------------------------

set -u

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"
REPO_ROOT="$(cd -- "${SCRIPT_DIR}/.." >/dev/null 2>&1 && pwd)"
BUILD_DIR="${REPO_ROOT}/build/test"
COBC="${COBC:-cobc}"

if ! command -v "${COBC}" >/dev/null 2>&1; then
    echo "error: ${COBC} not found in PATH. Install GnuCOBOL first." >&2
    echo "       Ubuntu: sudo apt-get install -y gnucobol" >&2
    exit 127
fi

mkdir -p "${BUILD_DIR}"

shopt -s nullglob
TEST_SOURCES=("${SCRIPT_DIR}"/test-*.cbl)
shopt -u nullglob

if [[ ${#TEST_SOURCES[@]} -eq 0 ]]; then
    echo "error: no test-*.cbl files found in ${SCRIPT_DIR}" >&2
    exit 1
fi

echo "================================================================"
echo "Compiling ${#TEST_SOURCES[@]} test program(s)..."
echo "================================================================"

build_failures=0
for src in "${TEST_SOURCES[@]}"; do
    name="$(basename "${src}" .cbl)"
    bin="${BUILD_DIR}/${name}"
    echo "  cobc ${name}"

    extra_units=()
    if [[ "${name}" == "test-sub-program" ]]; then
        extra_units+=("${REPO_ROOT}/sub_program/sub.cbl")
    fi

    if ! "${COBC}" -x -Wall -I "${SCRIPT_DIR}" -o "${bin}" \
            "${src}" "${extra_units[@]}"; then
        echo "    BUILD FAILED: ${name}"
        build_failures=$((build_failures + 1))
    fi
done

if [[ ${build_failures} -gt 0 ]]; then
    echo ""
    echo "================================================================"
    echo "FAILED to compile ${build_failures} test program(s)."
    echo "================================================================"
    exit 1
fi

echo ""
echo "================================================================"
echo "Running ${#TEST_SOURCES[@]} test program(s)..."
echo "================================================================"

programs_run=0
programs_passed=0
programs_failed=0
failed_programs=()

for src in "${TEST_SOURCES[@]}"; do
    name="$(basename "${src}" .cbl)"
    bin="${BUILD_DIR}/${name}"
    programs_run=$((programs_run + 1))

    echo ""
    echo "---- ${name} ----"

    # Run each test program from its own scratch directory so file-based
    # tests don't pollute the repo root.
    run_dir="${BUILD_DIR}/${name}.run"
    rm -rf "${run_dir}"
    mkdir -p "${run_dir}"

    if (cd "${run_dir}" && "${bin}"); then
        programs_passed=$((programs_passed + 1))
    else
        exit_code=$?
        programs_failed=$((programs_failed + 1))
        failed_programs+=("${name} (exit=${exit_code})")
    fi
done

echo ""
echo "================================================================"
echo "Overall test suite summary"
echo "  programs run:    ${programs_run}"
echo "  programs passed: ${programs_passed}"
echo "  programs failed: ${programs_failed}"
echo "================================================================"

if [[ ${programs_failed} -gt 0 ]]; then
    echo "Failed programs:"
    for p in "${failed_programs[@]}"; do
        echo "  - ${p}"
    done
    exit 1
fi

exit 0
