package event.compound_event;

import event.SimpleEvent;
import skill.Skill;
import skill.SkillLibrary;

import java.util.LinkedList;
import java.util.List;

public class UseSkillEvent extends CompoundEvent {
    public UseSkillEvent(int time, int priority) {
        super(time, priority);
    }

    @Override
    public CompoundEvent clone() {
        CompoundEvent ce = new UseSkillEvent(getTriggerDelay(), getPriority());
        ce.setCaster(getCaster());
        ce.setTarget(getTarget());
        ce.setItem(getItem());
        ce.setSkill(getSkill());
        ce.setTile(getTile().x(), getTile().y());
        ce.setSecondary(getSecondary());
        return ce;
    }

    @Override
    public List<SimpleEvent> decompose() {
        List<SimpleEvent> eventList = new LinkedList<>();
        this.setTriggerDelay(0);

        Skill usedSkill;
        if((usedSkill = SkillLibrary.getSkillByID(getSkill().getId())) != null) {
            List<SimpleEvent> l = usedSkill.useSkill(getTarget());
            for(SimpleEvent se : l) {
                se.setTriggerDelay(se.getTriggerDelay() + getTriggerDelay());
                eventList.add(se);
            }
        }

        return eventList;
    }
}
