package display.game.focus;

import display.GraphicInstructionSet;
import display.Stage;
import event.SimpleEvent;

import java.util.List;

public class GUIFocus {

    private Stage view;

    private GraphicInstructionSet gis;

    public GUIFocus(Stage view, GraphicInstructionSet gis) {
        this.view = view;
        this.gis = gis;
    }

    public void draw() {
        view.draw();
    }

    public void interpretSimpleEventsGraphically(List<SimpleEvent> simpleEvents) {
        for(SimpleEvent se : simpleEvents) {
            gis.alterViewByEvent(se, view);
        }
    }
}
