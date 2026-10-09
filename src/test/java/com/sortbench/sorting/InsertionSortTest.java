
package com.sortbench.sorting;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.cyco.uni.sortbench.sorting.InsertionSort;

/**
 * Tests the correctness and expected behaviour of insertion sort.
 */
class InsertionSortTest {

    private final InsertionSort insertionSort = new InsertionSort();

    @Test
    void testNormalArray() {
        assertArrayEquals(
                new double[]{1, 2, 3, 5, 8},
                insertionSort.sort(new double[]{5, 2, 8, 1, 3})
        );
    }

    @Test
    void testDuplicates() {
        assertArrayEquals(
                new double[]{1, 2, 2, 4, 4},
                insertionSort.sort(new double[]{4, 2, 4, 1, 2})
        );
    }

    @Test
    void testAlreadySorted() {
        assertArrayEquals(
                new double[]{1, 2, 3, 4, 5},
                insertionSort.sort(new double[]{1, 2, 3, 4, 5})
        );
    }

    @Test
    void testReverseSorted() {
        assertArrayEquals(
                new double[]{1, 2, 3, 4, 5},
                insertionSort.sort(new double[]{5, 4, 3, 2, 1})
        );
    }

    @Test
    void testEmptyArray() {
        assertArrayEquals(
                new double[]{},
                insertionSort.sort(new double[]{})
        );
    }

    @Test
    void testSingleElement() {
        assertArrayEquals(
                new double[]{42},
                insertionSort.sort(new double[]{42})
        );
    }

    @Test
    void testOriginalArrayUnchanged() {
        double[] input = {5, 1, 4, 2};
        double[] original = input.clone();

        insertionSort.sort(input);

        assertArrayEquals(original, input);
    }

    @Test
    void testNullInput() {
        assertThrows(
                IllegalArgumentException.class,
                () -> insertionSort.sort(null)
        );
    }

    @Test
    void testAlgorithmName() {
        assertEquals("Insertion Sort", insertionSort.getName());
    }

    @Test
    void testNegativeNumbers() {
        assertArrayEquals(
                new double[]{-5, -2, -1, 0, 3},
                insertionSort.sort(new double[]{3, -1, -5, 0, -2})
        );
    }
}
