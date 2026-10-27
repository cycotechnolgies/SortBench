package com.sortbench.analysis;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.cyco.uni.sortbench.analysis.ColumnDetector;
import com.cyco.uni.sortbench.model.Dataset;

class ColumnDetectorTest {

    private final ColumnDetector detector = new ColumnDetector();

    @Test
    void detectsIntegerAndDecimalColumns() {
        Dataset dataset = new Dataset(
                List.of("Name", "Age", "GPA"),
                List.of(
                        List.of("Amara", "20", "3.4"),
                        List.of("Nimal", "21", "2.9")
                ));

        assertEquals(
                List.of("Age", "GPA"),
                detector.detectNumericColumns(dataset));
    }

    @Test
    void excludesTextOnlyColumns() {
        Dataset dataset = new Dataset(
                List.of("Country", "Gender"),
                List.of(
                        List.of("Sri Lanka", "Female"),
                        List.of("India", "Male")
                ));

        assertTrue(detector.detectNumericColumns(dataset).isEmpty());
    }

    @Test
    void excludesColumnsContainingInvalidText() {
        Dataset dataset = new Dataset(
                List.of("Score"),
                List.of(
                        List.of("85"),
                        List.of("not available"),
                        List.of("92")
                ));

        assertTrue(detector.detectNumericColumns(dataset).isEmpty());
    }

    @Test
    void ignoresBlankCellsWhenDetectingNumericColumns() {
        Dataset dataset = new Dataset(
                List.of("Score"),
                List.of(
                        List.of("85"),
                        List.of(""),
                        List.of("92")
                ));

        assertEquals(
                List.of("Score"),
                detector.detectNumericColumns(dataset));
    }

    @Test
    void detectsNegativeAndDecimalValues() {
        Dataset dataset = new Dataset(
                List.of("Measurement"),
                List.of(
                        List.of("-12.5"),
                        List.of("0"),
                        List.of("3.75")
                ));

        assertEquals(
                List.of("Measurement"),
                detector.detectNumericColumns(dataset));
    }

    @Test
    void excludesColumnsWithOnlyBlankValues() {
        Dataset dataset = new Dataset(
                List.of("Score"),
                List.of(
                        List.of(""),
                        List.of("   ")
                ));

        assertTrue(detector.detectNumericColumns(dataset).isEmpty());
    }

       @Test
        void excludesValuesOutsideFiniteDoubleRange() {
        Dataset dataset = new Dataset(
                List.of("Score"),
                List.of(
                        List.of("85"),
                        List.of("1e10000")
                ));

        assertTrue(detector.detectNumericColumns(dataset).isEmpty());
    }

    @Test
    void returnsNoNumericColumnsForDatasetWithoutRows() {
        Dataset dataset = new Dataset(List.of("Age", "Score"), List.of());

        assertTrue(detector.detectNumericColumns(dataset).isEmpty());
    }

    @Test
    void rejectsNullDataset() {
        assertThrows(
                NullPointerException.class,
                () -> detector.detectNumericColumns(null));
    }
}
