package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * A group of enemies.
 */
public class EnemyRoster {

    /**
     * The list of all enemies in this instance.
     */
    private List<Combatant> roster;

    public EnemyRoster() {
        roster = new ArrayList<Combatant>();
    }

    public void addCombatant(String enemyName) {
        throw new UnsupportedOperationException();
    }

    public void addCombatant(Combatant c) {
        throw new UnsupportedOperationException();
    }

    public void addRandomCombatant() {
        throw new UnsupportedOperationException();
    }

    public void addRandomCombatant(int powerRating) {
        throw new UnsupportedOperationException();
    }

    public Combatant getCombatant(int id) {
        throw new UnsupportedOperationException();
    }

    public Combatant removeCombatant(int id) {
        throw new UnsupportedOperationException();
    }

    public void emptyRoster() {
        throw new UnsupportedOperationException();
    }
}
