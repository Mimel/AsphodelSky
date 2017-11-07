package event;

/**
 * The Opcode of an operation that represents a set of events.
 */
public enum CompoundOpcode {
    /**
     * No operation. Does not increment the EventQueue clock on execution.
     */
    NO_OP,

    /**
     * Uses an item.
     * Discards the item from the user's inventory.
     * Performs the item's use() events.
     */
    USE_ITEM,

    /**
     * Drops an item.
     * Discards the item from the user's inventory.
     * Spawns the same item of the same quantity on the space given.
     */
    DROP_ITEM,

    /**
     * Attacks with the default attack.
     * All weapon effects are procced.
     *
     */
    BASIC_ATTACK
}
