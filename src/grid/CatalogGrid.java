package grid;

import item.Catalog;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Owner on 8/22/2017.
 */
public class CatalogGrid implements Grid<Catalog> {

    private Map<Pair<Integer, Integer>, Catalog> catalogMap;

    CatalogGrid() {
        catalogMap = new HashMap<>();
    }

    @Override
    public void placeOccupant(Catalog occupant, int x, int y) {
        catalogMap.put(new Pair<>(x, y), occupant);
    }

    @Override
    public boolean isOccupied(int x, int y) {
        return catalogMap.containsKey(new Pair<>(x, y));
    }

    @Override
    public Catalog getOccupantAt(int x, int y) {
        return catalogMap.get(new Pair<>(x, y));
    }

    @Override
    public Catalog removeOccupantAt(int x, int y) {
        return catalogMap.remove(new Pair<>(x, y));
    }

    @Override
    public void clearGrid() {
        catalogMap.clear();
    }
}
