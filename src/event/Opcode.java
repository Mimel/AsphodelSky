package event;

/**
 * A set of opcodes that are used in the Instruction class.
 */
public enum Opcode {
    ECHOPARAM,
    ADJUSTHP,
    ADJUSTMP,
    ADJUSTSCI,

    PICKUP_ITEM,
    DISCARD_ITEM,
    REMOVE_ITEM,
    SPAWN_ITEM
}
