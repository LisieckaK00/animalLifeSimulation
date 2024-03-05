package agh.fcs.als.model.utilities;

public record Vector2d(int x, int y) {
    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x(), this.y + other.y());
    }

    public boolean precedes(Vector2d other) {
        return this.x <= other.x() && this.y <= other.y();
    }

    public boolean follows(Vector2d other) {
        return this.x >= other.x() && this.y >= other.y();
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
