package event;

import grid.Grid;
import item.Item;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * An operation that is performed on the grid, which ranges from spawning enemies to damaging the player.
 * Instances cannot be normally instantiated. There exists instead an operation set of actions one can take.
 */
public class Instruction
{
    @FunctionalInterface
    interface QuintFunction<A, B, C, D, E, R> {
        R apply(A a, B b, C c, D d, E e);
    }
    /**
     * A map that connects strings to their associated operation.
     */
    private static Map<Opcode, QuintFunction<Integer, Integer, Integer, Integer, Grid, ResponseDetails>> instructionSet;

    /**
     * Private constructor used to prevent instantiation.
     */
    private Instruction() {}

    /**
     * Loads the current instruction set.
     * All commands are hardcoded.
     */
    public static void loadInstructionSet() {
        if(instructionSet == null) {
            instructionSet = new HashMap<>();

            instructionSet.put(Opcode.TILE_SPAWN, (actorId, affectedId, x, y, grid) -> {
                grid.addItem(actorId, x, y);
                return null;
            });

            instructionSet.put(Opcode.TILE_REMOVE, (actorId, affectedId, x, y, grid) -> {
                grid.removeItem(actorId, affectedId, x, y);
                return null;
            });

            instructionSet.put(Opcode.TILE_REMOVEALL, (actorId, affectedId, x, y, grid) -> {
                grid.removeItem(actorId, x, y);
                return null;
            });

            instructionSet.put(Opcode.TILE_CLEAR, (actorId, affectedId, x, y, grid) -> {
                grid.getTileAt(x, y).getCatalog().clearCatalog();
                return null;
            });

            instructionSet.put(Opcode.COMBATANT_ADJUSTHP, (actorId, affectedId, x, y, grid) -> {
                grid.searchForOccupant(actorId).adjustHealthBy(affectedId);
                if(affectedId < 0) {
                    return new ResponseDetails(ResponseCondition.NEGATIVE, grid.searchForOccupant(actorId).getName(), actorId.toString());
                } else if(affectedId > 0) {
                    return new ResponseDetails(ResponseCondition.POSITIVE, grid.searchForOccupant(actorId).getName(), actorId.toString());
                }
                return null;
            });

            instructionSet.put(Opcode.COMBATANT_ADD_ITEM, (actorId, affectedId, x, y, grid) -> {
                grid.searchForOccupant(actorId).getInventory().insertItem(Item.getItemById(affectedId), x);
                return null;
            });

            instructionSet.put(Opcode.COMBATANT_REMOVE_ITEM, (actorId, affectedId, x, y, grid) -> {
                grid.searchForOccupant(actorId).getInventory().consumeItem(affectedId, x);
                return null;
            });

            instructionSet.put(Opcode.COMBATANT_REMOVEALL_ITEM, (actorId, affectedId, x, y, grid) -> {
                grid.searchForOccupant(actorId).getInventory().consumeAll(affectedId);
                return null;
            });

            instructionSet.put(Opcode.TRANSFER_ITEM, (actorId, affectedId, x, y, grid) -> {
                Pair<Item, Integer> tileItems = grid.getItemsOnTile(actorId).consumeItem(affectedId, x);
                grid.searchForOccupant(actorId).getInventory().insertItem(tileItems.getValue0(), tileItems.getValue1());
                return null;
            });

            instructionSet.put(Opcode.TRANSFER_ITEMALL, (actorId, affectedId, x, y, grid) -> {
                Pair<Item, Integer> tileItems = grid.getItemsOnTile(actorId).consumeAll(affectedId);
                grid.searchForOccupant(actorId).getInventory().insertItem(tileItems.getValue0(), tileItems.getValue1());
                return new ResponseDetails(ResponseCondition.SUCCESS, grid.searchForOccupant(actorId).getName(), tileItems.getValue0().getName());
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
    static String execute(Opcode opcode, int actorId, int affectedId, int x, int y, Grid gr) {
        if(instructionSet.containsKey(opcode)) {

            ResponseDetails rd = instructionSet.get(opcode).apply(actorId, affectedId, x, y, gr);
            if(rd != null) {
                return Response.getResponse(opcode, rd);
            }

        }

        return null;
    }
}
