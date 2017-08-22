package grid;

import entity.Combatant;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Owner on 8/22/2017.
 */
public class CombatantGrid implements IdSearchableGrid<Combatant> {
    /**
     * Map that maps coordinates to the combatant located at that set of coordinates.
     */
    private Map<Pair<Integer, Integer>, Combatant> coordToOccupant;

    /**
     * Map that maps the id of the combatant to the location of that combatant.
     */
    private Map<Integer, Pair<Integer, Integer>> idToCoord;

    CombatantGrid() {
        coordToOccupant = new HashMap<>();
        idToCoord = new HashMap<>();
    }

    @Override
    public void placeOccupant(Combatant occupant, int x, int y) {
        coordToOccupant.put(new Pair<>(x, y), occupant);
        idToCoord.put(occupant.getId(), new Pair<>(x, y));
    }

    @Override
    public boolean isOccupied(int x, int y) {
        return coordToOccupant.containsKey(new Pair<>(x, y));
    }

    @Override
    public Combatant getOccupantAt(int x, int y) {
        return coordToOccupant.get(new Pair<>(x, y));
    }

    @Override
    public Combatant getOccupantById(int id) {
        return coordToOccupant.get(idToCoord.get(id));
    }

    @Override
    public Combatant removeOccupantAt(int x, int y) {
        Combatant c = coordToOccupant.remove(new Pair<>(x, y));
        idToCoord.remove(c.getId());
        return c;
    }

    @Override
    public Combatant removeOccuapantById(int id) {
        Pair<Integer, Integer> coords = idToCoord.remove(id);
        return coordToOccupant.remove(coords);
    }

    @Override
    public void clearGrid() {
        coordToOccupant.clear();
        idToCoord.clear();
    }
}
