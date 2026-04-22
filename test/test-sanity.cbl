      ******************************************************************
      * author: COBOL-Demo test framework
      * purpose: Smoke test verifying the test-utils copybook compiles
      *          and that pass/fail paths behave correctly.
      * tectonics: cobc -x -I test test/test-sanity.cbl
      ******************************************************************
       identification division.
       program-id. test-sanity.

       data division.
       file section.

       working-storage section.
           copy "test-utils-data.cpy".

       procedure division.
       main-procedure.
           move "numeric equality holds" to test-name
           move 42 to test-expected-num
           move 42 to test-actual-num
           perform assert-num-equal

           move "alphanumeric equality holds" to test-name
           move "hello" to test-expected-str
           move "hello" to test-actual-str
           perform assert-str-equal

           move "non-zero value is truthy" to test-name
           move 7 to test-actual-num
           perform assert-true

           perform test-summary
           .

           copy "test-utils.cpy".

       end program test-sanity.
