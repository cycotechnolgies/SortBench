package com.sortbench.sorting;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.cyco.uni.sortbench.sorting.HeapSort;

class HeapSortTest {

    private final HeapSort heapSort = new HeapSort();

    @Test
    void testNormalArray() {
        assertArrayEquals(
            new double[]{1, 2, 3, 5, 8},
            heapSort.sort(new double[]{5, 2, 8, 1, 3})
        );
    }

    @Test
    void testDuplicates() {
        assertArrayEquals(
            new double[]{1, 2, 3, 3, 5, 9, 9},
            heapSort.sort(new double[]{9, 3, 5, 3, 1, 9, 2})
        );
    }

    @Test
    void testAlreadySorted() {
        assertArrayEquals(
            new double[]{1, 2, 3, 4, 5},
            heapSort.sort(new double[]{1, 2, 3, 4, 5})
        );
    }

    @Test
    void testReverseSorted() {
        assertArrayEquals(
            new double[]{1, 2, 3, 4, 5},
            heapSort.sort(new double[]{5, 4, 3, 2, 1})
        );
    }

    @Test
    void testEmptyArray() {
        assertArrayEquals(
            new double[]{},
            heapSort.sort(new double[]{})
        );
    }

    @Test
    void testSingleElement() {
        assertArrayEquals(
            new double[]{42},
            heapSort.sort(new double[]{42})
        );
    }

    @Test
    void testOriginalArrayUnchanged() {
        double[] input = {5, 1, 4, 2};
        double[] original = input.clone();

        heapSort.sort(input);

        assertArrayEquals(original, input);
    }

    @Test
    void testNullInput() {
        assertThrows(
            IllegalArgumentException.class,
            () -> heapSort.sort(null)
        );
    }

    @Test
    void testAlgorithmName() {
        assertEquals("Heap Sort", heapSort.getName());
    }
}

