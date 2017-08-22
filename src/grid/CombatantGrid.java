package grid;

import entity.Combatant;

/**
 * Created by Owner on 8/22/2017.
 */
public class CombatantGrid implements Grid<Combatant> {

    @Override
    public void placeOccupant(Combatant occupant, int x, int y) {

    }

    @Override
    public boolean isOccupied(int x, int y) {
        return false;
    }

    @Override
    public Combatant getOccupantAt(int x, int y) {
        return null;
    }

    @Override
    public Combatant removeOccupantAt(int x, int y) {
        return null;
    }

    @Override
    public void clearGrid() {

    }
}
