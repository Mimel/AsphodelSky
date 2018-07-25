package grid;

import java.util.Objects;

import static grid.Direction.*;

/**
 * Represents a set of 2D coordinates that pertain to a space on top of a grid.
 */
public class Point {
    private int x;

    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public void moveStepsInDirection(Direction direction, int numberOfSteps) {
        switch(direction) {
            case EAST:
                x += numberOfSteps;
                break;
            case WEST:
                x -= numberOfSteps;
                break;
            case NORTH:
                y -= numberOfSteps;
                break;
            case SOUTH:
                y += numberOfSteps;
                break;
        }
    }

    public boolean isValid(Point bounds) {
        return x >= 0 && y >= 0 && x < bounds.x && y < bounds.y;
    }

    double distanceTo(Point p2) {
        return Math.sqrt(Math.pow(p2.y() - y, 2) + Math.pow(p2.x() - x, 2));
    }

    /**
     * Gets the horizontal direction from this point to a given point.
     * @param p2 The second point to measure to.
     * @return EAST, CENTER, or WEST, depending if this point has a lower, equal or higher x-Coordinate value than the given point.
     */
    Direction horizontalDirectionToward(Point p2) {
        if(p2.x() > x) {
            return EAST;
        } else if(p2.x() < x) {
            return WEST;
        } else {
            return CENTER;
        }
    }

    /**
     * Gets the vertical direction from this point to a given point.
     * @param p2 The second point to measure to.
     * @return NORTH, CENTER, or SOUTH, depending if this point has a lower, equal or higher y-Coordinate value than the given point.
     */
    Direction verticalDirectionToward(Point p2) {
        if(p2.y() > y) {
            return SOUTH;
        } else if(p2.y() < y) {
            return NORTH;
        } else {
            return CENTER;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}
