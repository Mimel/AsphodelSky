package event;

import entity.Combatant;
import grid.Point;
import item.Item;
import skill.Skill;

/**
 * Describes an event that can be executed, either by executing a raw instruction (see SimpleEvent)
 * or by returning a set of Events (see CompoundEvent).
 *
 * The A wildcard refers to an enum, which refers to the set of opcodes to use.
 */
public class Event {
    /**
     * The amount of time between the current time and the time the event triggers.
     */
    private int triggerDelay;

    /**
     * The secondary priority of the event. Higher priorities are executed first when
     * two events are to be fired concurrently.
     */
    private final int priority;

    /**
     * Determines if this event goes through flag checks.
     */
    private boolean flaggable;

    private final InstructionData params;

    public Event(int delay, int priority) {
        if(delay >= 0) {
            this.triggerDelay = delay;
        } else {
            this.triggerDelay = 0;
        }

        if(priority >= 0) {
            this.priority = priority;
        } else {
            this.priority = 0;
        }

        this.flaggable = true;
        this.params = new InstructionData();
    }

    public InstructionData getData() {
        return params;
    }

    public int getTriggerDelay() {
        return triggerDelay;
    }

    public void setTriggerDelay(int triggerDelay) {
        this.triggerDelay = triggerDelay;
    }

    public int getPriority() {
        return priority;
    }

    boolean isFlaggable() { return flaggable; }

    void setFlaggable(boolean newFlaggable) {
        this.flaggable = newFlaggable;
    }

    public Combatant getCaster() {
        return params.getCaster();
    }

    public void setCaster(Combatant caster) {
        this.getData().setCasterTo(caster);
    }

    public Combatant getTarget() {
        return params.getTarget();
    }

    public void setTarget(Combatant target) {
        this.getData().setTargetTo(target);
    }

    public Item getItem() {
        return params.getItem();
    }

    public void setItem(Item item) {
        this.getData().setItemTo(item);
    }

    public Skill getSkill() {
        return params.getSkill();
    }

    public void setSkill(Skill skill) {
        this.getData().setSkillTo(skill);
    }

    public Point getTile() {
        return params.getTile();
    }

    public void setTile(int x, int y) {
        this.getData().setCoordTo(x, y);
    }


    public int getSecondary() {
        return params.getSecondary();
    }

    public void setSecondary(int secondary) {
        this.getData().setSecondaryTo(secondary);
    }
}
