package grid;

import item.Catalog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Owner on 8/22/2017.
 */
public class CatalogGrid implements Grid<Catalog, Map.Entry<Point, Catalog>> {

    private Map<Point, Catalog> catalogMap;

    CatalogGrid() {
        catalogMap = new HashMap<>();
    }

    @Override
    public void placeOccupant(Catalog occupant, int x, int y) {
        catalogMap.put(new Point(x, y), occupant);
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
    public Grid<Catalog, Map.Entry<Point, Catalog>> subGrid(int x, int y, int width, int height) {
        Grid<Catalog, Map.Entry<Point, Catalog>> subGrid = new CatalogGrid();
        for(Point p : catalogMap.keySet()) {
            if(p.x() >= x && p.x() < x + width && p.y() >= y && p.y() < y + height) {
                subGrid.placeOccupant(catalogMap.get(p), p.x(), p.y());
            }
        }
        return subGrid;
    }

    @Override
    public Iterator<Map.Entry<Point, Catalog>> iterator() {
        return catalogMap.entrySet().iterator();
    }
}
