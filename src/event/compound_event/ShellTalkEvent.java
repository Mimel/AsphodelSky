package event.compound_event;

import event.Opcode;
import event.SimpleEvent;

import java.util.LinkedList;
import java.util.List;

public class ShellTalkEvent extends CompoundEvent {
    public ShellTalkEvent(int time, int priority) {
        super(time, priority);
    }

    @Override
    public CompoundEvent clone() {
        CompoundEvent ce = new ShellTalkEvent(getTriggerDelay(), getPriority());
        ce.setCaster(getCaster());
        ce.setTarget(getTarget());
        ce.setItem(getItem());
        ce.setSkillID(getSkill());
        ce.setTile(getTileX(), getTileY());
        ce.setSecondary(getSecondary());
        return ce;
    }

    @Override
    public List<SimpleEvent> decompose() {
        List<SimpleEvent> eventList = new LinkedList<>();
        this.setTriggerDelay(0);
        eventList.add(copyInfoToSimpleEvent(Opcode.START_DIALOGUE));

        return eventList;
    }
}
