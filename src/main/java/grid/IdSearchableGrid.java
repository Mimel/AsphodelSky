package grid;

/**
 * Represents a grid whose occupants can be found by an id parameter.
 */
interface IdSearchableGrid<T> extends Grid<T> {
    T getOccupantById(int id);

    Point getLocationById(int id);

    T getClosestOccupant(int id, Direction horiz, Direction vert);

    T[] getAllOccupants();

    T removeOccuapantById(int id);
}
