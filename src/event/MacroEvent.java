package event;

import item.Item;

import java.util.LinkedList;
import java.util.List;

/**
 * An event, that when executed, performs multiple events.
 */
public class MacroEvent extends Executable {

    private MacroOperation op;

    public MacroEvent(int time, int priority, MacroOperation mo, InstructionData data) {
        super(time, priority, data);
        this.op = mo;
    }

    /**
     * Reduces this MacroEvent into a set of smaller events based on the Macro opcode and returns those
     * events.
     * @return The list of events that this MacroEvent decomposes to.
     */
    List<Event> decomposeMacroEvent() {
        List<Event> eventList = new LinkedList<>();
        switch(op) {
            case USE_ITEM:
                eventList.add(new Event(getTriggerDelay(), getPriority(), Opcode.COMBATANT_REMOVE_ITEM, new InstructionData.DataBuilder(getData()).secondary(1).build()));
                eventList.addAll(Item.getItemById(getData().getItemID()).use(getData().getCasterID()));
                break;
            case DROP_ITEM:
                eventList.add(new Event(getTriggerDelay(), getPriority(), Opcode.COMBATANT_REMOVE_ITEM, new InstructionData.DataBuilder(getData()).secondary(1).build()));
                eventList.add(new Event(getTriggerDelay(), getPriority(), Opcode.TILE_SPAWN, new InstructionData.DataBuilder(getData()).secondary(1).build()));
                break;
        }

        return eventList;
    }
}
