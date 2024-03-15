package agh.fcs.als.model.entity.eating;

import agh.fcs.als.model.entity.Food;

public interface EatStrategy {
    // Return true if food was eaten otherwise return false
    boolean eat(Food food);
}
