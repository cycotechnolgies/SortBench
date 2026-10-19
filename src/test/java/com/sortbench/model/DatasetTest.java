package com.sortbench.model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.cyco.uni.sortbench.model.Dataset;

class DatasetTest {

    @Test
    void storesColumnNamesRowsAndProvidesCellAccess() {
        Dataset dataset = new Dataset(
                List.of("Name", "Age"),
                List.of(
                        List.of("Amara", "20"),
                        List.of("Nimal", "21")
                )
        );

        assertEquals(List.of("Name", "Age"), dataset.getColumnNames());
        assertEquals(2, dataset.getRowCount());
        assertEquals(2, dataset.getColumnCount());
        assertEquals("Amara", dataset.getValueAt(0, 0));
        assertEquals("21", dataset.getValueAt(1, 1));
    }

    @Test
    void rejectsOutOfRangeCellIndices() {
        Dataset dataset = new Dataset(
                List.of("Name", "Age"),
                List.of(List.of("Amara", "20"))
        );

        assertThrows(IndexOutOfBoundsException.class,
                () -> dataset.getValueAt(1, 0));

        assertThrows(IndexOutOfBoundsException.class,
                () -> dataset.getValueAt(0, 2));
    }

    @Test
    void allowsHeadersWithNoDataRows() {
        Dataset dataset = new Dataset(
                List.of("Name", "Age"),
                List.of()
        );

        assertEquals(0, dataset.getRowCount());
        assertEquals(2, dataset.getColumnCount());
        assertEquals(List.of("Name", "Age"), dataset.getColumnNames());
    }

    @Test
    void rejectsNullColumnNames() {
        assertThrows(NullPointerException.class,
                () -> new Dataset(null, List.of()));
    }

    @Test
    void rejectsNullRowsCollection() {
        assertThrows(NullPointerException.class,
                () -> new Dataset(List.of("Name"), null));
    }

    @Test
    void rejectsEmptyColumnNames() {
        assertThrows(IllegalArgumentException.class,
                () -> new Dataset(List.of(), List.of()));
    }

    @Test
    void rejectsBlankColumnNames() {
        assertThrows(IllegalArgumentException.class,
                () -> new Dataset(List.of("Name", " "), List.of()));
    }

    @Test
    void rejectsRowsWithIncorrectNumberOfValues() {
        assertThrows(IllegalArgumentException.class,
                () -> new Dataset(
                        List.of("Name", "Age"),
                        List.of(List.of("Amara"))));
    }

    @Test
    void rejectsNullRows() {
        List<List<String>> rows = new ArrayList<>();
        rows.add(null);

        assertThrows(NullPointerException.class,
                () -> new Dataset(List.of("Name"), rows));
    }

    @Test
    void protectsAgainstChangesToInputCollections() {
        List<String> headers =
                new ArrayList<>(List.of("Name", "Age"));
        List<String> row =
                new ArrayList<>(List.of("Amara", "20"));
        List<List<String>> rows = new ArrayList<>();
        rows.add(row);

        Dataset dataset = new Dataset(headers, rows);

        headers.set(0, "Changed");
        row.set(0, "Changed");
        rows.clear();

        assertEquals(List.of("Name", "Age"), dataset.getColumnNames());
        assertEquals("Amara", dataset.getValueAt(0, 0));
        assertEquals(1, dataset.getRowCount());
    }

    @Test
    void preventsModificationThroughGetters() {
        Dataset dataset = new Dataset(
                List.of("Name"),
                List.of(List.of("Amara"))
        );

        assertThrows(UnsupportedOperationException.class,
                () -> dataset.getColumnNames().add("Age"));

        assertThrows(UnsupportedOperationException.class,
                () -> dataset.getRows().add(List.of("Nimal")));

        assertThrows(UnsupportedOperationException.class,
                () -> dataset.getRows().get(0).add("20"));
    }
}