package entity;

import event.SimpleEvent;
import grid.Grid;

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

    public SimpleEvent[] act(OperationAI opai, int time, Grid gr) {
        return opai.useBrilliant(this, time, gr);
    }
}
