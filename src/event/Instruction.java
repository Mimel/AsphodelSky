package event;

import grid.Grid;
import item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * An operation that is performed on the grid, which ranges from spawning enemies to damaging the player.
 * Instances cannot be normally instantiated. There exists instead an operation set of actions one can take.
 */
public class Instruction
{
    /**
     * An interface used to allow lambda functions that take three inputs and provide no outputs.
     * @param <A> The first parameter (In the context of the instruction set, the id).
     * @param <B> The second parameter (In the context of the instruction set, the secondary variable (int)).
     * @param <C> The third parameter (In the context of the instruction set, the Grid).
     */
    @FunctionalInterface
    interface QuintConsumer<A, B, C, D, E> {
        public void accept(A a, B b, C c, D d, E e);

        public default QuintConsumer<A, B, C, D, E> andThen(QuintConsumer<? super A, ? super B, ? super C, ? super D, ? super E> after) {
            Objects.requireNonNull(after);
            return (a, b, c, d, e) -> {
                accept(a, b, c, d, e);
                after.accept(a, b, c, d, e);
            };
        }
    }

    /**
     * A map that connects strings to their associated operation.
     */
    private static Map<Opcode, QuintConsumer<Integer, Integer, Integer, Integer, Grid>> instructionSet;

    /**
     * Private constructor used to prevent instantiation.
     */
    private Instruction() {}

    /**
     * Loads the current instruction set.
     * All commands are hardcoded.
     * TODO: Consider using enum instead of strings as the opcodes.
     */
    public static void loadInstructionSet() {
        if(instructionSet == null) {
            instructionSet = new HashMap<>();

            //Prints the parameter values to standard input.
            instructionSet.put(Opcode.ECHOPARAM, (actorId, affectedId, x, y, grid) -> System.out.println("Echoing parameters:\nActor ID = " + actorId + "\nAffected ID = " + affectedId + "\nX = " + x + "\nY = " + y));

            //Adjusts health by AFFECTEDID for combatant with given ACTORID.
            instructionSet.put(Opcode.ADJUSTHP, (actorId, affectedId, x, y, grid) -> grid.searchForOccupant(actorId).adjustHealthBy(x));

            //Adds item with given AFFECTEDID to ACTORID's inventory.
            instructionSet.put(Opcode.PICKUP_ITEM, (actorId, affectedId, x, y, grid) -> {
                grid.searchForOccupant(actorId).getInventory().insertItem(Item.getItemById(affectedId));
            });

            //Discards item with given AFFECTEDID from ACTORID's inventory.
            instructionSet.put(Opcode.DISCARD_ITEM, (actorId, affectedId, x, y, grid) -> {
                grid.searchForOccupant(actorId).getInventory().consumeItem(affectedId);
            });

            //Spawns item with given ACTORID on tile at position (X, Y).
            instructionSet.put(Opcode.SPAWN_ITEM, (actorId, affectedId, x, y, grid) -> {
                grid.addItem(actorId, x, y);
            });
        }
    }

    /**
     * Executes the specified instruction.
     * @param opcode The operation to use.
     * @param actorId The user id.
     * @param affectedId The id of the object of affection.
     * @param x The secondary variable used in the operation, or the x-coordinate of a given tile.
     * @param y The ternary variable used in the operation, or the y-coordinate of a given tile.
     * @param gr The grid to impose the operation on.
     */
    static void execute(Opcode opcode, int actorId, int affectedId, int x, int y, Grid gr) {
        if(instructionSet.containsKey(opcode)) {
            instructionSet.get(opcode).accept(actorId, affectedId, x, y, gr);
        }
    }
}
