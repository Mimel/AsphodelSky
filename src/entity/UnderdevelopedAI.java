package entity;

import event.Event;
import grid.Grid;

/**
 * A class of enemies that share the "Underdeveloped" intelligence. Their associated algorithms from OperationAI are more 'smart' than
 * Animalistic enemies and 'dumber' than Sapient enemies.
 */
public class UnderdevelopedAI extends Combatant {
    UnderdevelopedAI(String name, String title, String desc, int health, int momentum, int science, int pse, int sub, int acu, int cha, int itt) {
        super(name, title, desc, health, momentum, science, pse, sub, acu, cha, itt);
    }

    UnderdevelopedAI(UnderdevelopedAI uai) {
        super(uai);
    }

    public Event[] act(OperationAI opai, int time, Grid gr) {
        return opai.useUnderdeveloped(this, time, gr);
    }
}
