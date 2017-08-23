package grid;

/**
 * A series of objects T and associated coordinates.
 */
public interface Grid<T, U> extends Iterable<U> {

    void placeOccupant(T occupant, int x, int y);

    boolean canOccupy(int x, int y);

    T getOccupantAt(int x, int y);

    T removeOccupantAt(int x, int y);

    void clearGrid();

    Grid<T, U> subGrid(int x, int y, int width, int height);
}
