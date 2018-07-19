package entity;

import event.SimpleEvent;
import grid.CompositeGrid;

import java.util.List;

/**
 * A class of enemies that share the "Brilliant" intelligence. Their associated algorithms from OperationAI are the
 * 'smartest' out of all the five intelligence classes.
 */
public class BrilliantAI extends Combatant {
    BrilliantAI() {
        super();
    }

    BrilliantAI(BrilliantAI bai) {
        super(bai);
    }

    public List<SimpleEvent> act(OperationAI opai, int time, CompositeGrid gr) {
        return opai.useBrilliant(this, time, gr);
    }

    @Override
    public String toString() {
        return "$Brilliant\n" + super.toString();
    }
}
