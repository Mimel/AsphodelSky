package event;

import entity.Combatant;
import entity.NullCombatant;
import grid.CompositeGrid;
import grid.Point;

/**
 * An action to be executed, with the time of execution and a secondary priority parameter attached.
 */
public class SimpleEvent extends Event {

    private Opcode operation;

    public SimpleEvent(int time, int priority, Opcode opcode, Combatant caster) {
        super(time, priority, caster);
        this.operation = opcode;
    }

    /**
     * A copy constructor that copies all the attributes (barring the opcode) of an event onto
     * this new one.
     * @param e The event to copy from.
     */
    public SimpleEvent(SimpleEvent e, Combatant newCaster) {
        super(e.getTriggerDelay(), e.getPriority(), newCaster);
        this.operation = e.operation;
        this.setTarget(e.getData().getTarget());
        this.setItem(e.getData().getItem());
        this.setSkill(e.getData().getSkill());
        this.setTile(e.getData().getTile());
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

        SimpleEvent interpretedEvent = new SimpleEvent(time, priority, name, new NullCombatant());
        interpretedEvent.getData().setCoordTo(new Point(x, y)).setSecondaryTo(sec);
        return interpretedEvent;
    }

    /**
     * Executes the instruction in this event.
     * @param gr The grid to impose the instruction on.
     */
    String execute(CompositeGrid gr, InstructionSet operations) {
        return operations.execute(operation, this.getData(), gr);
    }

    public String toString() {
        return String.valueOf(operation) + '(' +
                getTriggerDelay() + ',' +
                getPriority() + ')' +
                " ~x=" + getTile().x() +
                " ~y=" + getTile().y() +
                " ~sec=" + getSecondary();
    }
}
