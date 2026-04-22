      ******************************************************************
      * test-utils.cpy
      *
      * Lightweight COBOL unit test helper paragraphs.
      *
      * Usage:
      *   working-storage section.
      *       copy "test-utils-data.cpy".
      *       ...your own working-storage items...
      *
      *   procedure division.
      *   main-procedure.
      *       move "it adds two numbers" to test-name
      *       move 4               to test-expected-num
      *       move 2               to test-actual-num
      *       add 2                to test-actual-num
      *       perform assert-num-equal
      *
      *       perform test-summary  *> never returns
      *       .
      *
      *   copy "test-utils.cpy".   *> at the end of the procedure div.
      *   end program ...
      *
      * Paragraphs:
      *   assert-num-equal  - passes when test-expected-num = test-actual-num
      *   assert-str-equal  - passes when test-expected-str = test-actual-str
      *   assert-true       - passes when test-actual-num is non-zero
      *   test-summary      - prints totals, stops with status 0 (all pass)
      *                       or 1 (any failure)
      ******************************************************************

       assert-num-equal.
           add 1 to test-cases-run
           if test-expected-num = test-actual-num
               add 1 to test-cases-passed
               display "  PASS: " function trim(test-name)
           else
               add 1 to test-cases-failed
               display "  FAIL: " function trim(test-name)
               move test-expected-num to test-display-num
               display "        expected: " test-display-num
               move test-actual-num   to test-display-num
               display "        actual:   " test-display-num
           end-if
           .

       assert-str-equal.
           add 1 to test-cases-run
           if test-expected-str = test-actual-str
               add 1 to test-cases-passed
               display "  PASS: " function trim(test-name)
           else
               add 1 to test-cases-failed
               display "  FAIL: " function trim(test-name)
               display "        expected: ["
                   function trim(test-expected-str) "]"
               display "        actual:   ["
                   function trim(test-actual-str) "]"
           end-if
           .

       assert-true.
           add 1 to test-cases-run
           if test-actual-num not = 0
               add 1 to test-cases-passed
               display "  PASS: " function trim(test-name)
           else
               add 1 to test-cases-failed
               display "  FAIL: " function trim(test-name)
               display "        expected truthy, got 0"
           end-if
           .

       test-summary.
           display " "
           display "------------------------------------------------"
           display "Test summary:"
           display "  tests run:    " test-cases-run
           display "  tests passed: " test-cases-passed
           display "  tests failed: " test-cases-failed
           display "------------------------------------------------"
           if test-cases-failed > 0
               stop run returning 1
           else
               stop run returning 0
           end-if
           .
