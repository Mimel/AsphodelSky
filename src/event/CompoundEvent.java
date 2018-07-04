package event;

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
        se.getData().setCasterIDTo(getCasterID())
                .setTargetIDTo(getTargetID())
                .setItemIDTo(getItemID())
                .setSkillIDTo(getSkillID())
                .setCoordTo(getTileX(), getTileY())
                .setSecondaryTo(getSecondary());
        return se;
    }

    public abstract CompoundEvent clone();

    public abstract List<SimpleEvent> decompose();
}
