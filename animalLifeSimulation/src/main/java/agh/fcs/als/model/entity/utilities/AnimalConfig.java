package agh.fcs.als.model.entity.utilities;

public record AnimalConfig(int startEnergy,
                           int maxEnergy,
                           int energyGainedByEating,
                           int energyNeededToBeReadyForReproduction,
                           int energyUsedForReproduction,
                           int genomeLength,
                           int minMutationNumber,
                           int maxMutationNumber,
                           Mutation mutation
) {

    public AnimalConfig {
        validatePositive(startEnergy, "startEnergy");
        validateMaxEnergy(maxEnergy, startEnergy);
        validatePositive(energyGainedByEating, "energyGainedByEating");
        validateEnergyUsedForReproduction(energyUsedForReproduction);
        validateReproductionEnergy(energyNeededToBeReadyForReproduction, energyUsedForReproduction);
        validatePositive(genomeLength, "genomeLength");
        validateMutationNumbers(minMutationNumber, maxMutationNumber, genomeLength);
        validateMutation(mutation);
    }

    private static void validatePositive(int value, String paramName) {

        if (value <= 0) {
            throw new IllegalArgumentException(paramName + " must be positive");
        }
    }

    private void validateMaxEnergy(int maxEnergy, int startEnergy){
        if(maxEnergy < startEnergy){
            throw new IllegalArgumentException("maxEnergy must be greater or equal to startEnergy.");
        }
    }

    private void validateReproductionEnergy(int energyNeededToBeReadyForReproduction, int energyUsedForReproduction){
        if(energyNeededToBeReadyForReproduction < energyUsedForReproduction){
            throw new IllegalArgumentException("energyNeededToBeReadyForReproduction must be greater or equal to energyUsedForReproduction.");
        }
    }

    private void validateEnergyUsedForReproduction(int energyUsedForReproduction){
        if(energyUsedForReproduction < 0){
            throw new IllegalArgumentException("energyUsedForReproduction must be equal to or greater than 0.");
        }
    }

    private void validateMutationNumbers(int minMutationNumber, int maxMutationNumber, int genomeLength){
        if (minMutationNumber < 0 || minMutationNumber > genomeLength){
            throw new IllegalArgumentException("minMutationNumber must be between 0 and genomeLength");
        }
        if (maxMutationNumber < 0 || maxMutationNumber > genomeLength){
            throw new IllegalArgumentException("maxMutationNumber must be between 0 and genomeLength");
        }
        if (maxMutationNumber < minMutationNumber){
            throw new IllegalArgumentException("maxMutationNumber must be greater than or equal to minMutationNumber");
        }
    }

    private void validateMutation(Mutation mutation){
        if(mutation == null){
            throw new IllegalArgumentException("mutation cannot be null.");
        }
    }
}