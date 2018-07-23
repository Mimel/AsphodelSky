package event;

import dialogue.DialogueParser;
import dialogue.Statement;
import entity.Act;
import entity.Combatant;
import event.compound_event.CompoundEvent;
import event.flag.Flag;
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
    private final Queue<SimpleEvent> eventQueue;

    /**
     * A priority queue of all Compound Events, which will be injected into the eventQueue.
     */
    private final Queue<CompoundEvent> injectionQueue;

    private Statement pendingDialogueTree;

    private boolean dialogueTreePending;

    private InstructionSet operations;

    public EventQueue(InstructionSet ops) {
        this.time = 0;

        Comparator<Event> c = new EventComparator();
        this.eventQueue = new PriorityQueue<>(20, c);
        this.injectionQueue = new PriorityQueue<>(20, c);

        dialogueTreePending = false;

        this.operations = ops;
    }

    public void createInjection(CompoundEvent ce) {
        if(ce.getTriggerDelay() >= 0) {
            CompoundEvent dup = ce.clone();
            dup.setTriggerDelay(time + dup.getTriggerDelay());
            injectionQueue.add(dup);
        }
    }

    private void setPendingDialogueTree(Statement newTree) {
        this.pendingDialogueTree = newTree;
        dialogueTreePending = true;
    }
    /**
     * Adds an event to the queue.
     * @param e The event to add.
     */
    public void addEvent(SimpleEvent e) {
        if(e.getTriggerDelay() >= 0) {
            SimpleEvent dup = new SimpleEvent(e, e.getCaster());
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

    public SimpleEvent peek() {
        return eventQueue.peek();
    }

    public void poll() {
        eventQueue.poll();
    }

    /**
     * Increments the time by the specified time, executing all events in order that exist before the
     * new time.
     * @param timeOffset The amount of time to progress.
     * @param gr The grid environment.
     * @return The set of messages received from the events.
     */
    public List<String> progressTimeBy(int timeOffset, CompositeGrid gr) {
        List<String> messagesList = new ArrayList<>();

        for(Combatant c : gr.getAllCombatants()) {
            if(c.getId() != 0) {
                addEvents(c.act(new Act(), timeOffset, gr));
            }
        }

        while(timeOffset >= 0) {

            // Inject Composites into the Standard Queue.
            while(!injectionQueue.isEmpty() && injectionQueue.peek().getTriggerDelay() == time) {
               addEvents(injectionQueue.poll().decompose());
            }

            // Execute all events at time.
            if(!eventQueue.isEmpty() && eventQueue.peek().getTriggerDelay() == time) {
                messagesList.addAll(progressTimeInstantaneous(gr));
            }

            // This should be the only location where time is adjusted.
            if(timeOffset != 0) {
                time++;
            }
            timeOffset--;
        }

        messagesList.removeIf(Objects::isNull);
        return messagesList;
    }

    /**
     * Checks if there are events that fire at the current time. If so, fire all such events.
     * @param gr The grid to affect.
     * @return The entire set of event messages that occured.
     */
    private List<String> progressTimeInstantaneous(CompositeGrid gr) {
        List<String> messageList = new ArrayList<>();
        while(!eventQueue.isEmpty() && eventQueue.peek().getTriggerDelay() == time) {
            boolean eventRemoved = false;
            SimpleEvent topEvent = eventQueue.peek();

            Opcode op = topEvent.getSimpleOperation();
            String message = null;
            if(gr.doesCombatantExist(topEvent.getCaster()) && gr.doesCombatantExist(topEvent.getTarget())) {
                if (topEvent.isFlaggable()) {
                    for (Flag f : gr.getCombatant(topEvent.getTarget()).getFlagList()) {
                        if (f.checkForTrigger(this)) {
                            eventRemoved = true;
                        }

                    }
                }

                if (eventRemoved) {
                    continue;
                } else {
                    topEvent.setFlaggable(false);
                }

                message = topEvent.execute(gr, operations);
            }
            eventQueue.remove(topEvent);

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
