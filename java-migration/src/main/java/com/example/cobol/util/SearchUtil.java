package com.example.cobol.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchUtil {
    
    public static <T> int binarySearch(List<T> list, T key, Comparator<T> comparator) {
        return Collections.binarySearch(list, key, comparator);
    }
    
    public static <T> T binarySearchFind(List<T> list, T key, Comparator<T> comparator) {
        int index = Collections.binarySearch(list, key, comparator);
        if (index >= 0) {
            return list.get(index);
        }
        return null;
    }
    
    public static <T> T sequentialSearch(List<T> list, SearchPredicate<T> predicate) {
        for (T item : list) {
            if (predicate.test(item)) {
                return item;
            }
        }
        return null;
    }
    
    public static <T> List<T> sequentialSearchAll(List<T> list, SearchPredicate<T> predicate) {
        List<T> results = new ArrayList<>();
        for (T item : list) {
            if (predicate.test(item)) {
                results.add(item);
            }
        }
        return results;
    }
    
    @FunctionalInterface
    public interface SearchPredicate<T> {
        boolean test(T item);
    }
}
