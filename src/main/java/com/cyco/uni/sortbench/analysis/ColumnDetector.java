package com.cyco.uni.sortbench.analysis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.cyco.uni.sortbench.model.Dataset;

/**
 * Identifies columns containing numeric data in a Dataset.
 * Empty cells are ignored during detection. A column is considered numeric
 * when it contains at least one non-empty value and every non-empty value
 * can be represented as a finite double.
 *
 * @author RMCN Rathnayaka
 */
public class ColumnDetector {

    /**
     * Returns the names of columns that contain numeric values only,
     * ignoring empty cells.
     */
    public List<String> detectNumericColumns(Dataset dataset) {
        Objects.requireNonNull(dataset, "Dataset cannot be null.");

        List<String> numericColumns = new ArrayList<>();

        for (int columnIndex = 0;
                columnIndex < dataset.getColumnCount();
                columnIndex++) {

            boolean hasNumericValue = false;
            boolean isNumericColumn = true;

            for (int rowIndex = 0;
                    rowIndex < dataset.getRowCount();
                    rowIndex++) {

                String value = dataset.getValueAt(rowIndex, columnIndex);

                if (value.isBlank()) {
                    continue;
                }

                if (!isNumeric(value)) {
                    isNumericColumn = false;
                    break;
                }

                hasNumericValue = true;
            }

            if (isNumericColumn && hasNumericValue) {
                numericColumns.add(
                        dataset.getColumnNames().get(columnIndex));
            }
        }

        return List.copyOf(numericColumns);
    }

    private boolean isNumeric(String value) {
        try {
            BigDecimal number = new BigDecimal(value.trim());
            return Double.isFinite(number.doubleValue());
        } catch (NumberFormatException exception) {
            return false;
        }
    }
}
