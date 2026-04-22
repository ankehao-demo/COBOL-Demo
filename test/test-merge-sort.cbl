      ******************************************************************
      * author: COBOL-Demo test framework
      * purpose: Unit tests for the MERGE / SORT pattern demonstrated
      *          in merge_sort/merge_sort_test.cbl.
      *          Builds two unsorted input files, merges them in
      *          ascending customer-id order, then sorts the merged
      *          output in descending contract-id order and asserts
      *          the order of the resulting records.
      * tectonics: cobc -x -I test test/test-merge-sort.cbl
      ******************************************************************
       identification division.
       program-id. test-merge-sort.

       environment division.
       input-output section.
       file-control.
           select fd-test-in-1 assign to "tms-in-1.txt"
               organization is line sequential
               file status is ws-fs-1.

           select fd-test-in-2 assign to "tms-in-2.txt"
               organization is line sequential
               file status is ws-fs-2.

           select fd-sorting-file assign to "tms-work.txt".

           select fd-merged-file assign to "tms-merged.txt"
               organization is line sequential
               file status is ws-fs-merge.

           select fd-sorted-file assign to "tms-sorted.txt"
               organization is line sequential
               file status is ws-fs-sorted.

       data division.
       file section.

       sd  fd-sorting-file.
       01  f-customer-sort.
           05  f-customer-id-sort          pic 9(5).
           05  f-customer-contract-id-sort pic 9(5).

       fd  fd-test-in-1 recording mode F.
       01  f-customer-in-1.
           05  f-customer-id-in-1          pic 9(5).
           05  f-customer-contract-id-in-1 pic 9(5).

       fd  fd-test-in-2 recording mode F.
       01  f-customer-in-2.
           05  f-customer-id-in-2          pic 9(5).
           05  f-customer-contract-id-in-2 pic 9(5).

       fd  fd-merged-file recording mode F.
       01  f-customer-merged.
           05  f-customer-id-merged        pic 9(5).
           05  f-customer-contract-id-merged pic 9(5).

       fd  fd-sorted-file recording mode F.
       01  f-customer-sorted.
           05  f-customer-id-sorted        pic 9(5).
           05  f-customer-contract-id-sorted pic 9(5).

       working-storage section.
           copy "test-utils-data.cpy".

       01  ws-fs-1                   pic xx.
       01  ws-fs-2                   pic xx.
       01  ws-fs-merge               pic xx.
       01  ws-fs-sorted              pic xx.

       01  ws-eof-sw                 pic x value 'N'.
           88  ws-eof                value 'Y'.
           88  ws-not-eof            value 'N'.

       01  ws-merged-ids             occurs 6 times
                                      pic 9(5) value 0.
       01  ws-merged-count           pic 9(3) value 0.

       01  ws-sorted-contracts       occurs 6 times
                                      pic 9(5) value 0.
       01  ws-sorted-count           pic 9(3) value 0.

       01  ws-idx                    pic 9(3) value 0.

       procedure division.
       main-procedure.
           perform create-test-data
           perform merge-input-files
           perform read-merged-into-memory
           perform sort-merged-file
           perform read-sorted-into-memory

      *>--------------------------------------------------------------
      *> MERGE on ascending customer-id should interleave the two
      *> input files by id in ascending order.
      *>--------------------------------------------------------------
           move "merge interleaves files in ascending customer-id"
               to test-name
           move 6 to test-expected-num
           move ws-merged-count to test-actual-num
           perform assert-num-equal

           move "merged record 1 is the smallest id (1)" to test-name
           move 1 to test-expected-num
           move ws-merged-ids(1) to test-actual-num
           perform assert-num-equal

           move "merged record 2 is id 3" to test-name
           move 3 to test-expected-num
           move ws-merged-ids(2) to test-actual-num
           perform assert-num-equal

           move "merged record 3 is id 5" to test-name
           move 5 to test-expected-num
           move ws-merged-ids(3) to test-actual-num
           perform assert-num-equal

           move "merged record 4 is id 10" to test-name
           move 10 to test-expected-num
           move ws-merged-ids(4) to test-actual-num
           perform assert-num-equal

           move "merged record 5 is id 25" to test-name
           move 25 to test-expected-num
           move ws-merged-ids(5) to test-actual-num
           perform assert-num-equal

           move "merged record 6 is the largest id (30)"
               to test-name
           move 30 to test-expected-num
           move ws-merged-ids(6) to test-actual-num
           perform assert-num-equal

      *>--------------------------------------------------------------
      *> SORT on descending contract-id should order the merged
      *> records from highest to lowest contract id.
      *>--------------------------------------------------------------
           move "sorted record 1 has the highest contract id"
               to test-name
           move 12323 to test-expected-num
           move ws-sorted-contracts(1) to test-actual-num
           perform assert-num-equal

           move "sorted record 2 has contract id 8765" to test-name
           move 8765 to test-expected-num
           move ws-sorted-contracts(2) to test-actual-num
           perform assert-num-equal

           move "sorted record 3 has contract id 5423" to test-name
           move 5423 to test-expected-num
           move ws-sorted-contracts(3) to test-actual-num
           perform assert-num-equal

           move "sorted record 6 has the lowest contract id"
               to test-name
           move 247 to test-expected-num
           move ws-sorted-contracts(6) to test-actual-num
           perform assert-num-equal

           move "sorted records remain non-increasing" to test-name
           move 1 to test-actual-num
           perform varying ws-idx from 2 by 1
               until ws-idx > ws-sorted-count
               if ws-sorted-contracts(ws-idx - 1)
                      < ws-sorted-contracts(ws-idx)
                   move 0 to test-actual-num
               end-if
           end-perform
           perform assert-true

           perform test-summary
           .

       create-test-data.
           open output fd-test-in-1
           if ws-fs-1 not = "00"
               display "Failed to open input-1: " ws-fs-1
               stop run returning 2
           end-if

           move 1     to f-customer-id-in-1
           move 5423  to f-customer-contract-id-in-1
           write f-customer-in-1

           move 5     to f-customer-id-in-1
           move 12323 to f-customer-contract-id-in-1
           write f-customer-in-1

           move 10    to f-customer-id-in-1
           move 653   to f-customer-contract-id-in-1
           write f-customer-in-1

           close fd-test-in-1

           open output fd-test-in-2
           if ws-fs-2 not = "00"
               display "Failed to open input-2: " ws-fs-2
               stop run returning 2
           end-if

           move 30    to f-customer-id-in-2
           move 8765  to f-customer-contract-id-in-2
           write f-customer-in-2

           move 3     to f-customer-id-in-2
           move 3331  to f-customer-contract-id-in-2
           write f-customer-in-2

           move 25    to f-customer-id-in-2
           move 247   to f-customer-contract-id-in-2
           write f-customer-in-2

           close fd-test-in-2
           .

       merge-input-files.
           merge fd-sorting-file
               on ascending key f-customer-id-sort
               using fd-test-in-1 fd-test-in-2
               giving fd-merged-file
           .

       read-merged-into-memory.
           move 0 to ws-merged-count
           set ws-not-eof to true
           open input fd-merged-file
           if ws-fs-merge not = "00"
               display "Failed to open merged file: " ws-fs-merge
               stop run returning 2
           end-if

           perform until ws-eof
               read fd-merged-file
                   at end
                       set ws-eof to true
                   not at end
                       add 1 to ws-merged-count
                       move f-customer-id-merged
                           to ws-merged-ids(ws-merged-count)
               end-read
           end-perform
           close fd-merged-file
           .

       sort-merged-file.
           sort fd-sorting-file
               on descending key f-customer-contract-id-sort
               using fd-merged-file
               giving fd-sorted-file
           .

       read-sorted-into-memory.
           move 0 to ws-sorted-count
           set ws-not-eof to true
           open input fd-sorted-file
           if ws-fs-sorted not = "00"
               display "Failed to open sorted file: " ws-fs-sorted
               stop run returning 2
           end-if

           perform until ws-eof
               read fd-sorted-file
                   at end
                       set ws-eof to true
                   not at end
                       add 1 to ws-sorted-count
                       move f-customer-contract-id-sorted
                           to ws-sorted-contracts(ws-sorted-count)
               end-read
           end-perform
           close fd-sorted-file
           .

           copy "test-utils.cpy".

       end program test-merge-sort.
