package grid;

import item.Catalog;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the catalog overlay. Consists of a map that relates grid coordinates
 * to the items that are on it.
 */
public class CatalogGrid implements Grid<Catalog> {

    private final Map<Point, Catalog> catalogMap;

    CatalogGrid() {
        catalogMap = new HashMap<>();
    }

    @Override
    public void placeOccupant(Catalog occupant, int x, int y) {
        Point placeToPutCatalog = new Point(x, y);
        if(catalogMap.get(placeToPutCatalog) != null) {
            catalogMap.get(placeToPutCatalog).transferFrom(occupant);
        } else {
            Catalog newCatalog = new Catalog();
            newCatalog.transferFrom(occupant);
            catalogMap.put(placeToPutCatalog, newCatalog);
        }
    }

    @Override
    public boolean canOccupy(int x, int y) {
        return !catalogMap.containsKey(new Point(x, y));
    }

    @Override
    public Catalog getOccupantAt(int x, int y) {
        return catalogMap.get(new Point(x, y));
    }

    @Override
    public Catalog removeOccupantAt(int x, int y) {
        return catalogMap.remove(new Point(x, y));
    }

    @Override
    public void clearGrid() {
        catalogMap.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Point, Catalog> entry : catalogMap.entrySet()) {
            sb.append("{").append(entry.getKey().toString()).append("}->").append(entry.getValue().toString()).append("\n");
        }

        return sb.toString();
    }
}
