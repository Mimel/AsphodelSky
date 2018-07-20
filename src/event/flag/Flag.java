package event.flag;

import entity.Combatant;
import event.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A Flag represents a trigger that interacts with an event queue whenever a specific Opcode is called from the queue.
 * Whenever a flag is used, it will remove the calling Opcode and/or inject more predefined operations into the queue.
 */
public abstract class Flag {
    /**
     * The event that will trigger this flag.
     */
    final Opcode eventTrigger;

    /**
     * The list of events to inject in the queue, if any exist.
     */
    final List<SimpleEvent> eventsAddedOnTrigger;

    /**
     * The target of the events in the eventsAddedOnTrigger list.
     * This and the eventsAddedOnTrigger list form an associative array, where
     * index 0 refers to the redirection imparted on the event at index 0
     * in the eventsAddedOnTrigger variable.
     */
    final List<FlagRedirectLocation> eventRedirections;

    Flag(Opcode trigger) {
        this.eventTrigger = trigger;
        eventsAddedOnTrigger = new ArrayList<>();
        eventRedirections = new ArrayList<>();
    }

    public static Flag determineFlag(Opcode trigger, FlagType redirectProtocol) {
        switch(redirectProtocol) {
            case ADD:
                return new ProtocolAdd(trigger);
            case CANCEL:
                return new ProtocolCancel(trigger);
            case REPLACE:
                return new ProtocolReplace(trigger);
            default:
                return new ProtocolStay(trigger);
        }
    }

    public void addEventToFlag(SimpleEvent eventToAdd, FlagRedirectLocation eventDirection) {
        eventsAddedOnTrigger.add(eventToAdd);
        eventRedirections.add(eventDirection);
    }

    public abstract Flag copyThis(Combatant newCaster);

    /**
     * Checks the event queue to see if this flag gets triggered; if so, proceeds with
     * the specified insertion/deletion processes.
     * @param queue The event queue to use.
     * @return true if the top event was removed; false otherwise.
     */
    public abstract boolean checkForTrigger(EventQueue queue);

    String fillStringRepresentationTemplate(FlagType redirectProtocol) {
        StringBuilder sb = new StringBuilder();

        sb.append('!').append(eventTrigger).append('/').append(redirectProtocol).append('\n');
        for(int i = 0; i < eventsAddedOnTrigger.size(); i++) {
            sb.append('\t').append(eventsAddedOnTrigger.get(i).toString())
                    .append(" @").append(eventRedirections.get(i)).append('\n');
        }

        return sb.toString();
    }
}
