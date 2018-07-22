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
    public void placeOccupant(Catalog occupant, Point location) {
        if(catalogMap.get(location) != null) {
            catalogMap.get(location).transferFrom(occupant);
        } else {
            Catalog newCatalog = new Catalog();
            newCatalog.transferFrom(occupant);
            catalogMap.put(location, newCatalog);
        }
    }

    @Override
    public boolean canOccupy(Point location) {
        return !catalogMap.containsKey(location);
    }

    @Override
    public Catalog getOccupantAt(Point location) {
        return catalogMap.get(location);
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
