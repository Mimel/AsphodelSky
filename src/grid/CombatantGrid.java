package grid;

import entity.Combatant;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Owner on 8/22/2017.
 */
public class CombatantGrid implements IdSearchableGrid<Combatant, Map.Entry<Point, Combatant>> {
    /**
     * Map that maps coordinates to the combatant located at that set of coordinates.
     */
    private Map<Point, Combatant> coordToOccupant;

    /**
     * Map that maps the id of the combatant to the location of that combatant.
     */
    private Map<Integer, Point> idToCoord;

    CombatantGrid() {
        coordToOccupant = new HashMap<>();
        idToCoord = new HashMap<>();
    }

    @Override
    public void placeOccupant(Combatant occupant, int x, int y) {
        coordToOccupant.put(new Point(x, y), occupant);
        idToCoord.put(occupant.getId(), new Point(x, y));
    }

    @Override
    public boolean canOccupy(int x, int y) {
        return !coordToOccupant.containsKey(new Point(x, y));
    }

    @Override
    public Combatant getOccupantAt(int x, int y) {
        return coordToOccupant.get(new Point(x, y));
    }

    @Override
    public Combatant getOccupantById(int id) {
        return coordToOccupant.get(idToCoord.get(id));
    }

    @Override
    public Point getLocationById(int id) {
        return idToCoord.get(id);
    }

    @Override
    public Combatant[] getAllOccupants() {
        return coordToOccupant.values().toArray(new Combatant[coordToOccupant.values().size()]);
    }

    @Override
    public Combatant removeOccupantAt(int x, int y) {
        Combatant c = coordToOccupant.remove(new Point(x, y));
        idToCoord.remove(c.getId());
        return c;
    }

    @Override
    public Combatant removeOccuapantById(int id) {
        Point coords = idToCoord.remove(id);
        return coordToOccupant.remove(coords);
    }

    @Override
    public void clearGrid() {
        coordToOccupant.clear();
        idToCoord.clear();
    }

    @Override
    public Grid<Combatant, Map.Entry<Point, Combatant>> subGrid(int x, int y, int width, int height) {
        Grid<Combatant, Map.Entry<Point, Combatant>> subGrid = new CombatantGrid();
        for(Point p : coordToOccupant.keySet()) {
            if(p.x() >= x && p.x() < x + width && p.y() >= y && p.y() < y + height) {
                subGrid.placeOccupant(coordToOccupant.get(p), p.x(), p.y());
            }
        }
        return subGrid;
    }

    @Override
    public Iterator iterator() {
        return coordToOccupant.entrySet().iterator();
    }
}
