
package com.sortbench.sorting;

import com.cyco.uni.sortbench.sorting.QuickSort;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuickSortTest {

    private final QuickSort quickSort = new QuickSort();

    @Test
    void testNormalArray() {
        assertArrayEquals(
            new double[]{1, 2, 3, 5, 8},
            quickSort.sort(new double[]{5, 2, 8, 1, 3})
        );
    }

    @Test
    void testDuplicates() {
        assertArrayEquals(
            new double[]{1, 2, 3, 3, 5, 9, 9},
            quickSort.sort(new double[]{9, 3, 5, 3, 1, 9, 2})
        );
    }

    @Test
    void testAlreadySorted() {
        assertArrayEquals(
            new double[]{1, 2, 3, 4, 5},
            quickSort.sort(new double[]{1, 2, 3, 4, 5})
        );
    }

    @Test
    void testReverseSorted() {
        assertArrayEquals(
            new double[]{1, 2, 3, 4, 5},
            quickSort.sort(new double[]{5, 4, 3, 2, 1})
        );
    }

    @Test
    void testEmptyArray() {
        assertArrayEquals(new double[]{}, quickSort.sort(new double[]{}));
    }

    @Test
    void testSingleElement() {
        assertArrayEquals(new double[]{42}, quickSort.sort(new double[]{42}));
    }

    @Test
    void testOriginalArrayUnchanged() {
        double[] input = {5, 1, 4, 2};
        double[] original = input.clone();

        quickSort.sort(input);

        assertArrayEquals(original, input);
    }

    @Test
    void testNullInput() {
        assertThrows(
            IllegalArgumentException.class,
            () -> quickSort.sort(null)
        );
    }

    @Test
    void testAlgorithmName() {
        assertEquals("Quick Sort", quickSort.getName());
    }
}