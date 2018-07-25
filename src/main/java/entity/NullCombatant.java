package entity;

import event.SimpleEvent;
import grid.CompositeGrid;

import java.util.List;

public class NullCombatant extends Combatant {

    @Override
    public List<SimpleEvent> act(OperationAI opai, int time, CompositeGrid gr) {
        return null;
    }
}
