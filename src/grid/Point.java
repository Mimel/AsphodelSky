package grid;

/**
 * Represents a set of 2D coordinates that pertain to a space on top of a grid.
 */
public class Point {
    private int x;

    private int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    //TODO test for workingness.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
