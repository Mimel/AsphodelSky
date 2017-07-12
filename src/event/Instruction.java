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
    interface QuadConsumer<A, B, C, D> {
        public void accept(A a, B b, C c, D d);

        public default QuadConsumer<A, B, C, D> andThen(QuadConsumer<? super A, ? super B, ? super C, ? super D> after) {
            Objects.requireNonNull(after);
            return (a, b, c, d) -> {
                accept(a, b, c, d);
                after.accept(a, b, c, d);
            };
        }
    }

    /**
     * A map that connects strings to their associated operation.
     */
    private static Map<Opcode, QuadConsumer<Integer, Integer, Integer, Grid>> instructionSet;

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
            instructionSet.put(Opcode.ECHOPARAM, (id, x, y, grid) -> System.out.println("Hello! id = " + id + " x = " + x));

            //Adjusts health by SEC for combatant with given ID.
            instructionSet.put(Opcode.ADJUSTHP, (id, x, y, grid) -> grid.searchForOccupant(id).adjustHealthBy(x));

            //Adjusts momentum by SEC for combatant with given ID.
            instructionSet.put(Opcode.ADJUSTMP, (id, x, y, grid) -> grid.searchForOccupant(id).adjustMomentumBy(x));

            //Adjusts science by SEC for combatant with given ID.
            instructionSet.put(Opcode.ADJUSTSCI, (id, x, y, grid) -> grid.searchForOccupant(id).adjustScienceBy(x));
        }
    }

    /**
     * Executes the specified instruction.
     * @param opcode The operation to use.
     * @param id The id that the operation affects.
     * @param x The secondary variable used in the operation, or the x-coordinate of a given tile.
     * @param y The ternary variable used in the operation, or the y-coordinate of a given tile.
     * @param gr The grid to impose the operation on.
     */
    static void execute(Opcode opcode, int id, int x, int y, Grid gr) {
        if(instructionSet.containsKey(opcode)) {
            instructionSet.get(opcode).accept(id, x, y, gr);
        }
    }
}
