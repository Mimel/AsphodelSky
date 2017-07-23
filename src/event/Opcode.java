package event;

/**
 * A set of opcodes that are used in the Instruction class.
 */
public enum Opcode {
    ECHOPARAM,
    ADJUSTHP,
    ADJUSTMP,
    ADJUSTSCI,

    //TODO new instructions used in MacroEvents.
    PICKUP_ITEM,
    DISCARD_ITEM,
    SPAWN_ITEM
}
