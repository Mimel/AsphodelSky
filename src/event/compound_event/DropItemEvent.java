package event.compound_event;

import event.Opcode;
import event.SimpleEvent;

import java.util.LinkedList;
import java.util.List;

public class DropItemEvent extends CompoundEvent {
    public DropItemEvent(int time, int priority) {
        super(time, priority);
    }

    @Override
    public CompoundEvent clone() {
        CompoundEvent ce = new DropItemEvent(getTriggerDelay(), getPriority());
        ce.setCasterID(getCasterID());
        ce.setTargetID(getTargetID());
        ce.setItemID(getItemID());
        ce.setSkillID(getSkillID());
        ce.setTile(getTileX(), getTileY());
        ce.setSecondary(getSecondary());
        return ce;
    }

    @Override
    public List<SimpleEvent> decompose() {
        List<SimpleEvent> eventList = new LinkedList<>();
        this.setTriggerDelay(0);

        SimpleEvent dropClause1 = copyInfoToSimpleEvent(Opcode.COMBATANT_REMOVE_ITEM);
        SimpleEvent dropClause2 = copyInfoToSimpleEvent(Opcode.TILE_SPAWN);

        dropClause1.getData().setSecondaryTo(1);
        dropClause2.getData().setSecondaryTo(1);

        eventList.add(dropClause1);
        eventList.add(dropClause2);

        return eventList;
    }
}
