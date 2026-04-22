      ******************************************************************
      * author: COBOL-Demo test framework
      * purpose: Unit tests for the NUMVAL intrinsic function usage
      *          demonstrated in numval_test/numval_test.cbl.
      * tectonics: cobc -x -I test test/test-numval.cbl
      ******************************************************************
       identification division.
       program-id. test-numval.

       data division.
       file section.

       working-storage section.
           copy "test-utils-data.cpy".

       01  ws-x-val                   pic x(10).
       01  ws-9-val                   pic 9(10).
       01  ws-total                   comp-2.
       01  ws-total-scaled            pic s9(10)v9(4).

       procedure division.
       main-procedure.

      *>--------------------------------------------------------------
      *> NUMVAL converts a plain PIC X string of digits to its
      *> numeric value.
      *>--------------------------------------------------------------
           move "NUMVAL('123') = 123" to test-name
           move 123 to test-expected-num
           compute test-actual-num = function numval("123       ")
           perform assert-num-equal

      *>--------------------------------------------------------------
      *> Leading spaces inside a PIC X field are tolerated by NUMVAL.
      *>--------------------------------------------------------------
           move "NUMVAL('  42     ') = 42" to test-name
           move "  42     " to ws-x-val
           move 42 to test-expected-num
           compute test-actual-num = function numval(ws-x-val)
           perform assert-num-equal

      *>--------------------------------------------------------------
      *> NUMVAL result can be added to a PIC 9 field, matching the
      *> "first + second" pattern used by the demo.
      *>--------------------------------------------------------------
           move "NUMVAL('100') + 50 = 150" to test-name
           move "100       " to ws-x-val
           move 50 to ws-9-val
           compute ws-total = function numval(ws-x-val) + ws-9-val
           move ws-total to ws-total-scaled
           move 150 to test-expected-num
           move ws-total-scaled to test-actual-num
           perform assert-num-equal

      *>--------------------------------------------------------------
      *> NUMVAL of "0" is numeric zero.
      *>--------------------------------------------------------------
           move "NUMVAL('0') = 0" to test-name
           move 0 to test-expected-num
           compute test-actual-num = function numval("0         ")
           perform assert-num-equal

      *>--------------------------------------------------------------
      *> NUMVAL of a signed string returns a negative value.
      *>--------------------------------------------------------------
           move "NUMVAL('-7') = -7" to test-name
           move -7 to test-expected-num
           compute test-actual-num = function numval("-7        ")
           perform assert-num-equal

           perform test-summary
           .

           copy "test-utils.cpy".

       end program test-numval.
