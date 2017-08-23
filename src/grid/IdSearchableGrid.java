package grid;

/**
 * Created by Owner on 8/22/2017.
 */
public interface IdSearchableGrid<T> extends Grid<T> {
    T getOccupantById(int id);

    Point getLocationById(int id);

    T removeOccuapantById(int id);
}
