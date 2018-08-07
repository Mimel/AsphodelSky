package display.game.focus;

import display.GraphicInstructionSet;
import display.Stage;
import entity.Combatant;
import event.SimpleEvent;

import java.util.List;

public class GUIFocus {

    private Stage view;

    private GraphicInstructionSet gis;

    private GUISidebar sidebar;

    public GUIFocus(Combatant player, Stage view, GraphicInstructionSet gis, int windowWidth, int windowHeight) {
        this.view = view;
        this.gis = gis;
        this.sidebar = new GUISidebar(player, windowWidth, windowHeight);
    }

    public void adjustDimensions(int newWidth, int newHeight) {

    }

    public void showSidebar() {
        sidebar.show();
    }

    public void hideSidebar() {
        sidebar.hide();
    }

    public void draw() {
        view.draw();
        sidebar.draw();
    }

    public void interpretSimpleEventsGraphically(List<SimpleEvent> simpleEvents) {
        for(SimpleEvent se : simpleEvents) {
            gis.alterViewByEvent(se, view);
        }
    }
}
