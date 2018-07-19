package grid;

import entity.Combatant;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents the combatant overlay. Consists of two maps; one that relates the
 * id of a combatant to the coordinates it's on, and a map that relates
 * a set of grid coordinates to the Combatant that exists there.
 */
public class CombatantGrid implements IdSearchableGrid<Combatant> {
    /**
     * Map that maps coordinates to the combatant located at that set of coordinates.
     */
    private final Map<Point, Combatant> coordToOccupant;

    /**
     * Map that maps the id of the combatant to the location of that combatant.
     */
    private final Map<Integer, Point> idToCoord;

    CombatantGrid() {
        coordToOccupant = new LinkedHashMap<>();
        idToCoord = new LinkedHashMap<>();
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
    public Combatant getClosestOccupant(int id, Direction horiz, Direction vert) {
        Point base = idToCoord.get(id);

        Point shortest = base;
        double shortestDistance = Double.MAX_VALUE;

        for(Point coord : idToCoord.values()) {
            if(horiz == base.horizontalDirectionToward(coord) && vert == base.verticalDirectionToward(coord)) {
                if(base.distanceTo(coord) < shortestDistance) {
                    shortest = coord;
                    shortestDistance = base.distanceTo(coord);
                }
            }
        }

        if(horiz == Direction.CENTER && shortestDistance == Double.MAX_VALUE) {
            for(Point coord : idToCoord.values()) {
                if(vert == base.verticalDirectionToward(coord)) {
                    if(base.distanceTo(coord) < shortestDistance) {
                        shortest = coord;
                        shortestDistance = base.distanceTo(coord);
                    }
                }
            }
        } else if(vert == Direction.CENTER && shortestDistance == Double.MAX_VALUE) {
            for(Point coord : idToCoord.values()) {
                if(horiz == base.horizontalDirectionToward(coord)) {
                    if(base.distanceTo(coord) < shortestDistance) {
                        shortest = coord;
                        shortestDistance = base.distanceTo(coord);
                    }
                }
            }
        }

        return coordToOccupant.get(shortest);
    }

    @Override
    public Combatant[] getAllOccupants() {
        return coordToOccupant.values().toArray(new Combatant[0]);
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Integer, Point> combatantLoc : idToCoord.entrySet()) {
            sb.append('{').append(combatantLoc.getKey()).append("}->{").append(combatantLoc.getValue().toString()).append("}->\n");
            sb.append(coordToOccupant.get(combatantLoc.getValue()).toString());
        }
        return sb.toString();
    }
}
