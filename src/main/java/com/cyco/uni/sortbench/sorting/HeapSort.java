package com.cyco.uni.sortbench.sorting;

import java.util.Arrays;

public class HeapSort implements SortingAlgorithm {

    @Override
    public String getName() {
        return "Heap Sort";
    }

    @Override
    public double[] sort(double[] data) {

        if (data == null) {
            throw new IllegalArgumentException(
                "Input array cannot be null."
            );
        }

        // Preserve the original input array
        double[] sorted = Arrays.copyOf(data, data.length);
        int n = sorted.length;

        // Build a max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(sorted, n, i);
        }

        // Move the largest element to the end
        for (int i = n - 1; i > 0; i--) {

            swap(sorted, 0, i);

            // Restore the max heap
            heapify(sorted, i, 0);
        }

        return sorted;
    }

    private void heapify(double[] data, int heapSize, int root) {

        int largest = root;
        int left = 2 * root + 1;
        int right = 2 * root + 2;

        if (left < heapSize && data[left] > data[largest]) {
            largest = left;
        }

        if (right < heapSize && data[right] > data[largest]) {
            largest = right;
        }

        if (largest != root) {
            swap(data, root, largest);
            heapify(data, heapSize, largest);
        }
    }

    private void swap(double[] data, int i, int j) {
        double temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }
}
