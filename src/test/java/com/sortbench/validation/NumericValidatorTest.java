package com.sortbench.validation;

import com.cyco.uni.sortbench.validation.NumericValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumericValidatorTest {

    private final NumericValidator validator = new NumericValidator();

    @Test
    void acceptsIntegerValues() {
        assertEquals(NumericValidator.Status.VALID,
                validator.validate("42"));
    }

    @Test
    void acceptsDecimalValues() {
        assertEquals(NumericValidator.Status.VALID,
                validator.validate("3.14159"));
    }

    @Test
    void acceptsNegativeValues() {
        assertEquals(NumericValidator.Status.VALID,
                validator.validate("-12.5"));
    }

    @Test
    void identifiesMissingValues() {
        assertEquals(NumericValidator.Status.MISSING,
                validator.validate(""));
        assertEquals(NumericValidator.Status.MISSING,
                validator.validate("   "));
        assertEquals(NumericValidator.Status.MISSING,
                validator.validate(null));
    }

    @Test
    void rejectsInvalidText() {
        assertEquals(NumericValidator.Status.INVALID,
                validator.validate("not a number"));
    }

    @Test
    void rejectsNaNAndInfinity() {
        assertEquals(NumericValidator.Status.INVALID,
                validator.validate("NaN"));
        assertEquals(NumericValidator.Status.INVALID,
                validator.validate("Infinity"));
        assertEquals(NumericValidator.Status.INVALID,
                validator.validate("1e10000"));
    }

    @Test
    void parsesValidValues() {
        assertEquals(42.0, validator.parseNumber("42"));
        assertEquals(-12.5, validator.parseNumber("-12.5"));
    }

    @Test
    void rejectsMissingOrInvalidValuesWhenParsing() {
        assertThrows(IllegalArgumentException.class,
                () -> validator.parseNumber(""));
        assertThrows(IllegalArgumentException.class,
                () -> validator.parseNumber("abc"));
    }
}
