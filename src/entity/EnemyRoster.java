package entity;

import org.javatuples.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * A group of enemies.
 */
public class EnemyRoster {

    /**
     * Map that maps coordinates to the combatant located at that set of coordinates.
     */
    private Map<Pair<Integer, Integer>, Combatant> roster;

    public EnemyRoster() {
        roster = new HashMap<>();
    }

    public void addCombatant(int x, int y, String enemyName) {
        roster.put(new Pair<>(x, y), EnemyGenerator.getEnemyByName(enemyName));
    }

    public void addCombatant(int x, int y, Combatant c) {
        roster.put(new Pair<>(x, y), c);
    }

    /**
     * Gets the combatant located at the specified coordinates.
     * TODO: Test to make sure the equals method in the Pair class is written correctly.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The combatant if one exists, or null if not.
     */
    public Combatant getCombatant(int x, int y) {
        return roster.get(new Pair<>(x, y));
    }

    public Combatant getCombatant(int id) {
        for(Combatant c : roster.values()) {
            if(id == c.getId()) {
                return c;
            }
        }

        return null;
    }

    public void moveCombatant(int x, int y, int newX, int newY) {
        Combatant c = roster.remove(new Pair<>(x, y));
        roster.put(new Pair<>(newX, newY), c);
    }

    public Combatant removeCombatant(int x, int y) {
        return roster.remove(new Pair<>(x, y));
    }

    public void emptyRoster() {
        roster.clear();
    }
}
