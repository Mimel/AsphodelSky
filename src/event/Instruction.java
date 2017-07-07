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
    interface TriConsumer<A, B, C> {
        public void accept(A a, B b, C c);

        public default TriConsumer<A, B, C> andThen(TriConsumer<? super A, ? super B, ? super C> after) {
            Objects.requireNonNull(after);
            return (a, b, c) -> {
                accept(a, b, c);
                after.accept(a, b, c);
            };
        }
    }

    /**
     * A map that connects strings to their associated operation.
     */
    private static Map<Opcode, TriConsumer<Integer, Integer, Grid>> instructionSet;

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
            instructionSet.put(Opcode.ECHOPARAM, (id, sec, grid) -> System.out.println("Hello! id = " + id + " sec = " + sec));

            //Adjusts health by SEC for combatant with given ID.
            instructionSet.put(Opcode.ADJUSTHP, (id, sec, grid) -> grid.searchForOccupant(id).adjustHealthBy(sec));

            //Adjusts momentum by SEC for combatant with given ID.
            instructionSet.put(Opcode.ADJUSTMP, (id, sec, grid) -> grid.searchForOccupant(id).adjustMomentumBy(sec));

            //Adjusts science by SEC for combatant with given ID.
            instructionSet.put(Opcode.ADJUSTSCI, (id, sec, grid) -> grid.searchForOccupant(id).adjustScienceBy(sec));
        }
    }

    /**
     * Executes the specified instruction.
     * @param opcode The operation to use.
     * @param id The id that the operation affects.
     * @param sec The secondary variable used in the operation.
     * @param gr The grid to impose the operation on.
     */
    static void execute(Opcode opcode, int id, int sec, Grid gr) {
        if(instructionSet.containsKey(opcode)) {
            instructionSet.get(opcode).accept(id, sec, gr);
        }
    }
}
