package entity;

import event.Event;
import grid.Grid;

/**
 * A class of enemies that share the "Sapient" intelligence. Their associated algorithms from OperationAI are more 'smart' than
 * Underdeveloped enemies and 'dumber' than Brilliant enemies.
 */
public class SapientAI extends Combatant {
    SapientAI(String name, String title, String desc, int health, int momentum, int science, int pse, int sub, int acu, int cha, int itt) {
        super(name, title, desc, health, momentum, science, pse, sub, acu, cha, itt);
    }

    SapientAI(SapientAI sai) {
        super(sai.getName(), sai.getTitle(), sai.getDesc(),
                sai.getMaxHealth(), sai.getMomentum(), sai.getMaximumScience(),
                sai.getPoise(), sai.getSubtlety(), sai.getAcumen(), sai.getCharisma(), sai.getIntuition());
    }

    public Event[] act(OperationAI opai, int time, Grid gr) {
        return opai.useSapient(this, time, gr);
    }
}
