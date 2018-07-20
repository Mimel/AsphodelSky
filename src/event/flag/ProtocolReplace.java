package event.flag;

import event.EventQueue;
import event.FlagType;
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
            dup.setCaster(newCID);
            copy.eventsAddedOnTrigger.add(dup);
        }

        copy.eventRedirections.addAll(this.eventRedirections);

        return copy;
    }

    @Override
    public boolean checkForTrigger(EventQueue queue) {
        if(queue.peek().getSimpleOperation() == eventTrigger) {
            int selfID = queue.peek().getTarget();
            int senderID = queue.peek().getCaster();
            queue.poll();
            for (int event = 0; event < eventsAddedOnTrigger.size(); event++) {
                SimpleEvent triggerEvent = eventsAddedOnTrigger.get(event);

                if (eventRedirections.get(event) == FlagRedirectLocation.SELF) {
                    triggerEvent.getData().setTargetIDTo(selfID);
                } else if (eventRedirections.get(event) == FlagRedirectLocation.SENDER) {
                    triggerEvent.getData().setTargetIDTo(senderID);
                }

                queue.addEvent(triggerEvent);
            }
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return fillStringRepresentationTemplate(FlagType.REPLACE);
    }
}
