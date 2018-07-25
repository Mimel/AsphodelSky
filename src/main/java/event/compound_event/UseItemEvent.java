package event.compound_event;

import entity.Combatant;
import event.Opcode;
import event.SimpleEvent;

import java.util.LinkedList;
import java.util.List;

public class UseItemEvent extends CompoundEvent {
    public UseItemEvent(int time, int priority, Combatant caster) {
        super(time, priority, caster);
    }

    @Override
    public CompoundEvent clone() {
        CompoundEvent ce = new UseItemEvent(getTriggerDelay(), getPriority(), getCaster());
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

        SimpleEvent useClause = copyInfoToSimpleEvent(Opcode.COMBATANT_REMOVE_ITEM);
        useClause.setTarget(getCaster());
        useClause.setSecondary(1);
        eventList.add(useClause);

        List<SimpleEvent> l = getItem().use(getCaster(), getTarget());
        for(SimpleEvent se : l) {
            se.setTriggerDelay(se.getTriggerDelay() + getTriggerDelay());
            eventList.add(se);
        }

        return eventList;
    }
}
