package entity;

import event.Event;
import grid.Grid;

/**
 * A class of enemies that share the "Brilliant" intelligence. Their associated algorithms from OperationAI are the
 * 'smartest' out of all the five intelligence classes.
 */
public class BrilliantAI extends Combatant {
    BrilliantAI(String name, String title, String desc, int health, int momentum, int science, int pse, int sub, int acu, int cha, int itt) {
        super(name, title, desc, health, momentum, science, pse, sub, acu, cha, itt);
    }

    BrilliantAI(BrilliantAI bai) {
        super(bai.getName(), bai.getTitle(), bai.getDesc(),
                bai.getMaxHealth(), bai.getMomentum(), bai.getMaximumScience(),
                bai.getPoise(), bai.getSubtlety(), bai.getAcumen(), bai.getCharisma(), bai.getIntuition());
    }

    public Event[] act(OperationAI opai, int time, Grid gr) {
        return opai.useBrilliant(this, time, gr);
    }
}
