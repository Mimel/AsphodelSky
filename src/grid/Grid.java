package grid;

/**
 * A series of objects T and associated coordinates.
 */
interface Grid<T> {
    void placeOccupant(T occupant, int x, int y);

    boolean isOccupied(int x, int y);

    T getOccupantAt(int x, int y);

    T removeOccupantAt(int x, int y);

    void clearGrid();
}
