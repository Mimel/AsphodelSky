package event;

import dialogue.DialogueParser;
import dialogue.Statement;
import grid.CompositeGrid;

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
     * A priority queue of all Compound Events, which will be injected into the eventQueue.
     */
    private Queue<CompoundEvent> injectionQueue;

    /**
     * An event that will eventually be added to the queue.
     */
    private CompoundEvent pendingEvent;

    private Statement pendingDialogueTree;

    private boolean dialogueTreePending;

    public EventQueue() {
        this.time = 0;

        Comparator<Event> c = new EventComparator();
        this.eventQueue = new PriorityQueue<>(20, c);
        this.injectionQueue = new PriorityQueue<>(20, c);

        dialogueTreePending = false;
    }

    /**
     * Gets the current time.
     * @return The current time.
     */
    public int getTime() {
        return time;
    }

    public void createInjection(CompoundEvent ce) {
        if(ce.getTriggerDelay() >= 0) {
            CompoundEvent dup = new CompoundEvent(ce);
            dup.setTriggerDelay(time + dup.getTriggerDelay());
            injectionQueue.add(dup);
        }
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
     * Adds an event to the queue.
     * @param e The event to add.
     */
    public void addEvent(SimpleEvent e) {
        if(e.getTriggerDelay() >= 0) {
            SimpleEvent dup = new SimpleEvent(e);
            dup.setTriggerDelay(time + dup.getTriggerDelay());
            eventQueue.add(dup);
        }
    }

    /**
     * Adds a set of events to the queue.
     * @param eSet The set of events to add.
     */
    private void addEvents(List<SimpleEvent> eSet) {
        for(SimpleEvent ev : eSet) {
            addEvent(ev);
        }
    }

    SimpleEvent peek() {
        return eventQueue.peek();
    }

    void poll() {
        eventQueue.poll();
    }

    /**
     * Increments the time by the specified time, executing all events in order that exist before the
     * new time. TODO: test.
     * @param timeOffset The amount of time to progress.
     * @param gr The grid environment.
     * @return The set of messages received from the events.
     */
    public List<String> progressTimeBy(int timeOffset, CompositeGrid gr) {
        List<String> messagesList = new ArrayList<>();
        while(timeOffset >= 0) {
            while(!injectionQueue.isEmpty() && injectionQueue.peek().getTriggerDelay() == time) {
                eventQueue.addAll(injectionQueue.poll().decomposeMacroEvent());
            }

            if(!eventQueue.isEmpty() && eventQueue.peek().getTriggerDelay() == time) {
                messagesList.addAll(progressTimeInstantaneous(gr));
            }

            time++;
            timeOffset--;
        }

        return messagesList;
    }

    /**
     * Checks if there are events that fire at the current time. If so, fire all such events.
     * @param gr The grid to affect.
     * @return The entire set of event messages that occured.
     */
    public List<String> progressTimeInstantaneous(CompositeGrid gr) {
        List<String> messageList = new ArrayList<>();
        while(!eventQueue.isEmpty() && eventQueue.peek().getTriggerDelay() == time) {
            Opcode op = eventQueue.peek().getOperation();
            for(Flag f : gr.getOccupant(eventQueue.peek().getTargetID()).getFlagList()) {
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
