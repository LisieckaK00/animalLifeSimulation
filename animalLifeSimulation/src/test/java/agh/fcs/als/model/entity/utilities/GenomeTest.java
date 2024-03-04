package agh.fcs.als.model.entity.utilities;

import agh.fcs.als.model.utilities.Direction;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class GenomeTest {
    @Test
    void testUpdateGene(){
        // given
        List<Integer> genes = List.of(1,2,3,4,5);
        Genome genome = new Genome(genes);
        List<Integer> activeGenes = List.of(1,2,3,4,0,1,2);

        for (int i=0; i<7; i++){
            // when
            genome.updateActiveGene();
            // then
            assertEquals(activeGenes.get(i), genome.getActiveGene());
        }
    }
    public static Stream<Arguments> genomeNotValidGenes() {
        int maxGeneValue = Direction.values().length;
        return Stream.of(
                Arguments.of(List.of(-1,0,0)),
                Arguments.of(List.of(0,-1,0)),
                Arguments.of(List.of(0,0,-1)),
                Arguments.of(List.of(-1,-1,0)),
                Arguments.of(List.of(-1,-1,-1)),
                Arguments.of(List.of(maxGeneValue,0,0)),
                Arguments.of(List.of(0,maxGeneValue,0)),
                Arguments.of(List.of(0,0,maxGeneValue)),
                Arguments.of(List.of(maxGeneValue,maxGeneValue,0)),
                Arguments.of(List.of(maxGeneValue,maxGeneValue,maxGeneValue))
        );
    }

    @ParameterizedTest
    @MethodSource("genomeNotValidGenes")
    @NullAndEmptySource
    void createGenomeWithInvalidValues(List<Integer> genes){
        // when trying to create Genome with invalid genes
        // then
        assertThrows(IllegalArgumentException.class, ()->new Genome(genes));
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "10"
    })
    void testGenerate_ValidValues(int genomeLength){
        // when
        Genome genome = Genome.generate(genomeLength);

        // then
        assertEquals(genomeLength, genome.getGenes().size());
    }

    @ParameterizedTest
    @CsvSource({
            "0",
            "-10"
    })
    void testGenerate_NotValidValues(int genomeLength){
        // when trying to generate genome with length less than 1
        // then
        assertThrows(IllegalArgumentException.class, ()->Genome.generate(genomeLength));
    }

    static Stream<Arguments> childGenomeNotValidValues(){
        Genome genome = new Genome(List.of(1,2,3));

        return Stream.of(
                Arguments.of(null, 5, genome, 5, "first genome is null"),
                Arguments.of(genome, 5, null, 5, "second genome is null"),
                Arguments.of(genome, -1, genome, 5, "first energy equals to -1"),
                Arguments.of(genome, 5, genome, -1, "second energy equals to -1"),
                Arguments.of(genome, 5, genome, 7, "second energy is greater than first one")
        );
    }

    @ParameterizedTest(name = "{4}")
    @MethodSource("childGenomeNotValidValues")
    void testGenerateChildGenome_NotValidValues(Genome g1, int energy1, Genome g2, int energy2, String displayName){
        // when trying to pass not valid arguments to generateChildGenes()
        // then
        assertThrows(IllegalArgumentException.class, () -> Genome.generateChildGenes(g1, energy1, g2, energy2));
    }

    static Stream<Arguments> childGenomeValidValues(){
        Genome genome0123 = new Genome(List.of(0,1,2,3));
        Genome genome4567 = new Genome(List.of(4,5,6,7));
        List<Integer> list0127 = List.of(0,1,2,7);
        List<Integer> list4123 = List.of(4,1,2,3);

        return Stream.of(
                Arguments.of(genome0123, 3, genome4567, 1, list4123, list0127,"list length is divisible by energy sum"),
                Arguments.of(genome0123, 4, genome4567, 1, list4123, list0127, "list length is not divisible by energy sum"),
                Arguments.of(genome0123, 37, genome4567, 10, list4123, list0127, "list length is not divisible by energy sum (not round numbers)"),
                Arguments.of(new Genome(List.of(0,1,2)), 2, new Genome(List.of(3,4,5)), 2, List.of(0,4,5), List.of(3, 4, 2), "energies are equal")
        );
    }

    @ParameterizedTest(name = "{6}")
    @MethodSource("childGenomeValidValues")
    void testGenerateChildGenome_ValidValues(Genome g1, int energy1, Genome g2, int energy2, List<Integer> result1, List<Integer> result2, String displayName){
        // when
        List<Integer> genes = Genome.generateChildGenes(g1, energy1, g2, energy2);

        // then
        assertTrue(genes.equals(result1) || genes.equals(result2));
    }
}
