package entity;

import grid.Grid;

/**
 * A class of enemies that share the "Mindless" intelligence. Their associated algorithms from OperationAI are the 'dumbest'
 * of the five intelligence classes.
 */
public class MindlessAI extends Combatant {

    MindlessAI(String name, String title, String desc, int health, int momentum, int science, int pse, int sub, int acu, int cha, int itt) {
        super(name, title, desc, health, momentum, science, pse, sub, acu, cha, itt);
    }

    MindlessAI(MindlessAI mai) {
        super(mai.getName(), mai.getTitle(), mai.getDesc(),
                mai.getMaxHealth(), mai.getMomentum(), mai.getMaximumScience(),
                mai.getPoise(), mai.getSubtlety(), mai.getAcumen(), mai.getCharisma(), mai.getIntuition());
    }

    public void act(OperationAI opai, int time, Grid gr) {
        opai.useMindless(this, time, gr);
    }
}
