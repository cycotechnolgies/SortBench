
package com.cyco.uni.sortbench.performance;

import com.cyco.uni.sortbench.model.SortResult;
import com.cyco.uni.sortbench.sorting.SortingAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class PerformanceEvaluator {

    public List<SortResult> evaluate(
            double[] input,
            List<SortingAlgorithm> algorithms) {

        if (input == null) {
            throw new IllegalArgumentException(
                    "Input array cannot be null.");
        }

        if (algorithms == null) {
            throw new IllegalArgumentException(
                    "Algorithm list cannot be null.");
        }

        List<SortResult> results = new ArrayList<>();

        for (SortingAlgorithm algorithm : algorithms) {

            if (algorithm == null) {
                continue;
            }

            try {
                // Warm-up run to reduce some JVM startup effects.
                algorithm.sort(input.clone());

                // Measure only the actual sorting operation.
                long startTime = System.nanoTime();

                double[] sorted = algorithm.sort(input.clone());

                long endTime = System.nanoTime();

                double executionTimeMs =
                        (endTime - startTime) / 1_000_000.0;

                // Validate the output.
                boolean isCorrect = sorted != null
                        && sorted.length == input.length;

                if (isCorrect) {
                    for (int i = 1; i < sorted.length; i++) {
                        if (sorted[i - 1] > sorted[i]) {
                            isCorrect = false;
                            break;
                        }
                    }
                }

                // Check that the algorithm did not lose/change values.
                if (isCorrect) {
                    double[] originalSorted = input.clone();
                    java.util.Arrays.sort(originalSorted);

                    double[] resultCopy = sorted.clone();
                    java.util.Arrays.sort(resultCopy);

                    for (int i = 0; i < originalSorted.length; i++) {
                        if (Double.compare(
                                originalSorted[i],
                                resultCopy[i]) != 0) {
                            isCorrect = false;
                            break;
                        }
                    }
                }

                String status = isCorrect
                        ? "Success"
                        : "Incorrect output";

                results.add(new SortResult(
                        algorithm.getName(),
                        executionTimeMs,
                        status,
                        sorted));

            } catch (RuntimeException ex) {
                results.add(new SortResult(
                        algorithm.getName(),
                        Double.NaN,
                        "Failed: " + ex.getMessage(),
                        null));
            }
        }

        return results;
    }
}
