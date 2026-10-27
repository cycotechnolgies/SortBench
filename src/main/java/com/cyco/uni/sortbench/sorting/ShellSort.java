package com.cyco.uni.sortbench.sorting;

import java.util.Arrays;

public class ShellSort implements SortingAlgorithm {

    @Override
    public String getName() {
        return "Shell Sort";
    }

    @Override
    public double[] sort(double[] data) {

        if (data == null) {
            throw new IllegalArgumentException(
                "Input array cannot be null."
            );
        }

        double[] sorted = Arrays.copyOf(data, data.length);

        int n = sorted.length;

        for (int gap = n / 2; gap > 0; gap /= 2) {

            for (int i = gap; i < n; i++) {

                double key = sorted[i];
                int j = i;

                while (j >= gap && sorted[j - gap] > key) {
                    sorted[j] = sorted[j - gap];
                    j -= gap;
                }

                sorted[j] = key;
            }
        }

        return sorted;
    }
}
