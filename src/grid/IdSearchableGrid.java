package grid;

/**
 * Created by Owner on 8/22/2017.
 */
public interface IdSearchableGrid<T, U> extends Grid<T, U> {
    T getOccupantById(int id);

    Point getLocationById(int id);

    T removeOccuapantById(int id);
}
