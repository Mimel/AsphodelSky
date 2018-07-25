package event.compound_event;

import entity.Combatant;
import event.SimpleEvent;

import java.util.Collections;
import java.util.List;

public class NoOpEvent extends CompoundEvent {
    public NoOpEvent(int time, int priority, Combatant caster) {
        super(time, priority, caster);
    }

    @Override
    public CompoundEvent clone() {
        CompoundEvent ce = new NoOpEvent(getTriggerDelay(), getPriority(), getCaster());
        ce.setTarget(getTarget());
        ce.setItem(getItem());
        ce.setSkill(getSkill());
        ce.setTile(getTile());
        ce.setSecondary(getSecondary());
        return ce;
    }

    @Override
    public List<SimpleEvent> decompose() {
        return Collections.emptyList();
    }
}
