package event;

import entity.Combatant;
import entity.Occupant;
import grid.Grid;
import grid.Tile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * An operation that is performed on the grid, which ranges from spawning enemies to damaging the player.
 * Instances cannot be normally instantiated. There exists instead an operation set of actions one can take.
 */
public class Instruction
{
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
    public static Map<String, TriConsumer<Integer, Integer, Grid>> instructionSet;

    private Instruction() {}

    public static void loadInstructionSet() {
        if(instructionSet != null) {
            return;
        } else {
            instructionSet = new HashMap<String, TriConsumer<Integer, Integer, Grid>>();

            //TODO: test function. Ensure that map/lambdas function.
            instructionSet.put("hello", (id, sec, grid) -> {
                System.out.println("Hello! id = " + id + " sec = " + sec);
            });
        }
    }

    public static void execute(String opcode, int id, int sec, Grid gr) {
        if(instructionSet.containsKey(opcode)) {
            instructionSet.get(opcode).accept(id, sec, gr);
        }
    }
}
