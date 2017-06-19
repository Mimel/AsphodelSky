package event;

import grid.Grid;

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

    /**
     * The instruction to perform.
     */
    private String opcode;

    /**
     * The id parameter to use in the instruction.
     * Does NOT refer to the id of the event.
     */
    private int id;

    /**
     * The secondary parameter to use in the instruction.
     */
    private int sec;

    public Event(int time, int priority, String opcode, int id, int sec) {
        this.triggerTime = time;

        if(priority >= 0) {
            this.priority = priority;
        }

        this.opcode = opcode;
        this.id = id;
        this.sec = sec;
    }

    /**
     * Gets the time in which the event is executed.
     * @return The time.
     */
    public int getTime() {
        return triggerTime;
    }

    /**
     * Gets the priority of the event. In cases where two events execute at the same time, the event with
     * the higher priority is executed first.
     * @return The priority of the event.
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Executes the instruction in this event.
     * @param gr The grid to impose the instruction on.
     */
    public void execute(Grid gr) {
        Instruction.execute(opcode, id, sec, gr);
    }
}
