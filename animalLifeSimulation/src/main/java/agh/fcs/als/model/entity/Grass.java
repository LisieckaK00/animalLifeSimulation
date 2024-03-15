package agh.fcs.als.model.entity;

import agh.fcs.als.model.utilities.Vector2d;

public record Grass(Vector2d position) implements Food{
    @Override
    public boolean isPoisonous() {
        return false;
    }
}
