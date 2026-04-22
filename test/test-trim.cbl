      ******************************************************************
      * author: COBOL-Demo test framework
      * purpose: Unit tests for the intrinsic TRIM function usage
      *          demonstrated in trim/trim.cbl.
      * tectonics: cobc -x -I test test/test-trim.cbl
      ******************************************************************
       identification division.
       program-id. test-trim.

       data division.
       file section.

       working-storage section.
           copy "test-utils-data.cpy".

       01  ws-test-string             pic x(30)
           value "    hello world       ".
       01  ws-trimmed                 pic x(30).
       01  ws-trimmed-leading         pic x(30).
       01  ws-trimmed-trailing        pic x(30).

       procedure division.
       main-procedure.

      *>--------------------------------------------------------------
      *> TRIM (default) strips both leading and trailing spaces.
      *>--------------------------------------------------------------
           move spaces to ws-trimmed
           move function trim(ws-test-string) to ws-trimmed
           move "full trim removes leading and trailing spaces"
               to test-name
           move "hello world" to test-expected-str
           move ws-trimmed to test-actual-str
           perform assert-str-equal

      *>--------------------------------------------------------------
      *> TRIM LEADING strips leading spaces only.
      *>--------------------------------------------------------------
           move spaces to ws-trimmed-leading
           move function trim(ws-test-string leading)
               to ws-trimmed-leading
           move "leading trim removes only the leading spaces"
               to test-name
           move "hello world       " to test-expected-str
           move ws-trimmed-leading to test-actual-str
           perform assert-str-equal

      *>--------------------------------------------------------------
      *> TRIM TRAILING strips trailing spaces only.
      *>--------------------------------------------------------------
           move spaces to ws-trimmed-trailing
           move function trim(ws-test-string trailing)
               to ws-trimmed-trailing
           move "trailing trim removes only the trailing spaces"
               to test-name
           move "    hello world" to test-expected-str
           move ws-trimmed-trailing to test-actual-str
           perform assert-str-equal

      *>--------------------------------------------------------------
      *> TRIM of a literal with only surrounding spaces.
      *>--------------------------------------------------------------
           move "trim literal '   foo   ' = 'foo'" to test-name
           move "foo" to test-expected-str
           move function trim("   foo   ") to test-actual-str
           perform assert-str-equal

      *>--------------------------------------------------------------
      *> TRIM of an all-spaces value produces an empty result.
      *>--------------------------------------------------------------
           move "trim of all spaces is empty" to test-name
           move spaces to test-expected-str
           move function trim("          ") to test-actual-str
           perform assert-str-equal

           perform test-summary
           .

           copy "test-utils.cpy".

       end program test-trim.
