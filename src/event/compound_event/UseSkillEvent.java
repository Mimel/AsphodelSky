package event.compound_event;

import entity.Combatant;
import event.SimpleEvent;

import java.util.LinkedList;
import java.util.List;

public class UseSkillEvent extends CompoundEvent {
    public UseSkillEvent(int time, int priority, Combatant caster) {
        super(time, priority, caster);
    }

    @Override
    public CompoundEvent clone() {
        CompoundEvent ce = new UseSkillEvent(getTriggerDelay(), getPriority(), getCaster());
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
        List<SimpleEvent> l = getSkill().useSkill(getCaster(), getTarget());
        for(SimpleEvent se : l) {
            se.setTriggerDelay(se.getTriggerDelay() + getTriggerDelay());
            eventList.add(se);
        }

        return eventList;
    }
}
