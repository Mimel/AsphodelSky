package event.compound_event;

import event.Event;
import event.Opcode;
import event.SimpleEvent;

import java.util.List;

/**
 * An event, that when executed, performs multiple events.
 */
public abstract class CompoundEvent extends Event {

    CompoundEvent(int time, int priority) {
        super(time, priority);
    }

    SimpleEvent copyInfoToSimpleEvent(Opcode op) {
        SimpleEvent se = new SimpleEvent(getTriggerDelay(), getPriority(), op);
        se.getData().setCasterTo(getCaster())
                .setTargetTo(getTarget())
                .setItemTo(getItem())
                .setSkillTo(getSkill())
                .setCoordTo(getTile())
                .setSecondaryTo(getSecondary());
        return se;
    }

    public abstract CompoundEvent clone();

    public abstract List<SimpleEvent> decompose();
}
