package entity;

import event.Opcode;
import event.SimpleEvent;
import grid.CompositeGrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A series of action algorithms imposed on enemies that are determinant on the enemy's intelligence. All such
 * algorithms require the selected enemy, the time to formulate an action, and the CompositeGrid in which to base the action on.
 */
public class Act implements OperationAI {

    /**
     * Determines the action(s) for a Mindless enemy on a given grid, based on the time given.
     * @param mai The enemy which to perform the action on.
     * @param time The amount of time in which an action must take.
     * @param gr The grid that the algorithm bases itself on, and the one that will be affected.
     */
    @Override
    public List<SimpleEvent> useMindless(MindlessAI mai, int time, CompositeGrid gr) {
        throw new UnsupportedOperationException();
    }

    /**
     * Determines the action(s) for an Animalistic enemy on a given grid, based on the time given.
     * @param aai The enemy which to perform the action on.
     * @param time The amount of time in which an action must take.
     * @param gr The grid that the algorithm bases itself on, and the one that will be affected.
     */
    @Override
    public List<SimpleEvent> useAnimalistic(AnimalisticAI aai, int time, CompositeGrid gr) {
        throw new UnsupportedOperationException();
    }

    /**
     * Determines the action(s) for an Underdeveloped enemy on a given grid, based on the time given.
     * @param uai The enemy which to perform the action on.
     * @param time The amount of time in which an action must take.
     * @param gr The grid that the algorithm bases itself on, and the one that will be affected.
     */
    @Override
    public List<SimpleEvent> useUnderdeveloped(UnderdevelopedAI uai, int time, CompositeGrid gr) {
        List<SimpleEvent> actions = new ArrayList<SimpleEvent>();
        actions.add((SimpleEvent) new SimpleEvent(0, 30, Opcode.COMBATANT_ADJUSTHP)
                .withCasterID(2)
                .withTargetID(0)
                .withSecondary(-2));
        return actions;
    }

    /**
     * Determines the action(s) for a Sapient enemy on a given grid, based on the time given.
     * @param sai The enemy which to perform the action on.
     * @param time The amount of time in which an action must take.
     * @param gr The grid that the algorithm bases itself on, and the one that will be affected.
     */
    @Override
    public List<SimpleEvent> useSapient(SapientAI sai, int time, CompositeGrid gr) {
        throw new UnsupportedOperationException();
    }

    /**
     * Determines the action(s) for a Brilliant enemy on a given grid, based on the time given.
     * @param bai The enemy which to perform the action on.
     * @param time The amount of time in which an action must take.
     * @param gr The grid that the algorithm bases itself on, and the one that will be affected.
     */
    @Override
    public List<SimpleEvent> useBrilliant(BrilliantAI bai, int time, CompositeGrid gr) {
        throw new UnsupportedOperationException();
    }
}
