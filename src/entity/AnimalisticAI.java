package entity;

import event.SimpleEvent;
import grid.CompositeGrid;

import java.util.List;

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
    public List<SimpleEvent> act(OperationAI opai, int time, CompositeGrid gr) {
        return opai.useAnimalistic(this, time, gr);
    }
}
