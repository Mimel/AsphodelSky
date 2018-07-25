package event.compound_event;

import entity.Combatant;
import event.Event;
import event.Opcode;
import event.SimpleEvent;

import java.util.List;

/**
 * An event, that when executed, performs multiple events.
 */
public abstract class CompoundEvent extends Event {

    CompoundEvent(int time, int priority, Combatant caster) {
        super(time, priority, caster);
    }

    SimpleEvent copyInfoToSimpleEvent(Opcode op) {
        SimpleEvent se = new SimpleEvent(getTriggerDelay(), getPriority(), op, getCaster());
        se.getData().setTargetTo(getTarget())
                .setItemTo(getItem())
                .setSkillTo(getSkill())
                .setCoordTo(getTile())
                .setSecondaryTo(getSecondary());
        return se;
    }

    public abstract CompoundEvent clone();

    public abstract List<SimpleEvent> decompose();
}
