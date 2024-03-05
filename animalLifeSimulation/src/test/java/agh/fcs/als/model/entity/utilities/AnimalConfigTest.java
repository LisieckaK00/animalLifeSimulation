package agh.fcs.als.model.entity.utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class AnimalConfigTest {
    static Mutation mutation;

    @BeforeEach
    void setUp(){
        mutation = mock(Mutation.class);
    }

    @CsvSource({
            "0, 5, 5, 5, 5, 5, 'startEnergy equals to 0'",
            "-1, 5, 5, 5, 5, 5, 'startEnergy is negative'",
            "5, 3, 5, 5, 5, 5, 'maxEnergy smaller than startEnergy'",
            "5, 5, 0, 5, 5, 5, 'energyGainedByEating equals to 0'",
            "5, 5, -1, 5, 5, 5, 'energyGainedByEating is negative'",
            "5, 5, 5, -1, 5, 5, 'energyNeededToBeReadyForReproduction is negative'",
            "5, 5, 5, 1, 3, 5, 'energyUsedForReproduction is greater than energyNeededToBeReadyForReproduction'",
            "5, 5, 5, 5, -1, 5, 'energyUsedForReproduction is negative'",
            "5, 5, 5, 5, 5, 0, 'genomeLength equals to 0'",
            "5, 5, 5, 5, 5, -1, 'genomeLength is negative'"
    })
    @ParameterizedTest(name = "{6}")
    void throwException_NotValidValues(int startEnergy,
                                       int maxEnergy,
                                       int energyGainedByEating,
                                       int energyNeededToBeReadyForReproduction,
                                       int energyUsedForReproduction,
                                       int genomeLength,
                                       String displayName){
        assertThrows(IllegalArgumentException.class,
                () -> new AnimalConfig(startEnergy, maxEnergy, energyGainedByEating,
                        energyNeededToBeReadyForReproduction, energyUsedForReproduction, genomeLength, mutation));
    }

    @CsvSource({
            "1, 1, 1, 0, 0, 1, 'minimum values for attributes'",
            "5, 10, 5, 5, 3, 5, 'values in range'"
    })
    @ParameterizedTest(name = "{6}")
    void DoNotThrowException_ValidValues(int startEnergy,
                                         int maxEnergy,
                                         int energyGainedByEating,
                                         int energyNeededToBeReadyForReproduction,
                                         int energyUsedForReproduction,
                                         int genomeLength,
                                         String displayName){
        assertDoesNotThrow(() -> new AnimalConfig(startEnergy, maxEnergy, energyGainedByEating,
                energyNeededToBeReadyForReproduction, energyUsedForReproduction, genomeLength, mutation));
    }

    @Test
    void throwException_NullMutation(){
        assertThrows(IllegalArgumentException.class,
                () -> new AnimalConfig(5, 10, 5,
                        5, 5, 5, null));
    }
}