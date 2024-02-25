package agh.fcs.als.model.utilities;

public record AnimalConfig(int startEnergy,
                           int maxEnergy,
                           int energyGainedByEating,
                           int energyNeededToBeReadyForReproduction,
                           int energyUsedForReproduction,
                           int genomeLength) {
    public AnimalConfig {
        Validator.validatePositive(startEnergy, "startEnergy");
        Validator.validateGreaterOrEqual(maxEnergy, startEnergy, "maxEnergy");
        Validator.validatePositive(energyGainedByEating, "energyGainedByEating");
        Validator.validateNonNegative(energyUsedForReproduction, "energyUsedForReproduction");
        Validator.validateGreaterOrEqual(energyNeededToBeReadyForReproduction, energyUsedForReproduction,
                "energyNeededToBeReadyForReproduction");
        Validator.validatePositive(genomeLength, "genomeLength");
    }
}