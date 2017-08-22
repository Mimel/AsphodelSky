package event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Flag represents a trigger that interacts with an event queue whenever a specific Opcode is called from the queue.
 * Whenever a glag is used, it will remove the calling Opcode and/or inject more predefined operations into the queue.
 */
public class Flag {
    private Opcode eventTrigger;

    private FlagType actionOnTrigger;

    private List<SimpleEvent> eventsAddedOnTrigger;

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
