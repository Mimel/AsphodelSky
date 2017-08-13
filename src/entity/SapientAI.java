package entity;

import event.SimpleEvent;
import grid.Grid;

/**
 * A class of enemies that share the "Sapient" intelligence. Their associated algorithms from OperationAI are more 'smart' than
 * Underdeveloped enemies and 'dumber' than Brilliant enemies.
 */
public class SapientAI extends Combatant {
    SapientAI() {
        super();
    }

    SapientAI(SapientAI sai) {
        super(sai);
    }

    public SimpleEvent[] act(OperationAI opai, int time, Grid gr) {
        return opai.useSapient(this, time, gr);
    }
}
