package agh.fcs.als.model.entity.eating;

import agh.fcs.als.model.entity.Food;

public class EatWithoutInstinct implements EatStrategy{
    @Override
    public boolean eat(Food food) {
        return true;
    }
}
