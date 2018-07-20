package event.compound_event;

import event.Event;
import event.Opcode;
import event.SimpleEvent;

import java.util.List;

/**
 * An event, that when executed, performs multiple events.
 */
public abstract class CompoundEvent extends Event {

    public CompoundEvent(int time, int priority) {
        super(time, priority);
    }

    protected SimpleEvent copyInfoToSimpleEvent(Opcode op) {
        SimpleEvent se = new SimpleEvent(getTriggerDelay(), getPriority(), op);
        se.getData().setCasterIDTo(getCaster())
                .setTargetIDTo(getTarget())
                .setItemIDTo(getItem())
                .setSkillIDTo(getSkill())
                .setCoordTo(getTileX(), getTileY())
                .setSecondaryTo(getSecondary());
        return se;
    }

    public abstract CompoundEvent clone();

    public abstract List<SimpleEvent> decompose();
}
