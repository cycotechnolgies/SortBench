/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sortbench.sorting;

import com.cyco.uni.sortbench.sorting.HeapSort;
import com.cyco.uni.sortbench.sorting.InsertionSort;
import com.cyco.uni.sortbench.sorting.MergeSort;
import com.cyco.uni.sortbench.sorting.QuickSort;
import com.cyco.uni.sortbench.sorting.ShellSort;
import com.cyco.uni.sortbench.sorting.SortingAlgorithm;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class SortingAlgorithmsTest {

    static Stream<SortingAlgorithm> sortingAlgorithms() {
        return Stream.of(
            new InsertionSort(),
            new ShellSort(),
            new MergeSort(),
            new QuickSort(),
            new HeapSort()
        );
    }

    @ParameterizedTest(name = "{0}: random values")
    @MethodSource("sortingAlgorithms")
    void testRandomValues(SortingAlgorithm algorithm) {
        Random random = new Random(12345);
        double[] input = new double[100];

        for (int i = 0; i < input.length; i++) {
            input[i] = random.nextInt(2001) - 1000;
        }

        double[] expected = input.clone();
        Arrays.sort(expected);

        assertArrayEquals(expected, algorithm.sort(input));
    }

    @ParameterizedTest(name = "{0}: already sorted")
    @MethodSource("sortingAlgorithms")
    void testAlreadySorted(SortingAlgorithm algorithm) {
        double[] input = {1, 2, 3, 4, 5};

        assertArrayEquals(
            new double[]{1, 2, 3, 4, 5},
            algorithm.sort(input)
        );
    }

    @ParameterizedTest(name = "{0}: reverse sorted")
    @MethodSource("sortingAlgorithms")
    void testReverseSorted(SortingAlgorithm algorithm) {
        double[] input = {5, 4, 3, 2, 1};

        assertArrayEquals(
            new double[]{1, 2, 3, 4, 5},
            algorithm.sort(input)
        );
    }

    @ParameterizedTest(name = "{0}: duplicate values")
    @MethodSource("sortingAlgorithms")
    void testDuplicateValues(SortingAlgorithm algorithm) {
        double[] input = {4, 2, 4, 1, 2, 4, 1};

        assertArrayEquals(
            new double[]{1, 1, 2, 2, 4, 4, 4},
            algorithm.sort(input)
        );
    }

    @ParameterizedTest(name = "{0}: negative numbers")
    @MethodSource("sortingAlgorithms")
    void testNegativeNumbers(SortingAlgorithm algorithm) {
        double[] input = {-5, 3, -1, 0, -10, 7};

        assertArrayEquals(
            new double[]{-10, -5, -1, 0, 3, 7},
            algorithm.sort(input)
        );
    }

    @ParameterizedTest(name = "{0}: empty array")
    @MethodSource("sortingAlgorithms")
    void testEmptyArray(SortingAlgorithm algorithm) {
        assertArrayEquals(
            new double[]{},
            algorithm.sort(new double[]{})
        );
    }

    @ParameterizedTest(name = "{0}: single element")
    @MethodSource("sortingAlgorithms")
    void testSingleElement(SortingAlgorithm algorithm) {
        assertArrayEquals(
            new double[]{42},
            algorithm.sort(new double[]{42})
        );
    }

    @ParameterizedTest(name = "{0}: two elements")
    @MethodSource("sortingAlgorithms")
    void testTwoElements(SortingAlgorithm algorithm) {
        assertArrayEquals(
            new double[]{1, 9},
            algorithm.sort(new double[]{9, 1})
        );
    }

    @ParameterizedTest(name = "{0}: original array unchanged")
    @MethodSource("sortingAlgorithms")
    void testOriginalArrayUnchanged(SortingAlgorithm algorithm) {
        double[] input = {5, -2, 8, 1, 3};
        double[] original = input.clone();

        algorithm.sort(input);

        assertArrayEquals(original, input);
    }

    @ParameterizedTest(name = "{0}: null input")
    @MethodSource("sortingAlgorithms")
    void testNullInput(SortingAlgorithm algorithm) {
        assertThrows(
            IllegalArgumentException.class,
            () -> algorithm.sort(null)
        );
    }
}
