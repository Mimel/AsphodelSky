package event.flag;

import event.EventQueue;
import event.Opcode;
import event.SimpleEvent;

public class ProtocolReplace extends Flag {

    public ProtocolReplace(Opcode trigger) {
        super(trigger);
    }

    @Override
    public Flag copyThis(int newCID) {
        Flag copy = new ProtocolReplace(this.eventTrigger);

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
        if(queue.peek().getOperation() == eventTrigger) {
            int selfID = queue.peek().getTargetID();
            int senderID = queue.peek().getCasterID();
            queue.poll();
            for (int event = 0; event < eventsAddedOnTrigger.size(); event++) {
                if (eventRedirections.get(event) == FlagRedirectLocation.SELF) {
                    queue.addEvent(eventsAddedOnTrigger.get(event).withTargetID(selfID));
                } else if (eventRedirections.get(event) == FlagRedirectLocation.SENDER) {
                    queue.addEvent(eventsAddedOnTrigger.get(event).withTargetID(senderID));
                }
            }
            return true;
        }

        return false;
    }
}
