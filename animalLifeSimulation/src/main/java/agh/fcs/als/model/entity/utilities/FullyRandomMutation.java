package agh.fcs.als.model.entity.utilities;

import agh.fcs.als.model.utilities.Direction;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FullyRandomMutation implements Mutation{
    private final Random rand = new Random();

    @Override
    public List<Integer> apply(List<Integer> genes, int mutationAmount) {
        List<Integer> genesToMutate = IntStream.range(0, genes.size()).boxed().collect(Collectors.toList());
        Collections.shuffle(genesToMutate, rand);

        List<Integer> result = new ArrayList<>(genes);

        for (int i = 0; i < mutationAmount; i++) {
            result.set(genesToMutate.get(i), rand.nextInt(Direction.values().length));
        }

        return result;
    }
}
