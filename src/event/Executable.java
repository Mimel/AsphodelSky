package event;

/**
 * Describes an event that can be executed, either by executing a raw instruction (see Event)
 * or by returning a set of Events (see MacroEvent).
 */
public class Executable {
    /**
     * The amount of time between the current time and the time the event triggers.
     */
    private int triggerDelay;

    /**
     * The secondary priority of the event. Higher priorities are executed first when
     * two events are to be fired concurrently.
     */
    private int priority;

    /**
     * The id of the combatant/item that the event focuses on (Usually the one that triggered the event).
     */
    private int actorId;

    /**
     * The id of the combatant/item that the event affects.
     */
    private int affectedId;

    /**
     * The X-coordinate of the tile of the grid that the event affects.
     */
    private int xTile;

    /**
     * The Y-coordinate of the tile of the grid that the event affects.
     */
    private int yTile;

    Executable(int delay, int priority, int actorId, int affectedId, int x, int y) {
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

        this.actorId = actorId;
        this.affectedId = affectedId;
        this.xTile = x;
        this.yTile = y;
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

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public int getAffectedId() {
        return affectedId;
    }

    public void setAffectedId(int affectedId) {
        this.affectedId = affectedId;
    }

    public int getxTile() {
        return xTile;
    }

    public void setxTile(int xTile) {
        this.xTile = xTile;
    }

    public int getyTile() {
        return yTile;
    }

    public void setyTile(int yTile) {
        this.yTile = yTile;
    }
}
