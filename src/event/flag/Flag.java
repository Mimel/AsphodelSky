package event.flag;

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
    protected Opcode eventTrigger;

    /**
     * The list of events to inject in the queue, if any exist.
     */
    protected List<SimpleEvent> eventsAddedOnTrigger;

    /**
     * The target of the events in the eventsAddedOnTrigger list.
     * This and the eventsAddedOnTrigger list form an associative array, where
     * index 0 refers to the redirection imparted on the event at index 0
     * in the eventsAddedOnTrigger variable.
     */
    protected List<FlagRedirectLocation> eventRedirections;

    public Flag(Opcode trigger) {
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

    public abstract Flag copyThis(int newCID);

    /**
     * Checks the event queue to see if this flag gets triggered; if so, proceeds with
     * the specified insertion/deletion processes.
     * @param queue The event queue to use.
     * @return true if the top event was removed; false otherwise.
     */
    public abstract boolean checkForTrigger(EventQueue queue);
}
