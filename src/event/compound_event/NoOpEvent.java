package event.compound_event;

import event.SimpleEvent;

import java.util.Collections;
import java.util.List;

public class NoOpEvent extends CompoundEvent {
    public NoOpEvent(int time, int priority) {
        super(time, priority);
    }

    @Override
    public CompoundEvent clone() {
        CompoundEvent ce = new NoOpEvent(getTriggerDelay(), getPriority());
        ce.setCaster(getCaster());
        ce.setTarget(getTarget());
        ce.setItem(getItem());
        ce.setSkillID(getSkill());
        ce.setTile(getTileX(), getTileY());
        ce.setSecondary(getSecondary());
        return ce;
    }

    @Override
    public List<SimpleEvent> decompose() {
        return Collections.emptyList();
    }
}
