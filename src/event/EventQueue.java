package event;

import grid.Grid;

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
        this.eventQueue = new PriorityQueue<>(20, c);
    }

    /**
     * Gets the current time.
     * @return The current time.
     */
    public int getTime() {
        return time;
    }


    /**
     * Adds an event to the queue. If the delay is negative, the event is not added..
     * @param delay Time before the event is executed. Must be greater than or equal to zero.
     * @param priority The priority of the event. Used as a secondary sorting mechanism in the case of ties in time.
     * @param opcode The operation to execute.
     * @param id The id that the operation will affect.
     * @param x A secondary variable that supports the operation.
     * @param y
     */
    public void addEvent(int delay, int priority, Opcode opcode, int id, int x, int y) {
        if(delay >= 0) {
            eventQueue.add(new Event(time + delay, priority, opcode, id, x, y));
        }
    }

    /**
     * Adds an event to the queue.
     * @param e The event to add.
     */
    private void addEvent(Event e) {
        if(e.getTime() >= 0) {
            e.setTime(e.getTime() + time);
            eventQueue.add(e);
        }
    }

    /**
     * Adds a set of events to the queue.
     * @param eSet The set of events to add.
     */
    public void addEvents(Event[] eSet) {
        for(Event ev : eSet) {
            addEvent(ev);
        }
    }

    /**
     * Increments the time by the specified time, executing all events in order that exist before the
     * new time.
     * TODO: Test.
     * @param timeOffset The amount of time to progress.
     * @return The new time.
     */
    public int progressTimeBy(int timeOffset, Grid gr) {
        while(timeOffset > 0) {
            if(eventQueue.isEmpty() || eventQueue.peek().getTime() > time) {
                time++;
                timeOffset--;
            } else {
                eventQueue.remove().execute(gr);
            }
        }

        return time;
    }

    /**
     * Checks if there are events that fire at the current time. If so, fire all such events.
     * @param gr The grid to affect.
     */
    public void progressTimeInstantaneous(Grid gr) {
        while(!eventQueue.isEmpty() && eventQueue.peek().getTime() == time) {
            eventQueue.remove().execute(gr);
        }
    }
}
