package event;

import grid.Grid;

/**
 * An action to be executed, with the time of execution and a secondary priority parameter attached.
 */
public class Event extends Executable {

    private Opcode op;

    public Event(int time, int priority, Opcode opcode, int actorId, int affectedId, int x, int y) {
        super(time, priority, actorId, affectedId, x, y);

        this.op = opcode;
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

        return new Event(time, priority, name, -1, -1, -1, -1);
    }

    /**
     * Executes the instruction in this event.
     * @param gr The grid to impose the instruction on.
     */
    void execute(Grid gr) {
        Instruction.execute(op, getActorId(), getAffectedId(), getxTile(), getyTile(), gr);
    }

    public String toString() {
        return "Event " + op + ": User=" + getActorId() + " Affectee=" + getAffectedId() + " x=" + getxTile() + " y=" + getyTile() + ".";
    }
}
