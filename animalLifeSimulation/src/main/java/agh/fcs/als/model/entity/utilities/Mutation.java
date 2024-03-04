package agh.fcs.als.model.entity.utilities;

import java.util.List;

public interface Mutation {
    List<Integer> apply(List<Integer> genes, int mutationAmount);
}
