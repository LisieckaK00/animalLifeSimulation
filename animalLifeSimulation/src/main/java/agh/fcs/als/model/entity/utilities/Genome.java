package agh.fcs.als.model.entity.utilities;

import agh.fcs.als.model.utilities.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Genome {
    private final List<Integer> genes;
    private int activeGene = 0;

    public Genome(List<Integer> genes) {
        validateGenes(genes);
        this.genes = genes;
    }

    private static void validateGenes(List<Integer> genes) {
        if (genes == null || genes.isEmpty()) {
            throw new IllegalArgumentException("Genes list cannot be empty or null.");
        }
        int maxValue = Direction.values().length;
        for (Integer gene : genes) {
            if (gene < 0 || gene >= maxValue) {
                throw new IllegalArgumentException("Gene values must be in range [0," + maxValue + ").");
            }
        }
    }

    public static Genome generate(int length) {
        validateLength(length);

        List<Integer> genes = new Random()
                .ints(0, Direction.values().length)
                .limit(length)
                .boxed()
                .toList();

        return new Genome(genes);
    }

    private static void validateLength(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Genome length must be greater than 0.");
        }
    }

    public static List<Integer> generateChildGenes(Genome g1, int energy1, Genome g2, int energy2) {
        validateParentsValues(g1, energy1, g2, energy2);

        int listLength = g1.getGenes().size();
        float divisionPoint = (float) energy2 / (energy1+ energy2) * listLength;
        int numbersFromWeakerAnimal = Math.max(1, Math.round(divisionPoint));
        List<Integer> result = new ArrayList<>(g1.getGenes());

        Random rand = new Random();
        boolean left = rand.nextFloat() < 0.5;
        int startIndex = left ? listLength-numbersFromWeakerAnimal : 0;
        int endIndex = left ? listLength : numbersFromWeakerAnimal;

        for (int i = startIndex; i < endIndex; i++) {
            result.set(i, g2.getGenes().get(i));
        }

        return result;
    }

    private static void validateParentsValues(Genome g1, int energy1, Genome g2, int energy2) {
        if(energy2 > energy1 || g1 == null || g2 == null || energy2 < 0){
            throw new IllegalArgumentException("Second energy must be greater than first.");
        }
    }

    public void updateActiveGene() {
        activeGene = ++activeGene % genes.size();
    }

    public int getActiveGene() {
        return activeGene;
    }

    public List<Integer> getGenes() {
        return genes;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Genome genome = (Genome) object;
        return activeGene == genome.activeGene && genes.equals(genome.genes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genes, activeGene);
    }
}
