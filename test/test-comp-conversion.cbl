      ******************************************************************
      * author: COBOL-Demo test framework
      * purpose: Unit tests for the COMP (binary) to DISPLAY conversion
      *          logic demonstrated in comp_test/comp_test.cbl.
      * tectonics: cobc -x -I test test/test-comp-conversion.cbl
      ******************************************************************
       identification division.
       program-id. test-comp-conversion.

       data division.
       file section.

       working-storage section.
           copy "test-utils-data.cpy".

       01  ws-comp-val                pic 999 comp.
       01  ws-disp-val                pic 999.
       01  ws-dyn-disp-val            pic zz9.
       01  ws-display-buffer          pic x(3).

       procedure division.
       main-procedure.

      *>--------------------------------------------------------------
      *> Multiplying a COMP value should double it, just like the demo.
      *>--------------------------------------------------------------
           move "12 * 2 in COMP field equals 24" to test-name
           move 12 to ws-comp-val
           multiply ws-comp-val by 2 giving ws-comp-val
           move 24 to test-expected-num
           move ws-comp-val to test-actual-num
           perform assert-num-equal

      *>--------------------------------------------------------------
      *> Moving a COMP field to a PIC 999 DISPLAY field preserves
      *> the numeric value.
      *>--------------------------------------------------------------
           move "COMP to PIC 999 DISPLAY keeps numeric value"
               to test-name
           move ws-comp-val to ws-disp-val
           move 24 to test-expected-num
           move ws-disp-val to test-actual-num
           perform assert-num-equal

      *>--------------------------------------------------------------
      *> Moving a COMP field to a zz9 field renders with a leading
      *> space in place of the leading zero.
      *>--------------------------------------------------------------
           move "COMP to PIC zz9 renders ' 24' (leading space)"
               to test-name
           move ws-comp-val to ws-dyn-disp-val
           move ws-dyn-disp-val to ws-display-buffer
           move " 24" to test-expected-str
           move ws-display-buffer to test-actual-str
           perform assert-str-equal

      *>--------------------------------------------------------------
      *> Direct numeric assignments into a COMP field round-trip
      *> through a DISPLAY field unchanged.
      *>--------------------------------------------------------------
           move "assign 7 to COMP and read back as DISPLAY"
               to test-name
           move 7 to ws-comp-val
           move ws-comp-val to ws-disp-val
           move 7 to test-expected-num
           move ws-disp-val to test-actual-num
           perform assert-num-equal

      *>--------------------------------------------------------------
      *> Maximum 3-digit value stored in COMP is preserved.
      *>--------------------------------------------------------------
           move "999 stored in PIC 999 COMP is preserved"
               to test-name
           move 999 to ws-comp-val
           move 999 to test-expected-num
           move ws-comp-val to test-actual-num
           perform assert-num-equal

           perform test-summary
           .

           copy "test-utils.cpy".

       end program test-comp-conversion.
