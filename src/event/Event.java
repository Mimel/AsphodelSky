package event;

/**
 * Describes an event that can be executed, either by executing a raw instruction (see SimpleEvent)
 * or by returning a set of Events (see CompoundEvent).
 */
public class Event<A> {
    /**
     * The amount of time between the current time and the time the event triggers.
     */
    private int triggerDelay;

    /**
     * The secondary priority of the event. Higher priorities are executed first when
     * two events are to be fired concurrently.
     */
    private int priority;

    private A operation;

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

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public A getOperation() {
        return operation;
    }

    public void setOperation(A newOp) {
        this.operation = newOp;
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

    public void setSkillID(int skillID) {
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

    public void setSecondary(int secondary) {
        this.secondary = secondary;
    }

    public Event withCasterID(int casterID) {
        setCasterID(casterID);
        return this;
    }

    public Event withTargetID(int targetID) {
        setTargetID(targetID);
        return this;
    }

    public Event withItemID(int itemID) {
        setItemID(itemID);
        return this;
    }

    public Event withSkillID(int skillID) {
        setSkillID(skillID);
        return this;
    }

    public Event withTile(int x, int y) {
        setTile(x, y);
        return this;
    }

    public Event withSecondary(int secondary) {
        setSecondary(secondary);
        return this;
    }
}
