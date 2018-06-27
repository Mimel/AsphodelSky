package event.flag;

import event.EventQueue;
import event.Opcode;
import event.SimpleEvent;

public class ProtocolStay extends Flag {

    public ProtocolStay(Opcode trigger) {
        super(trigger);
    }

    @Override
    public Flag copyThis(int newCID) {
        Flag copy = new ProtocolStay(this.eventTrigger);

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
        return false;
    }
}
