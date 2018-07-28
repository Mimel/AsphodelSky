package display;

import display.game.focus.GUIFocus;
import entity.Combatant;
import event.EventQueue;
import event.Opcode;
import event.SimpleEvent;
import grid.CompositeGrid;
import grid.Point;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.lwjgl.glfw.GLFW.*;

public class GameKeyBindings {
    private final SortedMap<Integer, Procedure> keybinds;

    public GameKeyBindings(long windowHandle, GUIFocus view, CompositeGrid model, EventQueue eventQueue) {
        this.keybinds = new TreeMap<>();

        //Move commands.
        keybinds.put(GLFW_KEY_A, () -> {
            eventQueue.addEvent(addMoveEvent(1, 0, model));
            return eventQueue.progressTimeBy(1, model);
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
