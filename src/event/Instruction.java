package event;

import grid.CompositeGrid;
import item.Item;
import item.ItemLoader;
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
    interface TriFunction<A, B, R> {
        R apply(A a, B b);
    }
    /**
     * A map that connects strings to their associated operation.
     */
    private static Map<Opcode, TriFunction<InstructionData, CompositeGrid, ResponseDetails>> instructionSet;

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

            instructionSet.put(Opcode.TILE_SPAWN, (opData, grid) -> {
                grid.addItem(opData.getItem(), opData.getTileX(), opData.getTileY());
                return null;
            });

            instructionSet.put(Opcode.TILE_REMOVE, (opData, grid) -> {
                grid.removeItem(opData.getItem(), opData.getSecondary(), opData.getTileX(), opData.getTileY());
                return null;
            });

            instructionSet.put(Opcode.TILE_REMOVEALL, (opData, grid) -> {
                grid.removeItem(opData.getItem(), opData.getTileX(), opData.getTileY());
                return null;
            });

            instructionSet.put(Opcode.TILE_CLEAR, (opData, grid) -> {
                grid.getTileAt(opData.getTileX(), opData.getTileY()).getCatalog().clearCatalog();
                return null;
            });

            instructionSet.put(Opcode.COMBATANT_ADJUSTHP, (opData, grid) -> {
                if(grid.getOccupant(opData.getTarget()).getHealth() + opData.getSecondary() > 0) {
                    grid.getOccupant(opData.getTarget()).adjustHealthBy(opData.getSecondary());
                    return new ResponseDetails(ResponseCondition.NEGATIVE, grid.getOccupant(opData.getTarget()).toString(), Integer.toString(-opData.getSecondary()));
                } else {
                    grid.killCombatant(opData.getTarget());
                    return null;
                }
            });

            instructionSet.put(Opcode.COMBATANT_ADD_ITEM, (opData, grid) -> {
                grid.getOccupant(opData.getTarget()).getInventory().insertItem(ItemLoader.getItemById(opData.getItem()), opData.getSecondary());
                return null;
            });

            instructionSet.put(Opcode.COMBATANT_REMOVE_ITEM, (opData, grid) -> {
                grid.getOccupant(opData.getTarget()).getInventory().consumeItem(opData.getItem(), opData.getSecondary());
                return null;
            });

            instructionSet.put(Opcode.COMBATANT_REMOVEALL_ITEM, (opData, grid) -> {
                grid.getOccupant(opData.getTarget()).getInventory().consumeAll(opData.getItem());
                return null;
            });

            instructionSet.put(Opcode.COMBATANT_MOVE, (opData, grid) -> {
                grid.moveCombatant(opData.getTarget(), opData.getTileX(), opData.getTileY());
                return null;
            });

            instructionSet.put(Opcode.TRANSFER_ITEM, (opData, grid) -> {
                Pair<Item, Integer> tileItems = grid.getItemsOnTile(opData.getTarget()).consumeItem(opData.getItem(), opData.getSecondary());
                grid.getOccupant(opData.getTarget()).getInventory().insertItem(tileItems.getValue0(), tileItems.getValue1());
                return null;
            });

            instructionSet.put(Opcode.TRANSFER_ITEMALL, (opData, grid) -> {
                grid.getOccupant(opData.getCaster()).getInventory().transferFrom(grid.getItemsOnTile(opData.getTarget()));
                return null;
            });

            instructionSet.put(Opcode.START_DIALOGUE, (opData, grid) -> new ResponseDetails(ResponseCondition.SUCCESS, grid.getOccupant(opData.getTarget()).getName() + "_" + opData.getSecondary() + ".dat"));
        }
    }

    /**
     * Executes the specified instruction.
     * @param opcode The operation to use.
     * @param data The instruction data to execute the instruction with.
     * @param gr The grid to impose the operation on.
     */
    static String execute(Opcode opcode, InstructionData data, CompositeGrid gr) {
        if(instructionSet.containsKey(opcode)) {

            ResponseDetails rd = instructionSet.get(opcode).apply(data, gr);
            if(rd != null) {
                return Response.getResponse(opcode, rd);
            }

        }

        return null;
    }
}
