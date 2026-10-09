
package com.cyco.uni.sortbench.sorting;

import java.util.Arrays;

/**
 * Sorts numeric arrays using the insertion sort algorithm.
 *
 * @author CYCO TECHNOLOGIES
 */
public class InsertionSort implements SortingAlgorithm {

    @Override
    public String getName() {
        return "Insertion Sort";
    }

    /**
     * Sorts the given array in ascending order using insertion sort.
     *
     * <p>Best-case time complexity: O(n).
     * Average and worst-case time complexity: O(n^2).</p>
     *
     * @param data the input array
     * @return a sorted copy of the input array
     * @throws IllegalArgumentException if data is null
     */

    @Override
    public double[] sort(double[] data) {
        if (data == null) {
            throw new IllegalArgumentException("Input array cannot be NULL");
        }

        // Work on a copy so the original array remains unchanged.
        double[] sorted = Arrays.copyOf(data, data.length);

        for (int i = 1; i < sorted.length; i++) {
            double key = sorted[i];
            int j = i - 1;

            // Shift larger elements right to make room for the key.
            while (j >= 0 && sorted[j] > key) {
                sorted[j + 1] = sorted[j];
                j--;
            }

            sorted[j + 1] = key;
        }

        return sorted;
    }
}
