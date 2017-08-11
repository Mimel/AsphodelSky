package event;

import java.util.Comparator;

/**
 * A comparator class used in the event queue, to determine the priority in which events exist at.
 */
class EventComparator implements Comparator<SimpleEvent> {

    /**
     * Compares two events by their times, then their priorities.
     * @param e1 The first event.
     * @param e2 The second event.
     * @return <0 if e1 is higher priority, >0 if e2 is higher priority, 0 if both are of equal priority.
     */
    @Override
    public int compare(SimpleEvent e1, SimpleEvent e2) {
        if(e1.getTriggerDelay() == e2.getTriggerDelay()) {
            //Higher arbitrary priorities are higher priorities.
            return e2.getPriority() - e1.getPriority();
        } else {
            //Lower times are higher priority.
            return e1.getTriggerDelay() - e2.getTriggerDelay();
        }
    }
}
