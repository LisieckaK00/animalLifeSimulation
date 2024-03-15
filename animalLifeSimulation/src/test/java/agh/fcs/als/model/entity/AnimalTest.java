package agh.fcs.als.model.entity;

import agh.fcs.als.model.entity.eating.EatStrategy;
import agh.fcs.als.model.entity.utilities.AnimalConfig;
import agh.fcs.als.model.entity.utilities.Genome;
import agh.fcs.als.model.entity.utilities.Mutation;
import agh.fcs.als.model.utilities.Direction;
import agh.fcs.als.model.utilities.MoveValidator;
import agh.fcs.als.model.utilities.Vector2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AnimalTest {
    static AnimalConfig config;
    static Genome genome;
    static EatStrategy eatStrategy;
    static Mutation mutation;

    @BeforeEach
    void setUp(){
        mutation = mock(Mutation.class);
        eatStrategy = mock(EatStrategy.class);
        config = new AnimalConfig(5, 100, 3, 10, 10, 3, 0, 0, eatStrategy, mutation);
        genome = mock(Genome.class);
    }

    @CsvSource({
            "0, 5, 5, 5, 'energy equals 0'",
            "-1, 5, 5, 5, 'energy less than 0'",
            "5, 0, 5, 5, 'age equals 0'",
            "5, -1, 5, 5, 'age less than 0'",
            "5, 5, -1, 5, 'grassEaten less than 0 (beginning of the interval)'",
            "5, 5, -10, 5, 'grassEaten less than 0'",
            "5, 5, 5, -1, 'childCount less than 0 (beginning of the interval)'",
            "5, 5, 5, -10, 'childCount less than 0'"
    })
    @ParameterizedTest(name = "{4}")
    void intArgumentsMustBeValid(int energy, int age, int grassEaten, int childCount, String displayName) {
        // given
        Vector2d position = new Vector2d(1, 1);

        // when trying to create animal with not valid values
        // then
        assertThrows(IllegalArgumentException.class,
                () -> new Animal(position, Direction.N, energy, age, grassEaten, childCount, config, genome));
    }

    static Stream<Arguments> argumentsCannotBeNullData() {
        Vector2d vector2d = new Vector2d(1, 1);
        Direction direction = Direction.N;
        return Stream.of(
                Arguments.of(null, direction, 5, 5, 5, 5, config),
                Arguments.of(vector2d, null, 5, 5, 5, 5, config),
                Arguments.of(vector2d, direction, 5, 5, 5, 5, null)
        );
    }

    @ParameterizedTest
    @MethodSource("argumentsCannotBeNullData")
    void argumentsCannotBeNull(Vector2d position, Direction direction, int energy, int age, int grassEaten, int childCount, AnimalConfig config) {
        // when trying to create animal with null params
        // then
        assertThrows(IllegalArgumentException.class,
                () -> new Animal(position, direction, energy, age, grassEaten, childCount, config, genome));
    }

    @Test
    void builderParamCannotBeNull() {
        // when trying to initialise Animal.Builder with null config
        // then
        assertThrows(IllegalArgumentException.class,
                () -> new Animal.Builder(null));
    }

    @Test
    void builderCreatesAnimal(){
        // given
        Vector2d position = new Vector2d(1,1);
        Animal animal = new Animal(position, Direction.E,5, 5, 5, 5, config, genome);

        // when
        Animal builderAnimal = new Animal.Builder(config)
                .setPosition(position)
                .setDirection(Direction.E)
                .setEnergy(5)
                .setAge(5)
                .setGrassEaten(5)
                .setChildCount(5)
                .setGenome(genome)
                .build();

        // then
        assertEquals(animal, builderAnimal);
    }

    public static Stream<Arguments> reproduceData() {
        return Stream.of(
                Arguments.of(30, List.of(1,1,2,2), 10, List.of(3,3,4,4), List.of(3,1,2,2), List.of(1,1,2,4)),
                Arguments.of(20, List.of(0,1,2), 10, List.of(3,4,5), List.of(0,1,5), List.of(3, 1, 2))
        );
    }

    @ParameterizedTest
    @MethodSource("reproduceData")
    void testReproduce_ValidValues(int energy1, List<Integer> genes1, int energy2, List<Integer> genes2, List<Integer> childGenes1, List<Integer> childGenes2){
        // given
        Vector2d vector2d = new Vector2d(2,2);
        Animal animal1 = new Animal.Builder(config)
                .setPosition(vector2d)
                .setEnergy(energy1)
                .setGenome(new Genome(genes1))
                .build();

        Animal animal2 = new Animal.Builder(config)
                .setPosition(vector2d)
                .setEnergy(energy2)
                .setGenome(new Genome(genes2))
                .build();

        Animal result1 = new Animal.Builder(config)
                .setPosition(vector2d)
                .setEnergy(2 * config.energyUsedForReproduction())
                .setGenome(new Genome(childGenes1))
                .build();
        Animal result2 = new Animal.Builder(config)
                .setPosition(vector2d)
                .setEnergy(2 * config.energyUsedForReproduction())
                .setGenome(new Genome(childGenes2))
                .build();

        // when
        when(mutation.apply(childGenes1, 0)).thenReturn(childGenes1);
        when(mutation.apply(childGenes2, 0)).thenReturn(childGenes2);
        Animal childAnimal = animal1.reproduce(animal2).get();

        // then
        assertTrue(result1.equals(childAnimal) || result2.equals(childAnimal),
                "the result should be\n"+result1+"\nor "+result2+"\nbut is "+childAnimal);
        verify(mutation).apply(anyList(), eq(0));
    }

    @Test
    void reproduceDifferentPosition(){
        // given
        Animal animal1 = new Animal.Builder(config)
                .setPosition(new Vector2d(1,1))
                .setEnergy(10)
                .build();

        Animal animal2 = new Animal.Builder(config)
                .setPosition(new Vector2d(1,2))
                .setEnergy(5)
                .build();

        // when trying to reproduce to animals on different positions
        // then
        assertThrows(IllegalArgumentException.class, () -> animal1.reproduce(animal2));
    }

    @Test
    void reproduceWrongAnimalsOrder(){
        // given
        Animal animal1 = new Animal.Builder(config)
                .setPosition(new Vector2d(1,1))
                .setEnergy(5)
                .build();

        Animal animal2 = new Animal.Builder(config)
                .setPosition(new Vector2d(1,1))
                .setEnergy(20)
                .build();

        // when trying to reproduce animals passed in wrong order (second animal has higher energy)
        // then
        assertThrows(IllegalArgumentException.class, () -> animal1.reproduce(animal2));
    }

    @Test
    void reproduceNotEnoughEnergy(){
        // given
        Animal animal1 = new Animal.Builder(config)
                .setPosition(new Vector2d(1,1))
                .setEnergy(20)
                .build();

        Animal animal2 = new Animal.Builder(config)
                .setPosition(new Vector2d(1,1))
                .setEnergy(3)
                .build();

        // when
        Optional<Animal> result = animal1.reproduce(animal2);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void moveNullValidator(){
        // given
        Animal animal = new Animal.Builder(config).build();
        // when trying to move animal passing null as moveValidator
        // then
        assertThrows(IllegalArgumentException.class, () -> animal.move(null));
    }

    public static Stream<Arguments> moveData() {
        return Stream.of(
                Arguments.of(true, new Vector2d(0, 1), Direction.N, Direction.N, List.of(0)),
                Arguments.of(true, new Vector2d(1,1), Direction.N, Direction.NE, List.of(1)),
                Arguments.of(true, new Vector2d(0, -1), Direction.E, Direction.S, List.of(2)),
                Arguments.of(false, new Vector2d(0,-1), Direction.N, Direction.S, List.of(0)),
                Arguments.of(false, new Vector2d(-1,-1), Direction.E, Direction.SW, List.of(7))
        );
    }

    @ParameterizedTest
    @MethodSource("moveData")
    void testOneMove(boolean canMoveToResult,
                  Vector2d finalPosition,
                  Direction startingDirection,
                  Direction finalDirection,
                  List<Integer> genes){
        // given
        Vector2d startPosition = new Vector2d(0,0);
        Genome genome = new Genome(genes);
        Animal animal = new Animal.Builder(config)
                .setPosition(startPosition)
                .setDirection(startingDirection)
                .setGenome(genome)
                .setEnergy(1)
                .build();
        MoveValidator moveValidator = mock(MoveValidator.class);

        // when
        when(moveValidator.canMoveTo(any(Vector2d.class))).thenReturn(canMoveToResult);
        animal.move(moveValidator);

        // then
        assertEquals(finalPosition, animal.getPosition());
        assertEquals(finalDirection, animal.getDirection());
        assertEquals(0, animal.getEnergy());
    }

    @Test
    void testSequenceOfMoves(){
        // given
        List<Integer> genes = List.of(0,1,2);
        int energy = 10;
        Animal animal = new Animal.Builder(config)
                .setEnergy(energy)
                .setDirection(Direction.N)
                .setPosition(new Vector2d(0,0))
                .setGenome(new Genome(genes))
                .build();
        List<Vector2d> positions = List.of(
                new Vector2d(0,1),
                new Vector2d(1, 2),
                new Vector2d(2, 1),
                new Vector2d(3,0)
        );
        List<Direction> directions = List.of(
                Direction.N,
                Direction.NE,
                Direction.SE,
                Direction.SE
        );
        MoveValidator moveValidator = mock(MoveValidator.class);
        when(moveValidator.canMoveTo(any(Vector2d.class))).thenReturn(true);

        for (int i = 0; i < 4; i++) {
            // when
            animal.move(moveValidator);

            //then
            assertEquals(positions.get(i), animal.getPosition());
            assertEquals(directions.get(i), animal.getDirection());
            assertEquals(energy-i-1, animal.getEnergy());
        }
    }

    @Test
    void moveNotPossibleWhenEnergyTooLow(){
        // given
        Animal animal = new Animal.Builder(config).setEnergy(1).build();
        MoveValidator moveValidator = mock(MoveValidator.class);
        animal.move(moveValidator);

        // when trying to move while having energy equal to 0
        // then
        assertThrows(IllegalStateException.class, () -> animal.move(moveValidator));
    }

    @Test
    void eatNullArgument(){
        // given
        Animal animal = new Animal.Builder(config).build();

        // when trying to pass null to eat()
        // then
        assertThrows(IllegalArgumentException.class, () -> animal.eat(null));
    }

    @Test
    void eatFoodOnDifferentPosition(){
        // given
        Animal animal = new Animal.Builder(config)
                .setPosition(new Vector2d(1,1))
                .build();
        Food food = mock(Food.class);
        when(food.position()).thenReturn(new Vector2d(1,2));

        // when trying to eat food on different position
        // then
        assertThrows(IllegalArgumentException.class, () -> animal.eat(food));
    }

    @Test
    void eatWhenEnergyIsFull(){
        //given
        Animal animal = new Animal.Builder(config)
                .setEnergy(config.maxEnergy())
                .build();
        Food food = mock(Food.class);
        when(food.position()).thenReturn(animal.getPosition());

        // when trying to eat while animal is full
        // then
        assertFalse(animal.eat(food));
    }

    static Stream<Arguments> eatData(){
        return Stream.of(
                Arguments.of(true, true, config.startEnergy()-config.energyGainedByEating()),
                Arguments.of(false, true, config.startEnergy()+config.energyGainedByEating()),
                Arguments.of(true, false, config.startEnergy()),
                Arguments.of(false, false, config.startEnergy())
        );
    }
    @ParameterizedTest
    @MethodSource("eatData")
    void testEat(boolean foodIsPoisonous, boolean foodShouldBeEaten, int finalEnergy){
        // given
        Animal animal = new Animal.Builder(config).build();
        Food food = mock(Food.class);
        when(food.position()).thenReturn(animal.getPosition());
        when(food.isPoisonous()).thenReturn(foodIsPoisonous);
        when(eatStrategy.eat(food)).thenReturn(foodShouldBeEaten);

        // when trying to eat food
        // then
        assertEquals(foodShouldBeEaten, animal.eat(food));
        assertEquals(finalEnergy, animal.getEnergy());
    }

    @Test
    void eatPoisonedFoodWithLowEnergy(){
        // given
        Animal animal = new Animal.Builder(config)
                .setEnergy(config.energyGainedByEating()-1)
                .build();
        Food food = mock(Food.class);
        when(food.position()).thenReturn(animal.getPosition());
        when(food.isPoisonous()).thenReturn(true);
        when(eatStrategy.eat(food)).thenReturn(true);

        // when trying to eat poisoned food having less energy than energyGainedByEating
        // then
        assertTrue(animal.eat(food));
        assertEquals(0, animal.getEnergy());
    }

    @Test
    void eatNotPoisonedFoodWithHighEnergy(){
        // given
        Animal animal = new Animal.Builder(config)
                .setEnergy(config.maxEnergy()-config.energyGainedByEating()+1)
                .build();
        Food food = mock(Food.class);
        when(food.position()).thenReturn(animal.getPosition());
        when(food.isPoisonous()).thenReturn(false);
        when(eatStrategy.eat(food)).thenReturn(true);

        // when trying to eat not poisoned food having energy nearly maxEnergy
        // then
        assertTrue(animal.eat(food));
        assertEquals(config.maxEnergy(), animal.getEnergy());
    }

}
