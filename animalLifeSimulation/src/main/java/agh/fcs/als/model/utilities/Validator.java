package agh.fcs.als.model.utilities;

public class Validator {
    public static void validatePositive(int value, String paramName) {
        if (value <= 0) {
            throw new IllegalArgumentException(paramName + " must be positive");
        }
    }
    public static void validateNonNegative(int value, String paramName) {
        if (value < 0) {
            throw new IllegalArgumentException(paramName + " must be non-negative");
        }
    }

    public static void validateGreaterOrEqual(int value, int minValue, String paramName) {
        if (value < minValue) {
            throw new IllegalArgumentException(paramName + " must be greater than or equal to " + minValue);
        }
    }

    public static void validateNotNull(Object object, String objectName){
        if (object == null) {
            throw new IllegalArgumentException(objectName + " cannot be null");
        }
    }
}
