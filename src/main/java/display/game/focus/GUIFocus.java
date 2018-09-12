package display.game.focus;

import display.GraphicInstructionSet;
import display.Stage;
import display.image.ImageAssets;
import entity.Combatant;
import event.SimpleEvent;
import grid.Point;

import java.util.List;

public class GUIFocus {

    private final Stage view;

    private final GraphicInstructionSet gis;

    private final GUISidebar sidebar;

    public GUIFocus(Combatant player, Stage view, GraphicInstructionSet gis, int windowWidth, int windowHeight) {
        this.view = view;
        this.gis = gis;
        this.sidebar = new GUISidebar(player, windowWidth, windowHeight, new ImageAssets());
    }

    public void hideEverything() {
        //hideTileSelector();
        sidebar.hideItemMarker();
        sidebar.hideSkillMarker();
        sidebar.hide();
    }

    public void showSidebar() {
        sidebar.show();
    }

    public void hideSidebar() {
        sidebar.hide();
    }

    public void toggleSidebar() {
        if(sidebar.isHidden()) {
            sidebar.show();
        } else {
            sidebar.hide();
        }
    }

    public void showTileSelector(int x, int y) {
        view.showOverlaysOnTile(x, y);
        view.moveCameraToPosition(new Point(x, y));
    }

    public void hideTileSelector(int x, int y) {
        view.hideOverlaysOnTile(x, y);
        view.moveCameraToPlayer();
    }

    public void showItemSelector() {
        sidebar.showItemMarker();
    }

    public void hideItemSelector() {
        sidebar.hideItemMarker();
    }

    public void showSkillSelector() {
        sidebar.showSkillMarker();
    }

    public void hideSkillSelector() {
        sidebar.hideSkillMarker();
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
