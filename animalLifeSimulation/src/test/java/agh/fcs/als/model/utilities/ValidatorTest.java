package agh.fcs.als.model.utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidatorTest {
    String paramName;

    @BeforeEach
    void setUp(){
        this.paramName = "TestParam";
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, Integer.MAX_VALUE})
    void validatePositive_ValidValue(int value){
        // when passing positive value to validatePositive()
        // then
        assertDoesNotThrow(() -> Validator.validatePositive(value, paramName));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -10, Integer.MIN_VALUE})
    void validatePositive_InvalidValue(int value){
        // when passing non-positive value to validatePositive()
        // then
        assertThrows(IllegalArgumentException.class, () -> Validator.validatePositive(value, paramName));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 10, Integer.MAX_VALUE})
    void validateNonNegative_ValidValue(int value){
        // when passing non-negative value to validateNonNegative()
        // then
        assertDoesNotThrow(() -> Validator.validateNonNegative(value, paramName));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10, Integer.MIN_VALUE})
    void validateNonNegative_InvalidValue(int value){
        // when passing negative value to validateNonNegative()
        // then
        assertThrows(IllegalArgumentException.class, () -> Validator.validateNonNegative(value, paramName));
    }

    @CsvSource({
            "-1, -1",
            "-1 ,-2",
            "1, 1",
            "2, 1"
    })
    @ParameterizedTest
    void validateGreaterOrEqual_ValidValues(int value, int minValue){
        // when passing value bigger than or equal to minValue to validateGreaterOrEqual()
        // then
        assertDoesNotThrow(() -> Validator.validateGreaterOrEqual(value, minValue, paramName));
    }

    @CsvSource({
            "-2, -1",
            "1, 2"
    })
    @ParameterizedTest
    void validateGreaterOrEqual_InvalidValues(int value, int minValue){
        // when passing value smaller than minValue to validateGreaterOrEqual()
        // then
        assertThrows(IllegalArgumentException.class, () -> Validator.validateGreaterOrEqual(value, minValue, paramName));
    }

    @Test
    void validateNotNull_ValidValue(){
        // given
        Object object = new Object();

        // when passing not null value to validateNotNull()
        // then
        assertDoesNotThrow(() -> Validator.validateNotNull(object, paramName));
    }

    @Test
    void validateNotNull_InvalidValue(){
        // when passing not null value to validateNotNull()
        // then
        assertThrows(IllegalArgumentException.class, () -> Validator.validateNotNull(null, paramName));
    }
}
