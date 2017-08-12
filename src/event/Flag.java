package event;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Owner on 8/11/2017.
 */
public class Flag {
    private Opcode eventTrigger;

    private FlagType actionOnTrigger;

    private SimpleEvent[] eventsAddedOnTrigger;

    public Flag(Opcode trigger, FlagType triggerAction, SimpleEvent... triggerEvents) {
        this.eventTrigger = trigger;
        this.actionOnTrigger = triggerAction;
        eventsAddedOnTrigger = new SimpleEvent[triggerEvents.length];
        System.arraycopy(triggerEvents, 0, eventsAddedOnTrigger, 0, eventsAddedOnTrigger.length);
    }

    public void addEventToFlag(SimpleEvent eventToAdd) {
        throw new NotImplementedException();
    }

    public void clearEvents() {
        throw new NotImplementedException();
    }

    public void checkForTrigger(EventQueue queue) {
        throw new NotImplementedException();
    }
}
