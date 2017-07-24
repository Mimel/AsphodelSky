package event;

import item.Item;

import java.util.LinkedList;
import java.util.List;

/**
 * An event, that when executed, performs multiple events.
 */
public class MacroEvent extends Executable {

    private MacroOperation op;

    public MacroEvent(int time, int priority, MacroOperation mo) {
        super(time, priority, -1, -1, -1, -1);
        this.op = mo;
    }

    public MacroEvent(int time, int priority, MacroOperation mo, int actorId, int affectedId, int x, int y) {
        super(time, priority, actorId, affectedId, x, y);
        this.op = mo;
    }

    public MacroOperation getOp() {
        return op;
    }

    public void setOp(MacroOperation op) {
        this.op = op;
    }


    public List<Event> performMacroEvent() {
        List<Event> eventList = new LinkedList<>();
        switch(op) {
            case USE_ITEM:
                eventList.add(new Event(getTriggerDelay(), getPriority(), Opcode.COMBATANT_REMOVE_ITEM, getActorId(), getAffectedId(), 1, getyTile()));
                eventList.addAll(Item.getItemById(getAffectedId()).use(getActorId()));
                break;
            case DROP_ITEM:
                eventList.add(new Event(getTriggerDelay(), getPriority(), Opcode.COMBATANT_REMOVE_ITEM, getActorId(), getAffectedId(), 1, getyTile()));
                eventList.add(new Event(getTriggerDelay(), getPriority(), Opcode.TILE_SPAWN, getAffectedId(), getAffectedId(), getxTile(), getyTile()));
                break;
        }

        return eventList;
    }
}
