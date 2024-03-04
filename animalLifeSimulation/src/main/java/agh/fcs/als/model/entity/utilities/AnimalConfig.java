package agh.fcs.als.model.entity.utilities;

public record AnimalConfig(int startEnergy,
                           int maxEnergy,
                           int energyGainedByEating,
                           int energyNeededToBeReadyForReproduction,
                           int energyUsedForReproduction,
                           int genomeLength
) {

    public AnimalConfig {
        validatePositive(startEnergy, "startEnergy");
        validateMaxEnergy(maxEnergy, startEnergy);
        validatePositive(energyGainedByEating, "energyGainedByEating");
        validateEnergyUsedForReproduction(energyUsedForReproduction);
        validateReproductionEnergy(energyNeededToBeReadyForReproduction, energyUsedForReproduction);
        validatePositive(genomeLength, "genomeLength");
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
}