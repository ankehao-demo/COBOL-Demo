package com.coboldemo.datastructures;

import com.coboldemo.datastructures.SearchExample.KeyedItem;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SearchExampleTest {

    private final KeyedItem[] items = {
            new KeyedItem(1, 101, 500, "item 1", "2021/01/01"),
            new KeyedItem(2, 102, 499, "item 2", "2021/02/02"),
            new KeyedItem(3, 103, 498, "item 3", "2021/03/03")
    };

    @Test
    void binarySearchFindsExistingId() {
        assertEquals(0, SearchExample.binarySearchById1(items, 1));
        assertEquals(1, SearchExample.binarySearchById1(items, 2));
        assertEquals(2, SearchExample.binarySearchById1(items, 3));
    }

    @Test
    void binarySearchReturnsNegativeForMissingId() {
        assertEquals(-1, SearchExample.binarySearchById1(items, 4));
        assertEquals(-1, SearchExample.binarySearchById1(items, 0));
        assertEquals(-1, SearchExample.binarySearchById1(items, 999));
    }

    @Test
    void binarySearchOnEmptyArray() {
        assertEquals(-1, SearchExample.binarySearchById1(new KeyedItem[0], 1));
    }

    @Test
    void binarySearchOnSingleElement() {
        KeyedItem[] single = {new KeyedItem(5, 0, 0, "only", "2021/01/01")};
        assertEquals(0, SearchExample.binarySearchById1(single, 5));
        assertEquals(-1, SearchExample.binarySearchById1(single, 3));
    }
}
