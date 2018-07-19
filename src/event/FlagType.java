package event;

/**
 * Determies the operation a flag does when it fires.
 */
public enum FlagType {
    /**
     * Adds events to the queue.
     */
    ADD,

    /**
     * Negates the event that fired this flag.
     */
    CANCEL,

    /**
     * Acts as both an ADD and CANCEL; it first removes the event
     * that fired this flag, then adds events to the queue.
     */
    REPLACE,

    /**
     * Does nothing.
     */
    STAY
}
