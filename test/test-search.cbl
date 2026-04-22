      ******************************************************************
      * author: COBOL-Demo test framework
      * purpose: Unit tests for SEARCH ALL (binary) and SEARCH
      *          (sequential) patterns demonstrated in search/search.cbl.
      * tectonics: cobc -x -I test test/test-search.cbl
      ******************************************************************
       identification division.
       program-id. test-search.

       data division.
       file section.

       working-storage section.
           copy "test-utils-data.cpy".

       01  ws-item-table              occurs 3 times
                                       ascending key is ws-item-id
                                       indexed by idx.
           05  ws-item-id              pic 9(4).
           05  ws-item-name            pic x(16).

       01  ws-no-key-table            occurs 3 times
                                       indexed by idx-2.
           05  ws-no-key-id            pic 9(4).
           05  ws-no-key-value         pic x(25).

       01  ws-found-flag               pic 9 value 0.
       01  ws-found-name               pic x(16).

       procedure division.
       main-procedure.
           perform setup-test-data

      *>--------------------------------------------------------------
      *> SEARCH ALL (binary search) finds an entry in a sorted table.
      *>--------------------------------------------------------------
           move 0 to ws-found-flag
           move spaces to ws-found-name
           set idx to 1
           search all ws-item-table
               at end
                   move 0 to ws-found-flag
               when ws-item-id(idx) = 2
                   move 1 to ws-found-flag
                   move ws-item-name(idx) to ws-found-name
           end-search

           move "SEARCH ALL finds existing id 2" to test-name
           move 1 to test-expected-num
           move ws-found-flag to test-actual-num
           perform assert-num-equal

           move "SEARCH ALL returns the matched item name"
               to test-name
           move "test item 2     " to test-expected-str
           move ws-found-name to test-actual-str
           perform assert-str-equal

      *>--------------------------------------------------------------
      *> SEARCH ALL on a missing id triggers AT END.
      *>--------------------------------------------------------------
           move 0 to ws-found-flag
           set idx to 1
           search all ws-item-table
               at end
                   move 0 to ws-found-flag
               when ws-item-id(idx) = 99
                   move 1 to ws-found-flag
           end-search

           move "SEARCH ALL reports not found for missing id"
               to test-name
           move 0 to test-expected-num
           move ws-found-flag to test-actual-num
           perform assert-num-equal

      *>--------------------------------------------------------------
      *> Sequential SEARCH works on an unsorted table (no key).
      *>--------------------------------------------------------------
           move 0 to ws-found-flag
           move spaces to ws-found-name
           set idx-2 to 1
           search ws-no-key-table
               at end
                   move 0 to ws-found-flag
               when ws-no-key-id(idx-2) = 2
                   move 1 to ws-found-flag
                   move ws-no-key-value(idx-2) to ws-found-name
           end-search

           move "sequential SEARCH finds id 2 in unsorted table"
               to test-name
           move 1 to test-expected-num
           move ws-found-flag to test-actual-num
           perform assert-num-equal

           move "sequential SEARCH returns the matched value"
               to test-name
           move "Value of id 2.  " to test-expected-str
           move ws-found-name to test-actual-str
           perform assert-str-equal

      *>--------------------------------------------------------------
      *> Sequential SEARCH on a missing id triggers AT END.
      *>--------------------------------------------------------------
           move 0 to ws-found-flag
           set idx-2 to 1
           search ws-no-key-table
               at end
                   move 0 to ws-found-flag
               when ws-no-key-id(idx-2) = 42
                   move 1 to ws-found-flag
           end-search

           move "sequential SEARCH reports not found for missing id"
               to test-name
           move 0 to test-expected-num
           move ws-found-flag to test-actual-num
           perform assert-num-equal

           perform test-summary
           .

       setup-test-data.
      *> keyed table must be sorted ascending by ws-item-id
           move 1    to ws-item-id(1)
           move "test item 1"   to ws-item-name(1)
           move 2    to ws-item-id(2)
           move "test item 2"   to ws-item-name(2)
           move 3    to ws-item-id(3)
           move "test item 3"   to ws-item-name(3)

      *> no-key table can be in any order
           move 2 to ws-no-key-id(1)
           move "Value of id 2." to ws-no-key-value(1)
           move 3 to ws-no-key-id(2)
           move "Value of id 3." to ws-no-key-value(2)
           move 1 to ws-no-key-id(3)
           move "Value of id 1." to ws-no-key-value(3)
           .

           copy "test-utils.cpy".

       end program test-search.
