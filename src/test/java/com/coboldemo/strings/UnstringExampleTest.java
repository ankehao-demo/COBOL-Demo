package com.coboldemo.strings;

import com.coboldemo.strings.UnstringExample.Pointer;
import com.coboldemo.strings.UnstringExample.UnstringFieldResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UnstringExampleTest {

    @Test
    void unstringOneWithSingleDelimiter() {
        Pointer pointer = new Pointer(0);
        String[] delimiters = {" "};
        UnstringFieldResult result = UnstringExample.unstringOne("Hello World", delimiters, false, pointer);
        assertEquals("Hello", result.value);
        assertEquals(" ", result.delimiter);
        assertEquals(5, result.charCount);
        assertEquals(6, pointer.value);
    }

    @Test
    void unstringOneWithAllDelimiters() {
        Pointer pointer = new Pointer(0);
        String[] delimiters = {" "};
        UnstringFieldResult result = UnstringExample.unstringOne("Hello   World", delimiters, true, pointer);
        assertEquals("Hello", result.value);
        assertEquals(" ", result.delimiter);
        assertEquals(5, result.charCount);
        assertEquals(8, pointer.value); // skipped all consecutive spaces
    }

    @Test
    void unstringMultipleDelimiters() {
        Pointer pointer = new Pointer(0);
        String[] delimiters = {"<", ">", "!"};
        UnstringFieldResult result = UnstringExample.unstringOne("A<B>C!D", delimiters, false, pointer);
        assertEquals("A", result.value);
        assertEquals("<", result.delimiter);
        assertEquals(1, result.charCount);
    }

    @Test
    void unstringMultipleFields() {
        Pointer pointer = new Pointer(0);
        String[] delimiters = {","};
        List<UnstringFieldResult> results = UnstringExample.unstringMultiple(
                "A,B,C", delimiters, false, pointer, 3);
        assertEquals(3, results.size());
        assertEquals("A", results.get(0).value);
        assertEquals("B", results.get(1).value);
        assertEquals("C", results.get(2).value);
    }

    @Test
    void unstringNoDelimiterFound() {
        Pointer pointer = new Pointer(0);
        String[] delimiters = {"|"};
        UnstringFieldResult result = UnstringExample.unstringOne("Hello", delimiters, false, pointer);
        assertEquals("Hello", result.value);
        assertEquals("", result.delimiter);
        assertEquals(5, pointer.value);
    }

    @Test
    void unstringPointerTracking() {
        Pointer pointer = new Pointer(0);
        String[] delimiters = {" "};

        UnstringFieldResult r1 = UnstringExample.unstringOne("Hello World Foo", delimiters, false, pointer);
        assertEquals("Hello", r1.value);
        assertEquals(6, pointer.value);

        UnstringFieldResult r2 = UnstringExample.unstringOne("Hello World Foo", delimiters, false, pointer);
        assertEquals("World", r2.value);
        assertEquals(12, pointer.value);

        UnstringFieldResult r3 = UnstringExample.unstringOne("Hello World Foo", delimiters, false, pointer);
        assertEquals("Foo", r3.value);
        assertEquals(15, pointer.value);
    }
}
