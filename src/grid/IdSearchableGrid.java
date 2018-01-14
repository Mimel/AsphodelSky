package grid;

/**
 * Represents a grid whose occupants can be found by an id parameter.
 */
public interface IdSearchableGrid<T, U> extends Grid<T, U> {
    T getOccupantById(int id);

    Point getLocationById(int id);

    T getClosestOccupant(int id, Direction horiz, Direction vert);

    T[] getAllOccupants();

    T removeOccuapantById(int id);
}
