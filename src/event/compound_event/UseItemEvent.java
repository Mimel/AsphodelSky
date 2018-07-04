package event.compound_event;

import event.CompoundEvent;
import event.Opcode;
import event.SimpleEvent;
import item.Item;
import item.ItemLoader;

import java.util.LinkedList;
import java.util.List;

public class UseItemEvent extends CompoundEvent {
    public UseItemEvent(int time, int priority) {
        super(time, priority);
    }

    @Override
    public CompoundEvent clone() {
        CompoundEvent ce = new UseItemEvent(getTriggerDelay(), getPriority());
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

        SimpleEvent useClause = copyInfoToSimpleEvent(Opcode.COMBATANT_REMOVE_ITEM);
        useClause.getData().setTargetIDTo(getCasterID()).setSecondaryTo(1);
        eventList.add(useClause);
        Item target;
        if((target = ItemLoader.getItemById(getItemID())) != null) {
            List<SimpleEvent> l = target.use(getTargetID());
            for(SimpleEvent se : l) {
                se.setTriggerDelay(se.getTriggerDelay() + getTriggerDelay());
                eventList.add(se);
            }
        }

        return eventList;
    }
}
