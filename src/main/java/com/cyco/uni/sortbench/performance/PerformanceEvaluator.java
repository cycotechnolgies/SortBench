package com.cyco.uni.sortbench.performance;

import com.cyco.uni.sortbench.model.SortResult;
import com.cyco.uni.sortbench.sorting.SortingAlgorithm;
import java.util.ArrayList;
import java.util.List;

/** Measures sorting execution time using System.nanoTime(). */
public class PerformanceEvaluator {
    public List<SortResult> evaluate(double[] input, List<SortingAlgorithm> algorithms) {
        if (input == null || algorithms == null) {
            throw new IllegalArgumentException("Input and algorithms cannot be null.");
        }
        List<SortResult> results = new ArrayList<>();
        for (SortingAlgorithm algorithm : algorithms) {
            try {
                // Warm up the JVM/JIT before the measured run.
                algorithm.sort(input);
                long start = System.nanoTime();
                double[] sorted = algorithm.sort(input);
                long elapsed = System.nanoTime() - start;
                results.add(new SortResult(algorithm.getName(), elapsed / 1_000_000.0,
                        "Success", sorted));
            } catch (RuntimeException ex) {
                results.add(new SortResult(algorithm.getName(), Double.NaN,
                        "Failed: " + ex.getMessage(), null));
            }
        }
        return results;
    }
}
