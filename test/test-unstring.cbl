      ******************************************************************
      * author: COBOL-Demo test framework
      * purpose: Unit tests for the UNSTRING patterns demonstrated in
      *          unstring/unstring.cbl.
      * tectonics: cobc -x -I test test/test-unstring.cbl
      ******************************************************************
       identification division.
       program-id. test-unstring.

       data division.
       file section.

       working-storage section.
           copy "test-utils-data.cpy".

       01  ws-source-str              pic x(30).
       01  ws-part-1                  pic x(15).
       01  ws-part-2                  pic x(15).
       01  ws-pointer                 pic 9(5) comp.
       01  ws-fields-filled           pic 99.

       01  ws-multi-table.
           05  ws-multi-entry         occurs 6 times.
               10  ws-multi-str       pic x(5).
               10  ws-multi-delim     pic x.
               10  ws-multi-count     pic 9.

       procedure division.
       main-procedure.

      *>--------------------------------------------------------------
      *> Simple UNSTRING splits on a single delimiter into named
      *> destinations.
      *>--------------------------------------------------------------
           move "unstring 'Hello World' DELIMITED BY SPACE"
               to test-name
           move "Hello World                   " to ws-source-str
           move spaces to ws-part-1
           move spaces to ws-part-2
           unstring ws-source-str
               delimited by space
               into ws-part-1 ws-part-2
           end-unstring

           move "Hello" to test-expected-str
           move ws-part-1 to test-actual-str
           perform assert-str-equal

           move "second destination captures 'World'" to test-name
           move "World" to test-expected-str
           move ws-part-2 to test-actual-str
           perform assert-str-equal

      *>--------------------------------------------------------------
      *> UNSTRING with multiple delimiters populates tallying and
      *> per-field delimiter / count metadata.
      *>--------------------------------------------------------------
           move "A<B<CD>EFG!HIJ|KLMN>O         " to ws-source-str
           move 0 to ws-fields-filled
           move 1 to ws-pointer

           unstring ws-source-str
               delimited by all "<" or all ">" or "!" or "|"
               into
                   ws-multi-str(1)
                       delimiter in ws-multi-delim(1)
                       count in ws-multi-count(1)
                   ws-multi-str(2)
                       delimiter in ws-multi-delim(2)
                       count in ws-multi-count(2)
                   ws-multi-str(3)
                       delimiter in ws-multi-delim(3)
                       count in ws-multi-count(3)
                   ws-multi-str(4)
                       delimiter in ws-multi-delim(4)
                       count in ws-multi-count(4)
                   ws-multi-str(5)
                       delimiter in ws-multi-delim(5)
                       count in ws-multi-count(5)
                   ws-multi-str(6)
                       delimiter in ws-multi-delim(6)
                       count in ws-multi-count(6)
               tallying in ws-fields-filled
           end-unstring

           move "unstring fills 6 destination fields" to test-name
           move 6 to test-expected-num
           move ws-fields-filled to test-actual-num
           perform assert-num-equal

           move "first field is 'A'" to test-name
           move "A    " to test-expected-str
           move ws-multi-str(1) to test-actual-str
           perform assert-str-equal

           move "second field is 'B'" to test-name
           move "B    " to test-expected-str
           move ws-multi-str(2) to test-actual-str
           perform assert-str-equal

           move "third field is 'CD'" to test-name
           move "CD   " to test-expected-str
           move ws-multi-str(3) to test-actual-str
           perform assert-str-equal

           move "fourth field is 'EFG'" to test-name
           move "EFG  " to test-expected-str
           move ws-multi-str(4) to test-actual-str
           perform assert-str-equal

           move "fifth field is 'HIJ'" to test-name
           move "HIJ  " to test-expected-str
           move ws-multi-str(5) to test-actual-str
           perform assert-str-equal

           move "sixth field is 'KLMN'" to test-name
           move "KLMN " to test-expected-str
           move ws-multi-str(6) to test-actual-str
           perform assert-str-equal

           move "delimiter captured between 'A' and 'B' is '<'"
               to test-name
           move "<" to test-expected-str
           move ws-multi-delim(1) to test-actual-str
           perform assert-str-equal

           move "character count of 'CD' is 2" to test-name
           move 2 to test-expected-num
           move ws-multi-count(3) to test-actual-num
           perform assert-num-equal

           perform test-summary
           .

           copy "test-utils.cpy".

       end program test-unstring.
