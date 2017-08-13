package event;

import com.sun.org.apache.bcel.internal.generic.SIPUSH;
import dialogue.DialogueParser;
import dialogue.Statement;
import grid.Grid;

import java.util.*;

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
    private Queue<SimpleEvent> eventQueue;

    /**
     * An event that will eventually be added to the queue.
     */
    private CompoundEvent pendingEvent;

    private Statement pendingDialogueTree;

    private boolean dialogueTreePending;

    public EventQueue() {
        this.time = 0;

        Comparator<SimpleEvent> c = new EventComparator();
        this.eventQueue = new PriorityQueue<>(20, c);

        dialogueTreePending = false;
    }

    /**
     * Gets the current time.
     * @return The current time.
     */
    public int getTime() {
        return time;
    }

    public void createPendingEvent(int priority, CompoundOpcode mo) {
        pendingEvent = new CompoundEvent(0, priority, mo);
    }

    public CompoundEvent getPendingEvent() {
        return pendingEvent;
    }

    public void executePendingEvent() {
        addEvents(pendingEvent.decomposeMacroEvent());
    }

    public Statement getPendingDialogueTree() {
        if(dialogueTreePending) {
            dialogueTreePending = false;
            return pendingDialogueTree;
        } else {
            return null;
        }
    }

    private void setPendingDialogueTree(Statement newTree) {
        this.pendingDialogueTree = newTree;
        dialogueTreePending = true;
    }

    public boolean isDialogueTreePending() {
        return dialogueTreePending;
    }

    /**
     * Adds an event to the queue. If the delay is negative, the event is not added..
     * @param delay Time before the event is executed. Must be greater than or equal to zero.
     * @param priority The priority of the event. Used as a secondary sorting mechanism in the case of ties in time.
     * @param opcode The operation to execute.
     */
    public void addEvent(int delay, int priority, Opcode opcode) {
        if(delay >= 0) {
            eventQueue.add(new SimpleEvent(time + delay, priority, opcode));
        }
    }

    /**
     * Adds an event to the queue.
     * @param e The event to add.
     */
    public void addEvent(SimpleEvent e) {
        if(e.getTriggerDelay() >= 0) {
            e.setTriggerDelay(e.getTriggerDelay() + time);
            eventQueue.add(e);
        }
    }

    /**
     * Adds a set of events to the queue.
     * @param eSet The set of events to add.
     */
    public void addEvents(List<SimpleEvent> eSet) {
        for(SimpleEvent ev : eSet) {
            addEvent(ev);
        }
    }

    public SimpleEvent peek() {
        return eventQueue.peek();
    }

    public SimpleEvent poll() {
        return eventQueue.poll();
    }

    /**
     * Increments the time by the specified time, executing all events in order that exist before the
     * new time. TODO: update.
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
     * @return The entire set of event messages that occured.
     */
    public List<String> progressTimeInstantaneous(Grid gr) {
        List<String> messageList = new ArrayList<>();
        while(!eventQueue.isEmpty() && eventQueue.peek().getTriggerDelay() == time) {
            Opcode op = eventQueue.peek().getOperation();
            for(Flag f : gr.searchForOccupant(eventQueue.peek().getTargetID()).getFlagList()) {
                f.checkForTrigger(this);
            }

            String message = eventQueue.remove().execute(gr);

            if(message != null) {
                if(op == Opcode.START_DIALOGUE) {
                    setPendingDialogueTree(DialogueParser.loadDialogueTree(message));
                } else {
                    messageList.add(message);
                }
            }
        }

        return messageList;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Time: ").append(time);
        for(SimpleEvent e : eventQueue) {
            sb.append(e.toString()).append(":::At time ").append(e.getTriggerDelay()).append("s \n");
        }
        return sb.toString();
    }
}
