package event;

import java.util.ArrayList;
import java.util.Collections;
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

    public Flag(Opcode trigger, FlagType triggerAction, SimpleEvent... triggerEvents) {
        this.eventTrigger = trigger;
        this.actionOnTrigger = triggerAction;
        eventsAddedOnTrigger = new ArrayList<>();
        eventRedirections = new ArrayList<>();
        Collections.addAll(eventsAddedOnTrigger, triggerEvents);
    }

    public void addEventToFlag(SimpleEvent eventToAdd, FlagRedirectLocation eventDirection) {
        eventsAddedOnTrigger.add(eventToAdd);
        eventRedirections.add(eventDirection);
    }

    void checkForTrigger(EventQueue queue) {
        if(queue.peek().getOperation() == eventTrigger) {
            int selfID = queue.peek().getTargetID();
            int senderID = queue.peek().getCasterID();
            switch(actionOnTrigger) {
                case CANCEL:
                    queue.poll();
                    break;
                case ADD:
                    for(int event = 0; event < eventsAddedOnTrigger.size(); event++) {
                        if(eventRedirections.get(event) == FlagRedirectLocation.SELF) {
                            queue.addEvent((SimpleEvent) eventsAddedOnTrigger.get(event).withTargetID(selfID));
                        } else if(eventRedirections.get(event) == FlagRedirectLocation.SENDER) {
                            queue.addEvent((SimpleEvent) eventsAddedOnTrigger.get(event).withTargetID(senderID));
                        }
                    }
                    break;
                case REPLACE:
                    queue.poll();
                    for(int event = 0; event < eventsAddedOnTrigger.size(); event++) {
                        if(eventRedirections.get(event) == FlagRedirectLocation.SELF) {
                            queue.addEvent((SimpleEvent) eventsAddedOnTrigger.get(event).withTargetID(selfID));
                        } else if(eventRedirections.get(event) == FlagRedirectLocation.SENDER) {
                            queue.addEvent((SimpleEvent) eventsAddedOnTrigger.get(event).withTargetID(senderID));
                        }
                    }
                    break;
            }
        }
    }
}
