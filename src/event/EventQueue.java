package event;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * A set of all events to be executed, as well as the contextual time.
 */
public class EventQueue {

    /**
     * The current in-game 'time' that increments in order to execute events by time priority in the event queue.
     */
    private int time;

    /**
     * A priority queue of all the events to fire, sorted by time to fire.
     */
    private Queue<Event> eventQueue;

    public EventQueue() {
        this.time = 0;

        Comparator<Event> c = new EventComparator();
        this.eventQueue = new PriorityQueue<Event>(20, c);
    }

    public int getTime() {
        return time;
    }


}
