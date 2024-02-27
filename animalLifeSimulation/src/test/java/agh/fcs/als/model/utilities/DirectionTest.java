package agh.fcs.als.model.utilities;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectionTest {
    static Stream<Arguments> rotateData() {
        return Stream.of(
                Arguments.of(Direction.N, 0, Direction.N),
                Arguments.of(Direction.N, 5, Direction.SW),
                Arguments.of(Direction.N, 7, Direction.NW),
                Arguments.of(Direction.NE, 0, Direction.NE),
                Arguments.of(Direction.NE, 5, Direction.W),
                Arguments.of(Direction.NE, 7, Direction.N),
                Arguments.of(Direction.E, 0, Direction.E),
                Arguments.of(Direction.E, 5, Direction.NW),
                Arguments.of(Direction.E, 7, Direction.NE),
                Arguments.of(Direction.SE, 0, Direction.SE),
                Arguments.of(Direction.SE, 5, Direction.N),
                Arguments.of(Direction.SE, 7, Direction.E),
                Arguments.of(Direction.S, 0, Direction.S),
                Arguments.of(Direction.S, 5, Direction.NE),
                Arguments.of(Direction.S, 7, Direction.SE),
                Arguments.of(Direction.SW, 0, Direction.SW),
                Arguments.of(Direction.SW, 5, Direction.E),
                Arguments.of(Direction.SW, 7, Direction.S),
                Arguments.of(Direction.W, 0, Direction.W),
                Arguments.of(Direction.W, 5, Direction.SE),
                Arguments.of(Direction.W, 7, Direction.SW),
                Arguments.of(Direction.NW, 0, Direction.NW),
                Arguments.of(Direction.NW, 5, Direction.S),
                Arguments.of(Direction.NW, 7, Direction.W)
        );
    }

    @ParameterizedTest
    @MethodSource("rotateData")
    void testRotate(Direction start, int angle, Direction expected){
        // when
        Direction result = start.rotate(angle);

        // then
        assertEquals(expected, result);
    }

    static Stream<Arguments> turnAroundData() {
        return Stream.of(
                Arguments.of(Direction.N, Direction.S),
                Arguments.of(Direction.NE, Direction.SW),
                Arguments.of(Direction.E, Direction.W),
                Arguments.of(Direction.SE, Direction.NW),
                Arguments.of(Direction.S, Direction.N),
                Arguments.of(Direction.SW, Direction.NE),
                Arguments.of(Direction.W, Direction.E),
                Arguments.of(Direction.NW, Direction.SE)
        );
    }

    @ParameterizedTest
    @MethodSource("turnAroundData")
    void testTurnAround(Direction start, Direction expected){
        // when
        Direction result = start.turnAround();

        // then
        assertEquals(expected, result);
    }

    static Stream<Arguments> toUnitVectorData() {
        return Stream.of(
                Arguments.of(Direction.N, new Vector2d(0,1)),
                Arguments.of(Direction.NE, new Vector2d(1,1)),
                Arguments.of(Direction.E, new Vector2d(1,0)),
                Arguments.of(Direction.SE, new Vector2d(1,-1)),
                Arguments.of(Direction.S, new Vector2d(0,-1)),
                Arguments.of(Direction.SW, new Vector2d(-1,-1)),
                Arguments.of(Direction.W, new Vector2d(-1,0)),
                Arguments.of(Direction.NW, new Vector2d(-1,1))
        );
    }

    @ParameterizedTest
    @MethodSource("toUnitVectorData")
    void testToUnitVector(Direction start, Vector2d expected){
        // when
        Vector2d result = start.toUnitVector();

        // then
        assertEquals(expected, result);
    }

}
