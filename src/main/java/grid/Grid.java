package grid;

/**
 * A series of objects T that can be represented as elements in a Cartesian plane.
 */
interface Grid<T> {

    /**
     * Places an occupant T on the given point (X, Y), where X and Y are
     * non-negative integers. If the coordinate (X, Y) exceeds the bounds
     * of the given plane or is already occupied, then T is not placed.
     * @param occupant The occupant of type T to place.
     * @param location A point consisting of non-negative integers that represents the location to place this occupant.
     */
    void placeOccupant(T occupant, Point location);

    /**
     * Checks if a point (X, Y) is a valid location for the placement of an object.
     * If a location is valid, then an occupant MUST be able to be placed at that coordinate.
     * @param location A point consisting of non-negative integers.
     * @return True if the location given is valid.
     */
    boolean canOccupy(Point location);

    /**
     * Gets the occupant T at a given location.
     * @param location A point to retrieve the occupant from.
     * @return The occupant if it exists at that location, or null otherwise.
     */
    T getOccupantAt(Point location);
}
