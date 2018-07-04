package event;

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

    Event(int delay, int priority) {
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

    public boolean isFlaggable() { return flaggable; }

    public void setFlaggable(boolean newFlaggable) {
        this.flaggable = newFlaggable;
    }

    public int getCasterID() {
        return params.getCasterID();
    }

    public void setCasterID(int casterID) {
        this.getData().setCasterIDTo(casterID);
    }

    public int getTargetID() {
        return params.getTargetID();
    }

    public void setTargetID(int targetID) {
        this.getData().setTargetIDTo(targetID);
    }

    public int getItemID() {
        return params.getItemID();
    }

    public void setItemID(int itemID) {
        this.getData().setItemIDTo(itemID);
    }

    public int getSkillID() {
        return params.getSkillID();
    }

    public void setSkillID(int skillID) {
        this.getData().setSkillIDTo(skillID);
    }

    public int getTileX() {
        return params.getTileX();
    }

    public int getTileY() {
        return params.getTileY();
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
