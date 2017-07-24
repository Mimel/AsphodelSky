package event;

import grid.Grid;
import item.Catalog;
import item.Item;
import org.javatuples.Pair;

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

            instructionSet.put(Opcode.TILE_SPAWN, (actorId, affectedId, x, y, grid) -> {
                grid.addItem(actorId, x, y);
            });

            instructionSet.put(Opcode.TILE_REMOVE, (actorId, affectedId, x, y, grid) -> {
                grid.removeItem(actorId, affectedId, x, y);
            });

            instructionSet.put(Opcode.TILE_REMOVEALL, (actorId, affectedId, x, y, grid) -> {
                grid.removeItem(actorId, x, y);
            });

            instructionSet.put(Opcode.TILE_CLEAR, (actorId, affectedId, x, y, grid) -> {
                grid.getTileAt(x, y).getCatalog().clearCatalog();
            });

            instructionSet.put(Opcode.COMBATANT_ADJUSTHP, (actorId, affectedId, x, y, grid) -> {
                grid.searchForOccupant(actorId).adjustHealthBy(affectedId);
            });

            instructionSet.put(Opcode.COMBATANT_ADD_ITEM, (actorId, affectedId, x, y, grid) -> {
                grid.searchForOccupant(actorId).getInventory().insertItem(Item.getItemById(affectedId), x);
            });

            instructionSet.put(Opcode.COMBATANT_REMOVE_ITEM, (actorId, affectedId, x, y, grid) -> {
                grid.searchForOccupant(actorId).getInventory().consumeItem(affectedId, x);
            });

            instructionSet.put(Opcode.COMBATANT_REMOVEALL_ITEM, (actorId, affectedId, x, y, grid) -> {
                grid.searchForOccupant(actorId).getInventory().consumeAll(affectedId);
            });

            instructionSet.put(Opcode.TRANSFER_ITEM, (actorId, affectedId, x, y, grid) -> {
                Pair<Item, Integer> tileItems = grid.getItemsOnTile(actorId).consumeItem(affectedId, x);
                grid.searchForOccupant(actorId).getInventory().insertItem(tileItems.getValue0(), tileItems.getValue1());
            });

            instructionSet.put(Opcode.TRANSFER_ITEMALL, (actorId, affectedId, x, y, grid) -> {
                Pair<Item, Integer> tileItems = grid.getItemsOnTile(actorId).consumeAll(affectedId);
                grid.searchForOccupant(actorId).getInventory().insertItem(tileItems.getValue0(), tileItems.getValue1());
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
