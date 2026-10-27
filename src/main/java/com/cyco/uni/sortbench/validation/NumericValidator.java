package com.cyco.uni.sortbench.validation;

import java.math.BigDecimal;

/**
 * Validates numeric input and distinguishes valid, missing,
 * and invalid values.
 *
 * @author RMCN Rathnayaka
 */
public class NumericValidator {

    public enum Status {
        VALID,
        MISSING,
        INVALID
    }

    /**
     * Classifies a value as valid, missing, or invalid.
     */
    public Status validate(String value) {
        if (value == null || value.isBlank()) {
            return Status.MISSING;
        }

        try {
            BigDecimal number = new BigDecimal(value.trim());

            return Double.isFinite(number.doubleValue())
                    ? Status.VALID
                    : Status.INVALID;
        } catch (NumberFormatException exception) {
            return Status.INVALID;
        }
    }

    /**
     * Converts a valid numeric value to a double.
     * invalid, or outside double range
     */
    public double parseNumber(String value) {
        Status status = validate(value);

        if (status != Status.VALID) {
            throw new IllegalArgumentException(
                    "Cannot parse numeric value. Status: "
                    + status + ", value: " + value);
        }

        return Double.parseDouble(value.trim());
    }
}
