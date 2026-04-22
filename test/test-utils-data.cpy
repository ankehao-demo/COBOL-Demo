      ******************************************************************
      * test-utils-data.cpy
      *
      * Working-storage fields for the lightweight COBOL unit test
      * helper. COPY this inside the WORKING-STORAGE SECTION of each
      * test program, then COPY "test-utils.cpy" at the end of the
      * PROCEDURE DIVISION to pull in the assertion / summary
      * paragraphs.
      ******************************************************************

       01  test-counters.
           05  test-cases-run         pic 9(5) value 0.
           05  test-cases-passed      pic 9(5) value 0.
           05  test-cases-failed      pic 9(5) value 0.

       01  test-assert-ctx.
           05  test-expected-num      pic s9(18) value 0.
           05  test-actual-num        pic s9(18) value 0.
           05  test-expected-str      pic x(80) value spaces.
           05  test-actual-str        pic x(80) value spaces.
           05  test-name              pic x(80) value spaces.

       01  test-display-num           pic -(17)9.
