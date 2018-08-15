package display;

import display.game.focus.GUIFocus;
import display.protocol.ItemSelectProtocol;
import display.protocol.ProtocolHistory;
import entity.Combatant;
import event.EventQueue;
import event.Opcode;
import event.SimpleEvent;
import event.compound_event.CompoundEvent;
import event.compound_event.NoOpEvent;
import grid.CompositeGrid;
import grid.Point;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.lwjgl.glfw.GLFW.*;

public class GameKeyBindings {
    private final SortedMap<Integer, Procedure> keybinds;

    private final ProtocolHistory history;

    public GameKeyBindings(long windowHandle, GUIFocus view, CompositeGrid model, EventQueue eventQueue) {
        this.keybinds = new TreeMap<>();
        this.history = new ProtocolHistory();

        //Move commands.
        keybinds.put(GLFW_KEY_Q, () -> {
            if(history.isEmpty()) {
                eventQueue.addEvent(addMoveEvent(-1, 1, model));
                return eventQueue.progressTimeBy(1, model);
            } else {
                return null;
            }
        });

        keybinds.put(GLFW_KEY_W, () -> {
            if(history.isEmpty()) {
                eventQueue.addEvent(addMoveEvent(0, 1, model));
                return eventQueue.progressTimeBy(1, model);
            } else {
                return null;
            }
        });

        keybinds.put(GLFW_KEY_E, () -> {
            if(history.isEmpty()) {
                eventQueue.addEvent(addMoveEvent(1, 1, model));
                return eventQueue.progressTimeBy(1, model);
            } else {
                return null;
            }
        });

        keybinds.put(GLFW_KEY_A, () -> {
            if(history.isEmpty()) {
                eventQueue.addEvent(addMoveEvent(-1, 0, model));
                return eventQueue.progressTimeBy(1, model);
            } else {
                history.peek().move(eventQueue, -1, 0);
                return null;
            }
        });

        keybinds.put(GLFW_KEY_D, () -> {
            if(history.isEmpty()) {
                eventQueue.addEvent(addMoveEvent(1, 0, model));
                return eventQueue.progressTimeBy(1, model);
            } else {
                history.peek().move(eventQueue, 1, 0);
                return null;
            }
        });

        keybinds.put(GLFW_KEY_Z, () -> {
            if(history.isEmpty()) {
                eventQueue.addEvent(addMoveEvent(-1, -1, model));
                return eventQueue.progressTimeBy(1, model);
            } else {
                return null;
            }
        });

        keybinds.put(GLFW_KEY_X, () -> {
            if(history.isEmpty()) {
                eventQueue.addEvent(addMoveEvent(0, -1, model));
                return eventQueue.progressTimeBy(1, model);
            } else {
                return null;
            }
        });

        keybinds.put(GLFW_KEY_C, () -> {
            if(history.isEmpty()) {
                eventQueue.addEvent(addMoveEvent(1, -1, model));
                return eventQueue.progressTimeBy(1, model);
            } else {
                return null;
            }
        });

        // Gets an item(s) from the ground.
        keybinds.put(GLFW_KEY_G, () -> {
            if(model.getItemsOnTileWithCombatant(model.getPlayer()) != null) {
                SimpleEvent getAllItems = new SimpleEvent(1, 100, Opcode.TRANSFER_ITEMALL, model.getPlayer());
                getAllItems.setTarget(model.getPlayer());
                eventQueue.addEvent(getAllItems);
            }
            return eventQueue.progressTimeBy(1, model);
        });

        keybinds.put(GLFW_KEY_I, () -> {
            view.showSidebar();
            return null;
        });

        keybinds.put(GLFW_KEY_O, () -> {
            view.hideSidebar();
            return null;
        });

        keybinds.put(GLFW_KEY_R, () -> {
            CompoundEvent recon = new NoOpEvent(0, 0, model.getPlayer());
            history.push(new ItemSelectProtocol(model.getPlayer().getInventory(), recon));
            view.showSidebar();
            view.showItemSelector();
            return null;
        });

        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            List<SimpleEvent> history = null;
            if(keybinds.containsKey(key) && action == GLFW_PRESS) {
                history = keybinds.get(key).execute();
            }

            if(history != null) {
                view.interpretSimpleEventsGraphically(history);
            }
        });
    }

    private SimpleEvent addMoveEvent(int xOffset, int yOffset, CompositeGrid model) {
        if(!model.isTileOccupiedRelativeTo(Combatant.PLAYER_ID, xOffset, yOffset)) {
            SimpleEvent moveSelf = new SimpleEvent(1, 100, Opcode.COMBATANT_MOVE, model.getPlayer());
            moveSelf.setTarget(model.getPlayer());
            moveSelf.setTile(new Point(xOffset, yOffset));

            return moveSelf;
        } else {
            return null;
        }
    }
}
