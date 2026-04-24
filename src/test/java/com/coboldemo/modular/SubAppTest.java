package com.coboldemo.modular;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubAppTest {

    @Test
    void executeModifiesReferenceItems() {
        SubApp subApp = new SubApp();
        String[] items = {"input1    ", "input2    "};
        subApp.execute(items);
        // SubApp replaces items with "replace1" and "replace2"
        assertTrue(items[0].startsWith("replace1"));
        assertTrue(items[1].startsWith("replace2"));
    }

    @Test
    void workingStoragePersistsBetweenCalls() {
        SubApp subApp = new SubApp();
        String[] items1 = {"first1    ", "first2    "};
        subApp.execute(items1);

        // Second call should see working-storage values from first call
        String[] items2 = {"second1   ", "second2   "};
        subApp.execute(items2);
        // Both calls modify items to replace1/replace2
        assertTrue(items2[0].startsWith("replace1"));
    }

    @Test
    void cancelResetsWorkingStorage() {
        SubApp subApp = new SubApp();
        String[] items = {"val1      ", "val2      "};
        subApp.execute(items);

        // "Cancel" = create new instance
        SubApp newSubApp = new SubApp();
        String[] items2 = {"new1      ", "new2      "};
        newSubApp.execute(items2);
        // New instance has fresh working-storage
        assertTrue(items2[0].startsWith("replace1"));
    }
}
