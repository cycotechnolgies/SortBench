package com.cyco.uni.sortbench.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a dataset containing column names and row values.
 *
 * @author RMCN Rathnayaka
 */
public class Dataset {
    
    private final List<String> columnNames;
    private final List<List<String>> rows;

    public Dataset(List<String> columnNames, List<List<String>> rows) {
        Objects.requireNonNull(columnNames, "Column names cannot be null.");
        Objects.requireNonNull(rows, "Rows cannot be null.");

        if (columnNames.isEmpty()) {
            throw new IllegalArgumentException(
                    "A dataset must contain at least one column.");
        }

        List<String> headers = List.copyOf(columnNames);

        for (String header : headers) {
            if (header.isBlank()) {
                throw new IllegalArgumentException(
                        "Column names cannot be blank.");
            }
        }

        List<List<String>> data = new ArrayList<>(rows.size());

        for (List<String> row : rows) {
            Objects.requireNonNull(row, "A row cannot be null.");

            if (row.size() != headers.size()) {
                throw new IllegalArgumentException(
                        "Row contains " + row.size()
                        + " values, but the dataset has "
                        + headers.size() + " columns.");
            }

            data.add(List.copyOf(row));
        }

        this.columnNames = headers;
        this.rows = List.copyOf(data);
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public int getRowCount() {
        return rows.size();
    }

    public int getColumnCount() {
        return columnNames.size();
    }

    public String getValueAt(int rowIndex, int columnIndex) {
        return rows.get(rowIndex).get(columnIndex);
    }


}
