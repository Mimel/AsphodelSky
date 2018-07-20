package event.flag;

import entity.Combatant;
import event.EventQueue;
import event.FlagType;
import event.Opcode;
import event.SimpleEvent;

public class ProtocolReplace extends Flag {

    public ProtocolReplace(Opcode trigger) {
        super(trigger);
    }

    @Override
    public Flag copyThis(Combatant newCaster) {
        Flag copy = new ProtocolReplace(this.eventTrigger);

        for(SimpleEvent se : this.eventsAddedOnTrigger) {
            SimpleEvent dup = new SimpleEvent(se, newCaster);
            copy.eventsAddedOnTrigger.add(dup);
        }

        copy.eventRedirections.addAll(this.eventRedirections);

        return copy;
    }

    @Override
    public boolean checkForTrigger(EventQueue queue) {
        if(queue.peek().getSimpleOperation() == eventTrigger) {
            Combatant self = queue.peek().getTarget();
            Combatant sender = queue.peek().getCaster();
            queue.poll();
            for (int event = 0; event < eventsAddedOnTrigger.size(); event++) {
                SimpleEvent triggerEvent = eventsAddedOnTrigger.get(event);

                if (eventRedirections.get(event) == FlagRedirectLocation.SELF) {
                    triggerEvent.getData().setTargetTo(self);
                } else if (eventRedirections.get(event) == FlagRedirectLocation.SENDER) {
                    triggerEvent.getData().setTargetTo(sender);
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
