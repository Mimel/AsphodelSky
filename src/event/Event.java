package event;

/**
 * An action to be executed, with the time of execution and a secondary priority parameter attached.
 */
public class Event {

    /**
     * When in the event queue, when the time reaches this triggerTime, then the event will fire.
     */
    private int triggerTime;

    /**
     * An arbitrary, secondary priority assigned to an event. If two events have the same trigger time in queue,
     * then the one with the highest priority will be executed first.
     * TODO: Move priority to dedicated trigger class.
     */
    private int priority;

    private Action act;

    public Event(int time, int priority, Action act) {
        this.triggerTime = time;
        this.priority = priority;
        this.act = act;
    }

    public int getTime() {
        return triggerTime;
    }

    public int getPriority() {
        return priority;
    }
}
