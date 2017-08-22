package entity;

import event.SimpleEvent;
import grid.CompositeGrid;

/**
 * A class of enemies that share the "Underdeveloped" intelligence. Their associated algorithms from OperationAI are more 'smart' than
 * Animalistic enemies and 'dumber' than Sapient enemies.
 */
public class UnderdevelopedAI extends Combatant {
    UnderdevelopedAI() {
        super();
    }


    UnderdevelopedAI(UnderdevelopedAI uai) {
        super(uai);
    }

    public SimpleEvent[] act(OperationAI opai, int time, CompositeGrid gr) {
        return opai.useUnderdeveloped(this, time, gr);
    }
}
