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
    private Opcode opcode;

    /**
     * The id parameter to use in the instruction.
     * Does NOT refer to the id of the event.
     */
    private int id;

    /**
     * The secondary parameter to use in the instruction.
     */
    private int x;

    /**
     * The ternary parameter to use in the instruction.
     */
    private int y;

    public Event(int time, int priority, Opcode opcode, int id, int x, int y) {
        this.triggerTime = time;

        if(priority >= 0) {
            this.priority = priority;
        }

        this.opcode = opcode;
        this.id = id;
        this.x = x;
        this.y = y;
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

    public int getId() {
        return id;
    }

    public int getX() { return x; }

    public int getY() {
        return y;
    }

    void setTime(int newTime) {
        this.triggerTime = newTime;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    /**
     * Reads a phrase and interprets and creates an event based on the parameters given, with invalid id and sec parameters.
     * All phrases must be in the format "NAME(time,priority)", where the name is one of the
     * listed entries in the Opcode enum.
     * @see Opcode
     * @param phrase The phrase to interpret.
     * @return The event that is interpreted from the given phrase.
     */
    public static Event interpretEvent(String phrase) {
        Opcode name;
        int time;
        int priority;

        int parenPos = phrase.indexOf('(');
        int commaPos = phrase.indexOf(',');

        name = Opcode.valueOf(phrase.substring(0, parenPos));
        time = Integer.parseInt(phrase.substring(parenPos + 1, commaPos));
        priority = Integer.parseInt(phrase.substring(commaPos + 1, phrase.indexOf(')')));

        return new Event(time, priority, name, -1, -1, -1);
    }

    /**
     * Executes the instruction in this event.
     * @param gr The grid to impose the instruction on.
     */
    public void execute(Grid gr) {
        Instruction.execute(opcode, id, x, y, gr);
    }
}
