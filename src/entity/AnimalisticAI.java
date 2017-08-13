package entity;

import event.SimpleEvent;
import grid.Grid;

/**
 * A class of enemies that share the "Animalistic" intelligence. Their associated algorithms from OperationAI are more 'smart' than
 * Mindless enemies and 'dumber' than Underdeveloped enemies.
 */
public class AnimalisticAI extends Combatant {

    AnimalisticAI() {
        super();
    }

    AnimalisticAI(AnimalisticAI aai) {
        super(aai);
    }

    @Override
    public SimpleEvent[] act(OperationAI opai, int time, Grid gr) {
        return opai.useAnimalistic(this, time, gr);
    }
}
