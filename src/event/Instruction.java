package event;

import grid.Grid;

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
            //TODO switch to enummap
            instructionSet = new HashMap<>();

            //Prints the ID and SEC to output.
            instructionSet.put(Opcode.ECHOPARAM, (actorId, affectedId, x, y, grid) -> System.out.println("Hello! id = " + actorId + " x = " + x));

            //Adjusts health by SEC for combatant with given ID.
            instructionSet.put(Opcode.ADJUSTHP, (actorId, affectedId, x, y, grid) -> grid.searchForOccupant(actorId).adjustHealthBy(x));
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
