package event;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owner on 8/11/2017.
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
        for(SimpleEvent event : triggerEvents) {
            eventsAddedOnTrigger.add(event);
        }
    }

    public void addEventToFlag(SimpleEvent eventToAdd, FlagRedirectLocation eventDirection) {
        eventsAddedOnTrigger.add(eventToAdd);
        eventRedirections.add(eventDirection);
    }

    public void clearEvents() {
        eventsAddedOnTrigger.clear();
    }

    public void checkForTrigger(EventQueue queue) {
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
