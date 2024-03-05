package agh.fcs.als.model.utilities;

public enum Direction {
    N,
    NE,
    E,
    SE,
    S,
    SW,
    W,
    NW;

    public Direction rotate(int angle){
        return Direction.values()[(this.ordinal() + angle) % Direction.values().length];
    }

    public Direction turnAround(){
        int length = Direction.values().length;
        return Direction.values()[(this.ordinal() + length/2) % length];
    }

    public Vector2d toUnitVector(){
        return switch (this){
            case N -> new Vector2d(0, 1);
            case NE -> new Vector2d(1, 1);
            case E -> new Vector2d(1, 0);
            case SE -> new Vector2d(1, -1);
            case S -> new Vector2d(0, -1);
            case SW -> new Vector2d(-1, -1);
            case W -> new Vector2d(-1, 0);
            case NW -> new Vector2d(-1, 1);
        };
    }

}
