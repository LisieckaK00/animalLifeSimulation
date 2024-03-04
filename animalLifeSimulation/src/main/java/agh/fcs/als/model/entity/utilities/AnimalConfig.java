package agh.fcs.als.model.entity.utilities;

public record AnimalConfig(int startEnergy,
                           int maxEnergy,
                           int energyGainedByEating,
                           int energyNeededToBeReadyForReproduction,
                           int energyUsedForReproduction,
                           int genomeLength) {
    private static void validatePositive(int value, String paramName) {
        if (value <= 0) {
            throw new IllegalArgumentException(paramName + " must be positive");
        }
    }

    private void validateMaxEnergy(){
        if(maxEnergy < startEnergy){
            throw new IllegalArgumentException("maxEnergy must be greater or equal to startEnergy.");
        }
    }

    private void validateReproductionEnergy(){
        if(energyNeededToBeReadyForReproduction < energyUsedForReproduction){
            throw new IllegalArgumentException("energyNeededToBeReadyForReproduction must be greater or equal to energyUsedForReproduction.");
        }
    }

    private void validateEnergyUsedForReproduction(){
        if(energyUsedForReproduction < 0){
            throw new IllegalArgumentException("energyUsedForReproduction must be equal to or greater than 0.");
        }
    }

    public AnimalConfig {
        AnimalConfig.validatePositive(startEnergy, "startEnergy");
        validateMaxEnergy();
        AnimalConfig.validatePositive(energyGainedByEating, "energyGainedByEating");
        validateEnergyUsedForReproduction();
        validateReproductionEnergy();
        AnimalConfig.validatePositive(genomeLength, "genomeLength");
    }
}