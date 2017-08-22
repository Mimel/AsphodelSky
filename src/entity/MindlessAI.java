package entity;

import event.SimpleEvent;
import grid.CompositeGrid;

/**
 * A class of enemies that share the "Mindless" intelligence. Their associated algorithms from OperationAI are the 'dumbest'
 * of the five intelligence classes.
 */
public class MindlessAI extends Combatant {
    MindlessAI() {
        super();
    }

    MindlessAI(MindlessAI mai) {
        super(mai);
    }

    public SimpleEvent[] act(OperationAI opai, int time, CompositeGrid gr) {
        return opai.useMindless(this, time, gr);
    }
}
