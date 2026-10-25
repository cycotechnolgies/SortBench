package com.cyco.uni.sortbench.sorting;

import java.util.Arrays;

/**
 * Sorts numeric arrays using the merge sort algorithm.
 *
 * @author CYCO TECHNOLOGIES
 */
public class MergeSort implements SortingAlgorithm {

    @Override
    public String getName() {
        return "Merge Sort";
    }

    /**
     * Sorts the given array in ascending order using merge sort.
     *
     * <p>Time complexity: O(n log n).
     * Space complexity: O(n).</p>
     *
     * @param data the input array
     * @return a sorted copy of the input array
     * @throws IllegalArgumentException if data is null
     */
    @Override
    public double[] sort(double[] data) {
        if (data == null) {
            throw new IllegalArgumentException(
                    "Input array cannot be NULL");
        }

        // Work on a copy so the original array remains unchanged.
        double[] sorted = Arrays.copyOf(data, data.length);

        // An empty or single-element array is already sorted.
        if (sorted.length <= 1) {
            return sorted;
        }

        // Create a temporary array used during merging.
        double[] temp = new double[sorted.length];

        // Recursively sort the entire array.
        mergeSort(sorted, temp, 0, sorted.length - 1);

        return sorted;
    }

    /**
     * Recursively divides the array into smaller sections
     * and sorts each section.
     *
     * @param data the array being sorted
     * @param temp the temporary array used for merging
     * @param left the starting index
     * @param right the ending index
     */
    private void mergeSort(double[] data, double[] temp,
                           int left, int right) {

        // Base case: a section with zero or one element.
        if (left >= right) {
            return;
        }

        // Find the middle index without integer overflow.
        int mid = left + (right - left) / 2;

        // Sort the left half.
        mergeSort(data, temp, left, mid);

        // Sort the right half.
        mergeSort(data, temp, mid + 1, right);

        // Merge both sorted halves.
        merge(data, temp, left, mid, right);
    }

    /**
     * Merges two sorted sections into one sorted section.
     *
     * @param data the array being sorted
     * @param temp the temporary array used for merging
     * @param left the starting index of the left section
     * @param mid the ending index of the left section
     * @param right the ending index of the right section
     */
    private void merge(double[] data, double[] temp,
                       int left, int mid, int right) {

        int i = left;      // Current position in the left half.
        int j = mid + 1;   // Current position in the right half.
        int k = left;      // Current position in the temporary array.

        // Compare elements from both halves and copy
        // the smaller element into the temporary array.
        while (i <= mid && j <= right) {
            if (Double.compare(data[i], data[j]) <= 0) {
                temp[k++] = data[i++];
            } else {
                temp[k++] = data[j++];
            }
        }

        // Copy any remaining elements from the left half.
        while (i <= mid) {
            temp[k++] = data[i++];
        }

        // Copy any remaining elements from the right half.
        while (j <= right) {
            temp[k++] = data[j++];
        }

        // Copy the merged section back into the original
        // working array.
        for (int index = left; index <= right; index++) {
            data[index] = temp[index];
        }
    }
}
