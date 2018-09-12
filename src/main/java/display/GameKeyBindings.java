package display;

import display.game.focus.GUIFocus;
import display.protocol.*;
import entity.Combatant;
import event.EventQueue;
import event.Opcode;
import event.SimpleEvent;
import event.compound_event.CompoundEvent;
import event.compound_event.NoOpEvent;
import event.compound_event.UseItemEvent;
import event.compound_event.UseSkillEvent;
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
                history.peek().move(eventQueue, view, -1, 1);
                return null;
            }
        });

        keybinds.put(GLFW_KEY_W, () -> {
            if(history.isEmpty()) {
                eventQueue.addEvent(addMoveEvent(0, 1, model));
                return eventQueue.progressTimeBy(1, model);
            } else {
                history.peek().move(eventQueue, view, 0, 1);
                return null;
            }
        });

        keybinds.put(GLFW_KEY_E, () -> {
            if(history.isEmpty()) {
                eventQueue.addEvent(addMoveEvent(1, 1, model));
                return eventQueue.progressTimeBy(1, model);
            } else {
                history.peek().move(eventQueue, view, 1, 1);
                return null;
            }
        });

        keybinds.put(GLFW_KEY_A, () -> {
            if(history.isEmpty()) {
                eventQueue.addEvent(addMoveEvent(-1, 0, model));
                return eventQueue.progressTimeBy(1, model);
            } else {
                history.peek().move(eventQueue, view, -1, 0);
                return null;
            }
        });

        keybinds.put(GLFW_KEY_D, () -> {
            if(history.isEmpty()) {
                eventQueue.addEvent(addMoveEvent(1, 0, model));
                return eventQueue.progressTimeBy(1, model);
            } else {
                history.peek().move(eventQueue, view, 1, 0);
                return null;
            }
        });

        keybinds.put(GLFW_KEY_Z, () -> {
            if(history.isEmpty()) {
                eventQueue.addEvent(addMoveEvent(-1, -1, model));
                return eventQueue.progressTimeBy(1, model);
            } else {
                history.peek().move(eventQueue, view, -1, -1);
                return null;
            }
        });

        keybinds.put(GLFW_KEY_X, () -> {
            if(history.isEmpty()) {
                eventQueue.addEvent(addMoveEvent(0, -1, model));
                return eventQueue.progressTimeBy(1, model);
            } else {
                history.peek().move(eventQueue, view, 0, -1);
                return null;
            }
        });

        keybinds.put(GLFW_KEY_C, () -> {
            if(history.isEmpty()) {
                eventQueue.addEvent(addMoveEvent(1, -1, model));
                return eventQueue.progressTimeBy(1, model);
            } else {
                history.peek().move(eventQueue, view, 1, -1);
                return null;
            }
        });

        keybinds.put(GLFW_KEY_ENTER, () -> {
            InputProtocol completedProtocol;
            if(!history.isEmpty()) {
                completedProtocol = history.pop();
                CompoundEvent ce = completedProtocol.confirm(eventQueue, view);

                if(history.isEmpty()) {
                    eventQueue.createInjection(ce);
                    view.hideEverything();
                    eventQueue.progressTimeBy(1, model);
                }
            }
            return null;
        });

        keybinds.put(GLFW_KEY_BACKSPACE, () -> {
            history.pushPreviousProtocol();
            return null;
        });

        keybinds.put(GLFW_KEY_ESCAPE, () -> {
            while(!history.isEmpty()) {
                history.pop().goBack(eventQueue, view);
            }
            history.clear();

            return null;
        });

        // [G]ets an item(s) from the ground.
        keybinds.put(GLFW_KEY_G, () -> {
            if(model.getItemsOnTileWithCombatant(model.getPlayer()) != null) {
                SimpleEvent getAllItems = new SimpleEvent(1, 100, Opcode.TRANSFER_ITEMALL, model.getPlayer());
                getAllItems.setTarget(model.getPlayer());
                eventQueue.addEvent(getAllItems);
            }
            return eventQueue.progressTimeBy(1, model);
        });

        keybinds.put(GLFW_KEY_LEFT_SHIFT, () -> {
            view.toggleSidebar();
            return null;
        });

        // Allows user to view [I]nventory items.
        keybinds.put(GLFW_KEY_I, () -> {
            CompoundEvent recon = new NoOpEvent(0, 0, model.getPlayer());
            history.push(new ItemSelectProtocol(model.getPlayer().getInventory(), recon));
            view.showSidebar();
            view.showItemSelector();
            return null;
        });

        // [U]ses an item.
        keybinds.put(GLFW_KEY_U, () -> {
            CompoundEvent use = new UseItemEvent(1, 0, model.getPlayer());
            history.push(new ItemSelectProtocol(model.getPlayer().getInventory(), use));
            view.showSidebar();
            view.showItemSelector();

            return null;
        });

        // e[V]okes a skill.
        keybinds.put(GLFW_KEY_V, () -> {
            CompoundEvent evoke = new UseSkillEvent(1, 0, model.getPlayer());
            history.push(new SkillSelectProtocol(model.getPlayer().getSkillSet(), evoke));
            view.showSidebar();
            view.showSkillSelector();

            return null;
        });

        // [R]econs the grid.
        keybinds.put(GLFW_KEY_R, () -> {
            CompoundEvent behold = new NoOpEvent(0, 0, model.getPlayer());
            Point p = model.getLocationOfPlayer();
            history.push(new TileSelectProtocol(p.x(), p.y(), model.getNumberOfColumns(), model.getNumberOfRows(), behold));
            view.showTileSelector(p.x(), p.y());
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
