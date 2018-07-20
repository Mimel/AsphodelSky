package event.compound_event;

import entity.Combatant;
import event.Opcode;
import event.SimpleEvent;

import java.util.LinkedList;
import java.util.List;

public class DropItemEvent extends CompoundEvent {
    public DropItemEvent(int time, int priority, Combatant caster) {
        super(time, priority, caster);
    }

    @Override
    public CompoundEvent clone() {
        CompoundEvent ce = new DropItemEvent(getTriggerDelay(), getPriority(), getCaster());
        ce.setTarget(getTarget());
        ce.setItem(getItem());
        ce.setSkill(getSkill());
        ce.setTile(getTile());
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
