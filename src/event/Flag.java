package event;

import java.util.ArrayList;
import java.util.List;

/**
 * A Flag represents a trigger that interacts with an event queue whenever a specific Opcode is called from the queue.
 * Whenever a flag is used, it will remove the calling Opcode and/or inject more predefined operations into the queue.
 */
public class Flag {
    /**
     * The event that will trigger this flag.
     */
    private Opcode eventTrigger;

    /**
     * The action to do when the flag is triggered.
     */
    private FlagType actionOnTrigger;

    /**
     * The list of events to inject in the queue, if any exist.
     */
    private List<SimpleEvent> eventsAddedOnTrigger;

    /**
     * The target of the events in the eventsAddedOnTrigger list.
     * This and the eventsAddedOnTrigger list form an associative array, where
     * index 0 refers to the redirection imparted on the event at index 0
     * in the eventsAddedOnTrigger variable.
     */
    private List<FlagRedirectLocation> eventRedirections;

    public Flag(Opcode trigger, FlagType triggerAction) {
        this.eventTrigger = trigger;
        this.actionOnTrigger = triggerAction;
        eventsAddedOnTrigger = new ArrayList<>();
        eventRedirections = new ArrayList<>();
    }

    /**
     * Copy constructor that takes a flag list and copies everything verbatim,
     * except for the ID of the caster.
     * @param f The flag to copy.
     * @param newCID The new caster ID.
     */
    public Flag(Flag f, int newCID) {
        this.eventTrigger = f.eventTrigger;
        this.actionOnTrigger = f.actionOnTrigger;
        eventsAddedOnTrigger = new ArrayList<>();
        eventRedirections = new ArrayList<>();

        for(SimpleEvent se : f.eventsAddedOnTrigger) {
            SimpleEvent dup = new SimpleEvent(se);
            dup.setCasterID(newCID);
            this.eventsAddedOnTrigger.add(dup);
        }

        this.eventRedirections.addAll(f.eventRedirections);
    }

    public void addEventToFlag(SimpleEvent eventToAdd, FlagRedirectLocation eventDirection) {
        eventsAddedOnTrigger.add(eventToAdd);
        eventRedirections.add(eventDirection);
    }

    /**
     * Checks the event queue to see if this flag gets triggered; if so, proceeds with
     * the specified insertion/deletion processes.
     * @param queue The event queue to use.
     * @return true if the top event was removed; false otherwise.
     */
    boolean checkForTrigger(EventQueue queue) {
        if(queue.peek().getOperation() == eventTrigger) {
            int selfID = queue.peek().getTargetID();
            int senderID = queue.peek().getCasterID();
            switch(actionOnTrigger) {
                case CANCEL:
                    queue.poll();
                    return true;
                case ADD:
                    for(int event = 0; event < eventsAddedOnTrigger.size(); event++) {
                        if(eventRedirections.get(event) == FlagRedirectLocation.SELF) {
                            queue.addEvent(eventsAddedOnTrigger.get(event).withTargetID(selfID));
                        } else if(eventRedirections.get(event) == FlagRedirectLocation.SENDER) {
                            queue.addEvent(eventsAddedOnTrigger.get(event).withTargetID(senderID));
                        }
                    }
                    return false;
                case REPLACE:
                    queue.poll();
                    for(int event = 0; event < eventsAddedOnTrigger.size(); event++) {
                        if(eventRedirections.get(event) == FlagRedirectLocation.SELF) {
                            queue.addEvent(eventsAddedOnTrigger.get(event).withTargetID(selfID));
                        } else if(eventRedirections.get(event) == FlagRedirectLocation.SENDER) {
                            queue.addEvent(eventsAddedOnTrigger.get(event).withTargetID(senderID));
                        }
                    }
                    return true;
            }
        }

        return false;
    }
}
