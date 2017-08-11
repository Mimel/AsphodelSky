package event;

import grid.Grid;

/**
 * An action to be executed, with the time of execution and a secondary priority parameter attached.
 */
public class SimpleEvent extends Event<Opcode> {

    public SimpleEvent(int time, int priority, Opcode opcode, InstructionData data) {
        super(time, priority, opcode, data);
    }

    public SimpleEvent(SimpleEvent e, Opcode opcode) {
        super(e.getTriggerDelay(), e.getPriority(), opcode, e.getData());
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

        //TODO: Revise.
        SimpleEvent e = new SimpleEvent(time, priority, name, new InstructionData.DataBuilder(0).secondary(sec).build());
        return e;
    }

    /**
     * Executes the instruction in this event.
     * @param gr The grid to impose the instruction on.
     */
    String execute(Grid gr) {
        return Instruction.execute(getOperation(), getData(), gr);
    }

    public String toString() {
        return "SimpleEvent " + getOperation() + " at time " + getTriggerDelay() + "s past the current.";
    }
}
