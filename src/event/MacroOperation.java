package event;

/**
 * Created by Owner on 7/17/2017.
 */
public enum MacroOperation {
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
}
