package agh.fcs.als.model.utilities;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Vector2dTest {
    public static Stream<Arguments> addData() {
        return Stream.of(
                Arguments.of(new Vector2d(1,1), new Vector2d(1,2), new Vector2d(2,3)),
                Arguments.of(new Vector2d(-1,-1), new Vector2d(-1,-2), new Vector2d(-2,-3)),
                Arguments.of(new Vector2d(1,1), new Vector2d(-1,-2), new Vector2d(0,-1))
        );
    }

    @ParameterizedTest
    @MethodSource("addData")
    void testAdd(Vector2d v1, Vector2d v2, Vector2d expected){
        // when
        Vector2d result = v1.add(v2);

        // then
        assertEquals(expected, result);
    }

    public static Stream<Arguments> precedesData() {
        return Stream.of(
                Arguments.of(new Vector2d(1,1), new Vector2d(2,2), true),
                Arguments.of(new Vector2d(1,1), new Vector2d(1,2), true),
                Arguments.of(new Vector2d(2,2), new Vector2d(1,2), false),
                Arguments.of(new Vector2d(-1,-1), new Vector2d(-1,-2), false)
        );
    }

    @ParameterizedTest
    @MethodSource("precedesData")
    void testPrecedes(Vector2d v1, Vector2d v2, boolean expected){
        // when
        boolean result = v1.precedes(v2);

        // then
        assertEquals(expected, result);
    }

    public static Stream<Arguments> followsData() {
        return Stream.of(
                Arguments.of(new Vector2d(2,2), new Vector2d(1,1), true),
                Arguments.of(new Vector2d(1,2), new Vector2d(1,1), true),
                Arguments.of(new Vector2d(1,1), new Vector2d(1,2), false),
                Arguments.of(new Vector2d(-1,-2), new Vector2d(-1,-1), false)
        );
    }

    @ParameterizedTest
    @MethodSource("followsData")
    void testFollows(Vector2d v1, Vector2d v2, boolean expected){
        // when
        boolean result = v1.follows(v2);

        // then
        assertEquals(expected, result);
    }
}
