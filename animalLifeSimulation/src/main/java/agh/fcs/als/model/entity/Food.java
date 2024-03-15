package agh.fcs.als.model.entity;

import agh.fcs.als.model.utilities.Vector2d;

public interface Food {
    boolean isPoisonous();
    Vector2d position();
}
