      ******************************************************************
      * author: COBOL-Demo test framework
      * purpose: Unit tests for the REDEFINES patterns demonstrated in
      *          redifines/redefines.cbl.
      * tectonics: cobc -x -I test test/test-redefines.cbl
      ******************************************************************
       identification division.
       program-id. test-redefines.

       data division.
       file section.

       working-storage section.
           copy "test-utils-data.cpy".

       01  ws-customer.
           05  ws-customer-name.
               10  ws-customer-first-name  pic x(10).
               10  ws-customer-last-name   pic x(20).
           05  ws-corp-name redefines ws-customer-name
                                                     pic x(30).

       procedure division.
       main-procedure.

      *>--------------------------------------------------------------
      *> Writing to the original field makes the redefinition visible
      *> as a single concatenated value covering the same storage.
      *>--------------------------------------------------------------
           move "first/last name written, corp name sees combined"
               to test-name
           move "alice     " to ws-customer-first-name
           move "smith               " to ws-customer-last-name
           move "alice     smith               "
               to test-expected-str
           move ws-corp-name to test-actual-str
           perform assert-str-equal

      *>--------------------------------------------------------------
      *> Writing to the redefined corp-name overlays the first-name
      *> portion of the original layout.
      *>--------------------------------------------------------------
           move "writing corp-name overlays first-name segment"
               to test-name
           move "Acme Corp Incorporated        " to ws-corp-name
           move "Acme Corp " to test-expected-str
           move ws-customer-first-name to test-actual-str
           perform assert-str-equal

           move "writing corp-name overlays last-name segment"
               to test-name
           move "Incorporated        " to test-expected-str
           move ws-customer-last-name to test-actual-str
           perform assert-str-equal

      *>--------------------------------------------------------------
      *> The two names share the same 30 bytes of storage.
      *>--------------------------------------------------------------
           move "customer-name and corp-name share storage length"
               to test-name
           move 30 to test-expected-num
           compute test-actual-num =
               function length(ws-customer-name)
           perform assert-num-equal

           move "corp-name storage length is 30" to test-name
           move 30 to test-expected-num
           compute test-actual-num = function length(ws-corp-name)
           perform assert-num-equal

           perform test-summary
           .

           copy "test-utils.cpy".

       end program test-redefines.
