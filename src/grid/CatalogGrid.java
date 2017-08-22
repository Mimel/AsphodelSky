package grid;

import item.Catalog;

/**
 * Created by Owner on 8/22/2017.
 */
public class CatalogGrid implements Grid<Catalog> {
    @Override
    public void placeOccupant(Catalog occupant, int x, int y) {

    }

    @Override
    public boolean isOccupied(int x, int y) {
        return false;
    }

    @Override
    public Catalog getOccupantAt(int x, int y) {
        return null;
    }

    @Override
    public Catalog removeOccupantAt(int x, int y) {
        return null;
    }

    @Override
    public void clearGrid() {

    }
}
