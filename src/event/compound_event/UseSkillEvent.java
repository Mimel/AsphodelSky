package event.compound_event;

import event.CompoundEvent;
import event.SimpleEvent;
import skill.Skill;
import skill.SkillLoader;

import java.util.LinkedList;
import java.util.List;

public class UseSkillEvent extends CompoundEvent {
    public UseSkillEvent(int time, int priority) {
        super(time, priority);
    }

    @Override
    public CompoundEvent clone() {
        CompoundEvent ce = new UseSkillEvent(getTriggerDelay(), getPriority());
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

        Skill usedSkill;
        if((usedSkill = SkillLoader.getSkillByID(getSkillID())) != null) {
            List<SimpleEvent> l = usedSkill.useSkill(getTargetID());
            for(SimpleEvent se : l) {
                se.setTriggerDelay(se.getTriggerDelay() + getTriggerDelay());
                eventList.add(se);
            }
        }

        return eventList;
    }
}
