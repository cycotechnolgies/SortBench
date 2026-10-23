
package com.sortbench.sorting;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.cyco.uni.sortbench.sorting.MergeSort;

/**
 * Tests the correctness and expected behaviour of merge sort.
 */
class MergeSortTest {

    private final MergeSort mergeSort = new MergeSort();

    @Test
    void testNormalArray() {
        assertArrayEquals(
                new double[]{1, 2, 3, 5, 8},
                mergeSort.sort(new double[]{5, 2, 8, 1, 3})
        );
    }

    @Test
    void testDuplicates() {
        assertArrayEquals(
                new double[]{1, 2, 2, 4, 4},
                mergeSort.sort(new double[]{4, 2, 4, 1, 2})
        );
    }

    @Test
    void testAlreadySorted() {
        assertArrayEquals(
                new double[]{1, 2, 3, 4, 5},
                mergeSort.sort(new double[]{1, 2, 3, 4, 5})
        );
    }

    @Test
    void testReverseSorted() {
        assertArrayEquals(
                new double[]{1, 2, 3, 4, 5},
                mergeSort.sort(new double[]{5, 4, 3, 2, 1})
        );
    }

    @Test
    void testEmptyArray() {
        assertArrayEquals(
                new double[]{},
                mergeSort.sort(new double[]{})
        );
    }

    @Test
    void testSingleElement() {
        assertArrayEquals(
                new double[]{42},
                mergeSort.sort(new double[]{42})
        );
    }

    @Test
    void testOriginalArrayUnchanged() {
        double[] input = {5, 1, 4, 2};
        double[] original = input.clone();

        mergeSort.sort(input);

        assertArrayEquals(original, input);
    }

    @Test
    void testNullInput() {
        assertThrows(
                IllegalArgumentException.class,
                () -> mergeSort.sort(null)
        );
    }

    @Test
    void testAlgorithmName() {
        assertEquals("Merge Sort", mergeSort.getName());
    }

    @Test
    void testNegativeNumbers() {
        assertArrayEquals(
                new double[]{-5, -2, -1, 0, 3},
                mergeSort.sort(new double[]{3, -1, -5, 0, -2})
        );
    }

    @Test
    void testDecimalNumbers() {
        assertArrayEquals(
                new double[]{1.2, 2.5, 3.7, 4.8},
                mergeSort.sort(new double[]{3.7, 1.2, 4.8, 2.5})
        );
    }

    @Test
    void testTwoElements() {
        assertArrayEquals(
                new double[]{1, 9},
                mergeSort.sort(new double[]{9, 1})
        );
    }

    @Test
    void testUnevenArrayLength() {
        assertArrayEquals(
                new double[]{1, 2, 3, 4, 5, 6, 7},
                mergeSort.sort(new double[]{7, 2, 5, 1, 6, 3, 4})
        );
    }
}
