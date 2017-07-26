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

    public Event(Event e, Opcode opcode) {
        super(e.getTriggerDelay(), e.getPriority(), e.getActorId(), e.getAffectedId(), e.getxTile(), e.getyTile());

        this.op = opcode;
    }

    public Opcode getOpcode() {
        return op;
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
        int id = -1;
        int sec = -1;
        int x = -1;
        int y = -1;

        int sParenPos = phrase.indexOf('(');
        int commaPos = phrase.indexOf(',');
        int eParenPos = phrase.indexOf(')');

        name = Opcode.valueOf(phrase.substring(0, sParenPos));
        time = Integer.parseInt(phrase.substring(sParenPos + 1, commaPos));
        priority = Integer.parseInt(phrase.substring(commaPos + 1, eParenPos));

        int counter = eParenPos;
        String flag_name;
        int flag_val;
        int flag_end;
        while((counter = phrase.indexOf('~', counter)) != -1) {
            System.out.println(counter);
            flag_name = phrase.substring(counter + 1, (counter = phrase.indexOf('=', counter)));
            if(phrase.indexOf('~', counter) == -1) {
                flag_val = Integer.parseInt(phrase.substring(counter + 1));
            } else {
                flag_val = Integer.parseInt(phrase.substring(counter + 1, (counter = phrase.indexOf('~', counter))));
            }


            switch(flag_name) {
                case "id":
                    id = flag_val;
                    break;
                case "sec":
                    sec = flag_val;
                    break;
                case "x":
                    x = flag_val;
                    break;
                case "y":
                    y = flag_val;
                    break;
            }
        }

        return new Event(time, priority, name, id, sec, x, y);
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
