# ---------------------------------------------------------------------------
# Build system for the COBOL-Demo repository.
#
# Targets:
#   make            - alias for `make all`
#   make all        - compile every example program into build/bin/
#   make test       - compile and run the unit test suite in test/
#   make clean      - remove the build/ directory
#
# Requires GnuCOBOL (cobc). On Ubuntu: `sudo apt-get install -y gnucobol`.
# ---------------------------------------------------------------------------

COBC        ?= cobc
COBCFLAGS   ?= -Wall

BUILD_DIR   := build
BIN_DIR     := $(BUILD_DIR)/bin
TEST_BIN_DIR:= $(BUILD_DIR)/test

# Example programs compiled as standalone executables. The merge-sort demo is
# noted separately because it requires user input (ACCEPT) to complete a run;
# it still compiles cleanly under `make all`.
EXAMPLE_SOURCES := \
    accept/accept.cbl \
    accept/accept_from.cbl \
    accept/accept-secure.cbl \
    comp_test/comp_test.cbl \
    display_test/display-test.cbl \
    display_timing/display_timing.cbl \
    is_numeric/is_numeric.cbl \
    json_generate/json_generate.cbl \
    merge_sort/merge_sort_test.cbl \
    numval_test/numval_test.cbl \
    read_command_args/read_cmd_line_args.cbl \
    read_command_args/read_specific_cmd_line_args.cbl \
    redifines/redefines.cbl \
    report_writer/report_test.cbl \
    screen_size/get_screen_size.cbl \
    search/search.cbl \
    trim/trim.cbl \
    unstring/unstring.cbl \
    xml_generate/xml_generate.cbl

# sub_program requires linking two compilation units together.
SUB_PROGRAM_BIN := $(BIN_DIR)/sub_program/main_app

# The mouse example depends on curses; compile it only if pdcurses / ncurses
# headers are available.
OPTIONAL_EXAMPLE_SOURCES := \
    mouse/mouse_example.cbl

# SQL examples use ESQL preprocessing; they are excluded from the default
# build and left for users who have esqlOC / PostgreSQL configured.

EXAMPLE_BINS := $(patsubst %.cbl,$(BIN_DIR)/%,$(EXAMPLE_SOURCES))

TEST_SOURCES := $(wildcard test/test-*.cbl)
TEST_BINS    := $(patsubst test/%.cbl,$(TEST_BIN_DIR)/%,$(TEST_SOURCES))

.PHONY: all test clean examples help

all: examples

examples: $(EXAMPLE_BINS) $(SUB_PROGRAM_BIN)

# Pattern rule for a single-source example program.
$(BIN_DIR)/%: %.cbl
	@mkdir -p $(dir $@)
	$(COBC) -x $(COBCFLAGS) -o $@ $<

# sub_program is two COBOL units linked together.
$(SUB_PROGRAM_BIN): sub_program/main_app.cbl sub_program/sub.cbl
	@mkdir -p $(dir $@)
	$(COBC) -x $(COBCFLAGS) -o $@ $^

# Pattern rule for compiling a unit test.
# test-sub-program.cbl additionally needs sub_program/sub.cbl linked in.
$(TEST_BIN_DIR)/test-sub-program: test/test-sub-program.cbl sub_program/sub.cbl test/test-utils.cpy test/test-utils-data.cpy
	@mkdir -p $(dir $@)
	$(COBC) -x $(COBCFLAGS) -I test -o $@ test/test-sub-program.cbl sub_program/sub.cbl

$(TEST_BIN_DIR)/%: test/%.cbl test/test-utils.cpy test/test-utils-data.cpy
	@mkdir -p $(dir $@)
	$(COBC) -x $(COBCFLAGS) -I test -o $@ $<

test: $(TEST_BINS)
	@bash test/run-tests.sh

clean:
	rm -rf $(BUILD_DIR)

help:
	@echo "COBOL-Demo Makefile targets:"
	@echo "  make / make all - compile all example programs"
	@echo "  make test       - compile and run the unit test suite"
	@echo "  make clean      - remove build artifacts"
