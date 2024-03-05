package agh.fcs.als.model.entity.utilities;

import net.jqwik.api.Assume;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Size;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FullyRandomMutationTest {
    @Property
    void testApply(@ForAll @Size(max = 10) List<Integer> genes, @ForAll @IntRange(min = 0, max = 10) int mutationAmount){
        Assume.that(mutationAmount <= genes.size());

        // given
        Mutation mutation = new FullyRandomMutation();

        // when
        List<Integer> result = mutation.apply(genes, mutationAmount);
        long notEqual = IntStream
                .range(0, genes.size())
                .filter(i -> !Objects.equals(result.get(i), genes.get(i)))
                .count();

        // then
        assertTrue(notEqual <= mutationAmount);
    }
}
