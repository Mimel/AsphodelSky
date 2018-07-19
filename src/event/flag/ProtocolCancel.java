package event.flag;

import event.EventQueue;
import event.FlagType;
import event.Opcode;
import event.SimpleEvent;

public class ProtocolCancel extends Flag {

    public ProtocolCancel(Opcode trigger) {
        super(trigger);
    }

    @Override
    public Flag copyThis(int newCID) {
        Flag copy = new ProtocolCancel(this.eventTrigger);

        for(SimpleEvent se : this.eventsAddedOnTrigger) {
            SimpleEvent dup = new SimpleEvent(se);
            dup.setCasterID(newCID);
            copy.eventsAddedOnTrigger.add(dup);
        }

        copy.eventRedirections.addAll(this.eventRedirections);

        return copy;
    }

    @Override
    public boolean checkForTrigger(EventQueue queue) {
        if(queue.peek().getSimpleOperation() == eventTrigger) {
            queue.poll();
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return fillStringRepresentationTemplate(FlagType.CANCEL);
    }
}
