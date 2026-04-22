      ******************************************************************
      * author: COBOL-Demo test framework
      * purpose: Unit tests for the CALL BY CONTENT / BY REFERENCE and
      *          CANCEL patterns in sub_program/main_app.cbl + sub.cbl.
      *          This test program CALLs the real sub-app routine so it
      *          must be compiled together with sub_program/sub.cbl:
      *              cobc -x -I test -o test-sub-program \
      *                   test/test-sub-program.cbl sub_program/sub.cbl
      ******************************************************************
       identification division.
       program-id. test-sub-program.

       data division.
       file section.

       working-storage section.
           copy "test-utils-data.cpy".

       01  ws-item-1                 pic x(10).
       01  ws-item-2                 pic x(10).

       procedure division.
       main-procedure.

      *>--------------------------------------------------------------
      *> CALL ... BY CONTENT does not modify the caller's variables
      *> even though the sub program overwrites its linkage fields.
      *>--------------------------------------------------------------
           move "alpha     " to ws-item-1
           move "beta      " to ws-item-2
           call "sub-app" using
               by content ws-item-1
               by content ws-item-2
           end-call

           move "BY CONTENT preserves caller's first item" to test-name
           move "alpha     " to test-expected-str
           move ws-item-1 to test-actual-str
           perform assert-str-equal

           move "BY CONTENT preserves caller's second item"
               to test-name
           move "beta      " to test-expected-str
           move ws-item-2 to test-actual-str
           perform assert-str-equal

      *>--------------------------------------------------------------
      *> CALL ... BY REFERENCE allows the sub program to overwrite
      *> the caller's variables with "replace1" / "replace2".
      *>--------------------------------------------------------------
           move "alpha     " to ws-item-1
           move "beta      " to ws-item-2
           call "sub-app" using
               ws-item-1 ws-item-2
           end-call

           move "BY REFERENCE overwrites caller's first item"
               to test-name
           move "replace1  " to test-expected-str
           move ws-item-1 to test-actual-str
           perform assert-str-equal

           move "BY REFERENCE overwrites caller's second item"
               to test-name
           move "replace2  " to test-expected-str
           move ws-item-2 to test-actual-str
           perform assert-str-equal

      *>--------------------------------------------------------------
      *> CANCEL resets the sub program's WORKING-STORAGE, so the next
      *> call must still succeed with fresh state.
      *>--------------------------------------------------------------
           cancel "sub-app"
           move "fresh1    " to ws-item-1
           move "fresh2    " to ws-item-2
           call "sub-app" using
               ws-item-1 ws-item-2
           end-call

           move "sub-app can be re-called after CANCEL" to test-name
           move "replace1  " to test-expected-str
           move ws-item-1 to test-actual-str
           perform assert-str-equal

           perform test-summary
           .

           copy "test-utils.cpy".

       end program test-sub-program.
