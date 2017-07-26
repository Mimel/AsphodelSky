package event;

import grid.Grid;

import java.util.Comparator;
import java.util.List;
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

    private MacroEvent pendingEvent;

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

    public void createPendingEvent(int priority, MacroOperation mo) {
        pendingEvent = new MacroEvent(0, priority, mo);
    }

    public MacroEvent getPendingEvent() {
        return pendingEvent;
    }

    public void executePendingEvent() {
        addEvents(pendingEvent.performMacroEvent());
    }

    /**
     * Adds an event to the queue. If the delay is negative, the event is not added..
     * @param delay Time before the event is executed. Must be greater than or equal to zero.
     * @param priority The priority of the event. Used as a secondary sorting mechanism in the case of ties in time.
     * @param opcode The operation to execute.
     * @param x A secondary variable that supports the operation.
     * @param y
     */
    public void addEvent(int delay, int priority, Opcode opcode, int actorId, int affectedId, int x, int y) {
        if(delay >= 0) {
            eventQueue.add(new Event(time + delay, priority, opcode, actorId, affectedId, x, y));
        }
    }

    /**
     * Adds an event to the queue.
     * @param e The event to add.
     */
    public void addEvent(Event e) {
        if(e.getTriggerDelay() >= 0) {
            e.setTriggerDelay(e.getTriggerDelay() + time);
            eventQueue.add(e);
        }
    }

    /**
     * Adds a set of events to the queue.
     * @param eSet The set of events to add.
     */
    public void addEvents(List<Event> eSet) {
        for(Event ev : eSet) {
            addEvent(ev);
        }
    }

    /**
     * Increments the time by the specified time, executing all events in order that exist before the
     * new time.
     * @param timeOffset The amount of time to progress.
     * @return The new time.
     */
    public int progressTimeBy(int timeOffset, Grid gr) {
        while(timeOffset > 0) {
            if(eventQueue.isEmpty() || eventQueue.peek().getTriggerDelay() > time) {
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
        while(!eventQueue.isEmpty() && eventQueue.peek().getTriggerDelay() == time) {
            eventQueue.remove().execute(gr);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Time: " + time);
        for(Event e : eventQueue) {
            sb.append(e.toString() + ":::At time " + e.getTriggerDelay() + "s \n");
        }
        return sb.toString();
    }
}
