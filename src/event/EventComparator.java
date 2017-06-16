package event;

import java.util.Comparator;

/**
 * A comparator class used in the event queue, to determine the priority in which events exist at.
 * TODO: test.
 */
class EventComparator implements Comparator<Event> {

    @Override
    public int compare(Event e1, Event e2) {
        if(e1.getTime() == e2.getTime()) {
            //Higher arbitrary priorities are higher priorities.
            return e2.getPriority() - e1.getPriority();
        } else {
            //Lower times are higher priority.
            return e1.getTime() - e2.getTime();
        }
    }
}
