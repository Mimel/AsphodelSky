package event;

import entity.Combatant;
import entity.Occupant;
import grid.Grid;

/**
 * An operation that is performed on the grid, which ranges from spawning enemies to damaging the player.
 * Instances cannot be normally instantiated. There exists instead an operation set of actions one can take.
 */
public class Action {

    /*
     * TODO: Put instances here.
     */
    public static final Action DAMAGE_COMBATANT = new Action() {

        public void fire(Grid gr) {

        }
    };

    private int id;

    private int sec;

    /**
     * Private constructor, to prevent instantiation from outside the class.
     */
    private Action() {}

    /**
     * Copy constructor, used to copy existing Action instances.
     * @param a
     */
    private Action(Action a) {
        this.id = a.id;
        this.sec = a.sec;
    }

    /**
     *
     * @param cmd
     * @return
     */
    public static Action interpretCommandAsAction(String cmd) {
        return null;
    }

    public static Action interpretCommandAsAction(String opcode, int id, int sec) {
        return null;
    }

    /**
     * Fires the current action. All instances must override this, or they will do nothing.
     * @param gr The grid to perform the action on.
     */
    public void fire(Grid gr) {
        return;
    }
}
