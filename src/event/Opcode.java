package event;

/**
 * A set of opcodes that are used in the Instruction class.
 */
public enum Opcode {
    NO_OP,

    TILE_SPAWN,
    TILE_REMOVE,
    TILE_REMOVEALL,
    TILE_CLEAR,

    COMBATANT_ADJUSTHP,
    COMBATANT_ADD_ITEM,
    COMBATANT_REMOVE_ITEM,
    COMBATANT_REMOVEALL_ITEM,

    TRANSFER_ITEM,
    TRANSFER_ITEMALL
}
