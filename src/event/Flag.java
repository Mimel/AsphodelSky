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
            switch(actionOnTrigger) {
                case CANCEL:
                    queue.poll();
                    break;
                case ADD:
                    for(SimpleEvent event : eventsAddedOnTrigger) {
                        queue.addEvent(event);
                    }
                    break;
                case REPLACE:
                    queue.poll();
                    for(SimpleEvent event : eventsAddedOnTrigger) {
                        queue.addEvent(event);
                    }
                    break;
            }
        }
    }
}
