package entity;

import grid.Grid;

/**
 * A class of enemies that share the "Animalistic" intelligence. Their associated algorithms from OperationAI are more 'smart' than
 * Mindless enemies and 'dumber' than Underdeveloped enemies.
 */
public class AnimalisticAI extends Combatant {

    AnimalisticAI(String name, String title, String desc, int health, int momentum, int science, int pse, int sub, int acu, int cha, int itt) {
        super(name, title, desc, health, momentum, science, pse, sub, acu, cha, itt);
    }

    AnimalisticAI(AnimalisticAI aai) {
        super(aai.getName(), aai.getTitle(), aai.getDesc(),
                aai.getMaxHealth(), aai.getMomentum(), aai.getMaximumScience(),
                aai.getPoise(), aai.getSubtlety(), aai.getAcumen(), aai.getCharisma(), aai.getIntuition());
    }

    @Override
    public void act(OperationAI opai, int time, Grid gr) {
        opai.useAnimalistic(this, time, gr);
    }
}
