package event.flag;

import entity.Combatant;
import event.EventQueue;
import event.Opcode;
import event.SimpleEvent;

public class ProtocolStay extends Flag {

    public ProtocolStay(Opcode trigger) {
        super(trigger);
    }

    @Override
    public Flag copyThis(Combatant newCaster) {
        Flag copy = new ProtocolStay(this.eventTrigger);

        for(SimpleEvent se : this.eventsAddedOnTrigger) {
            SimpleEvent dup = new SimpleEvent(se, newCaster);
            copy.eventsAddedOnTrigger.add(dup);
        }

        copy.eventRedirections.addAll(this.eventRedirections);

        return copy;
    }

    @Override
    public boolean checkForTrigger(EventQueue queue) {
        return false;
    }

    @Override
    public String toString() {
        return fillStringRepresentationTemplate(FlagType.STAY);
    }
}
