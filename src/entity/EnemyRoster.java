package entity;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * A group of enemies.
 * TODO: Remove; duplicative with CombatantGrid.
 */
public class EnemyRoster {

    /**
     * Map that maps coordinates to the combatant located at that set of coordinates.
     */
    private Map<Pair<Integer, Integer>, Combatant> coordToOccupant;

    /**
     * Map that maps the id of the combatant to the location of that combatant.
     */
    private Map<Integer, Pair<Integer, Integer>> idToCoord;

    public EnemyRoster() {
        coordToOccupant = new HashMap<>();
        idToCoord = new HashMap<>();
    }

    public void addCombatant(int x, int y, String enemyName) {
        addCombatant(x, y, EnemyGenerator.getEnemyByName(enemyName));
    }

    public void addCombatant(int x, int y, Combatant c) {
        coordToOccupant.put(new Pair<>(x, y), c);
        idToCoord.put(c.getId(), new Pair<>(x, y));
    }

    public Pair<Integer, Integer> getCombatantLocation(int id) {
        return idToCoord.get(id);
    }

    /**
     * Gets the combatant located at the specified coordinates.
     * TODO: Test to make sure the equals method in the Pair class is written correctly.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The combatant if one exists, or null if not.
     */
    public Combatant getCombatant(int x, int y) {
        return coordToOccupant.get(new Pair<>(x, y));
    }

    public Combatant getCombatant(int id) {
        for(Combatant c : coordToOccupant.values()) {
            if(id == c.getId()) {
                return c;
            }
        }

        return null;
    }

    public void moveCombatant(int x, int y, int newX, int newY) {
        Combatant c = coordToOccupant.remove(new Pair<>(x, y));
        coordToOccupant.put(new Pair<>(newX, newY), c);
        idToCoord.put(c.getId(), new Pair<>(newX, newY));
    }

    public void moveCombatant(int id, int newX, int newY) {
        Pair<Integer, Integer> prevCoords = idToCoord.get(id);
        moveCombatant(prevCoords.getValue0(), prevCoords.getValue1(), newX, newY);
    }

    public Combatant removeCombatant(int x, int y) {
        Combatant c = coordToOccupant.remove(new Pair<>(x, y));
        idToCoord.remove(c.getId());
        return c;
    }

    public void emptyRoster() {
        coordToOccupant.clear();
        idToCoord.clear();
    }
}
