package event;

/**
 * Describes an event that can be executed, either by executing a raw instruction (see SimpleEvent)
 * or by returning a set of Events (see CompoundEvent).
 *
 * The A wildcard refers to an enum, which refers to the set of opcodes to use.
 * The B wildcard refers to the subclass that extends this one,
 */
public class Event<A, B extends Event<A, B>> {
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

    /**
     * An operation to perform, from a given set of operations A.
     */
    private final A operation;

    private int casterID;
    private int targetID;
    private int itemID;
    private int skillID;
    private int tileX;
    private int tileY;
    private int secondary;

    Event(int delay, int priority, A op) {
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
        this.operation = op;
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

    public A getOperation() {
        return operation;
    }

    public int getCasterID() {
        return casterID;
    }

    public void setCasterID(int casterID) {
        this.casterID = casterID;
    }

    public int getTargetID() {
        return targetID;
    }

    public void setTargetID(int targetID) {
        this.targetID = targetID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getSkillID() {
        return skillID;
    }

    void setSkillID(int skillID) {
        this.skillID = skillID;
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void setTile(int x, int y) {
        this.tileX = x;
        this.tileY = y;
    }


    public int getSecondary() {
        return secondary;
    }

    void setSecondary(int secondary) {
        this.secondary = secondary;
    }

    public B withCasterID(int casterID) {
        setCasterID(casterID);
        return (B)this;
    }

    public B withTargetID(int targetID) {
        setTargetID(targetID);
        return (B)this;
    }

    public B withItemID(int itemID) {
        setItemID(itemID);
        return (B)this;
    }

    B withSkillID(int skillID) {
        setSkillID(skillID);
        return (B)this;
    }

    public B withTile(int x, int y) {
        setTile(x, y);
        return (B)this;
    }

    public B withSecondary(int secondary) {
        setSecondary(secondary);
        return (B)this;
    }
}
