package entity;

import event.SimpleEvent;
import grid.CompositeGrid;

import java.util.List;

/**
 * A class of enemies that share the "Underdeveloped" intelligence. Their associated algorithms from OperationAI are more 'smart' than
 * Animalistic enemies and 'dumber' than Sapient enemies.
 */
public class UnderdevelopedAI extends Combatant {
    public UnderdevelopedAI() {
        super();
    }


    UnderdevelopedAI(UnderdevelopedAI uai) {
        super(uai);
    }

    public List<SimpleEvent> act(OperationAI opai, int time, CompositeGrid gr) {
        return opai.useUnderdeveloped(this, time, gr);
    }

    @Override
    public String toString() {
        return "$Underdeveloped\n" + super.toString();
    }
}
