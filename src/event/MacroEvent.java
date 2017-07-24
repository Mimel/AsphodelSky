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
            case GET_ITEM:
                eventList.add(new Event(getTriggerDelay(), getPriority(), Opcode.REMOVE_ITEM, getAffectedId(), getAffectedId(), getxTile(), getyTile()));
                eventList.add(new Event(getTriggerDelay(), getPriority(), Opcode.PICKUP_ITEM, getActorId(), getAffectedId(), getxTile(), getyTile()));
                break;
            case USE_ITEM:
                eventList.add(new Event(getTriggerDelay(), getPriority(), Opcode.DISCARD_ITEM, getActorId(), getAffectedId(), getxTile(), getyTile()));
                eventList.addAll(Item.getItemById(getAffectedId()).use(getActorId()));
                break;
            case DROP_ITEM:
                eventList.add(new Event(getTriggerDelay(), getPriority(), Opcode.DISCARD_ITEM, getActorId(), getAffectedId(), getxTile(), getyTile()));
                eventList.add(new Event(getTriggerDelay(), getPriority(), Opcode.SPAWN_ITEM, getAffectedId(), getAffectedId(), getxTile(), getyTile()));
                break;
        }

        return eventList;
    }
}
