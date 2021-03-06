package entity;

import event.SimpleEvent;
import grid.CompositeGrid;

import java.util.List;

/**
 * A class of enemies that share the "Sapient" intelligence. Their associated algorithms from OperationAI are more 'smart' than
 * Underdeveloped enemies and 'dumber' than Brilliant enemies.
 */
public class SapientAI extends Combatant {
    public SapientAI() {
        super();
    }

    SapientAI(SapientAI sai) {
        super(sai);
    }

    public List<SimpleEvent> act(OperationAI opai, int time, CompositeGrid gr) {
        return opai.useSapient(this, time, gr);
    }

    @Override
    public String toString() {
        return "$Sapient\n" + super.toString();
    }
}
