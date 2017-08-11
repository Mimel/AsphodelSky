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

    private InstructionData data;

    Event(int delay, int priority, A op, InstructionData data) {
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
        this.data = data;
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

    public InstructionData getData() {
        return data;
    }

    public void reviseData(InstructionData.DataBuilder db) {
        data = db.build();
    }
}
