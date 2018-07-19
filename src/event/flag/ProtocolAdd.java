package event.flag;

import event.EventQueue;
import event.FlagType;
import event.Opcode;
import event.SimpleEvent;

public class ProtocolAdd extends Flag {

    ProtocolAdd(Opcode trigger) {
        super(trigger);
    }

    @Override
    public Flag copyThis(int newCID) {
        Flag copy = new ProtocolAdd(this.eventTrigger);

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
            int selfID = queue.peek().getTargetID();
            int senderID = queue.peek().getCasterID();

            for (int event = 0; event < eventsAddedOnTrigger.size(); event++) {
                SimpleEvent triggerEvent = eventsAddedOnTrigger.get(event);

                if (eventRedirections.get(event) == FlagRedirectLocation.SELF) {
                    triggerEvent.getData().setTargetIDTo(selfID);
                } else if (eventRedirections.get(event) == FlagRedirectLocation.SENDER) {
                    triggerEvent.getData().setTargetIDTo(senderID);
                }

                queue.addEvent(triggerEvent);
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return fillStringRepresentationTemplate(FlagType.ADD);
    }
}
