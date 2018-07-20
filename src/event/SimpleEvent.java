package event;

import grid.CompositeGrid;

/**
 * An action to be executed, with the time of execution and a secondary priority parameter attached.
 */
public class SimpleEvent extends Event {

    private Opcode operation;

    public SimpleEvent(int time, int priority, Opcode opcode) {
        super(time, priority);
        this.operation = opcode;
    }

    /**
     * A copy constructor that copies all the attributes (barring the opcode) of an event onto
     * this new one.
     * @param e The event to copy from.
     */
    public SimpleEvent(SimpleEvent e) {
        super(e.getTriggerDelay(), e.getPriority());
        this.operation = e.operation;
        this.setCasterID(e.getData().getCasterID());
        this.setTargetID(e.getData().getTargetID());
        this.setItemID(e.getData().getItemID());
        this.setSkillID(e.getData().getSkillID());
        this.setTile(e.getData().getTileX(), e.getData().getTileY());
        this.setSecondary(e.getData().getSecondary());
    }

    public Opcode getSimpleOperation() {
        return operation;
    }

    /**
     * Reads a phrase and interprets and creates an event based on the parameters given, with invalid id and sec parameters.
     * All phrases must be in the format "NAME(time,priority)", where the name is one of the
     * listed entries in the Opcode enum.
     * @see Opcode
     * @param phrase The phrase to interpret.
     * @return The event that is interpreted from the given phrase.
     */
    public static SimpleEvent interpretEvent(String phrase) {
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
        while((counter = phrase.indexOf('~', counter)) != -1) {
            flag_name = phrase.substring(counter + 1, (counter = phrase.indexOf('=', counter)));
            if(phrase.indexOf('~', counter) == -1) {
                flag_val = Integer.parseInt(phrase.substring(counter + 1));
            } else {
                flag_val = Integer.parseInt(phrase.substring(counter + 1, (counter = phrase.indexOf('~', counter))).trim());
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

        SimpleEvent interpretedEvent = new SimpleEvent(time, priority, name);
        interpretedEvent.getData().setTargetIDTo(id).setCoordTo(x, y).setSecondaryTo(sec);
        return interpretedEvent;
    }

    /**
     * Executes the instruction in this event.
     * @param gr The grid to impose the instruction on.
     */
    String execute(CompositeGrid gr) {
        return Instruction.execute(operation, this.getData(), gr);
    }

    public String toString() {
        return String.valueOf(operation) + '(' +
                getTriggerDelay() + ',' +
                getPriority() + ')' +
                " ~id=" + getTargetID() +
                " ~x=" + getTileX() +
                " ~y=" + getTileY() +
                " ~sec=" + getSecondary();
    }
}
