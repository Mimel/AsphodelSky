package display.game.focus;

import display.Camera;
import display.Drawable;
import display.DrawnCube;
import display.DrawnTile;
import display.image.ImageAssets;
import event.SimpleEvent;
import grid.CompositeGrid;
import grid.Point;
import org.joml.Vector3f;

import java.util.LinkedList;
import java.util.List;

public class GUIFocus {
    private CompositeGrid model;

    private List<Drawable> tiles;

    private Camera viewport;

    private ImageAssets ia;

    public GUIFocus(CompositeGrid model, Camera camera, ImageAssets ia) {
        this.model = model;

        this.tiles = new LinkedList<>();
        this.viewport = camera;

        this.ia = ia;

        interpretModelAsTiles();
    }

    public void draw() {
        for(Drawable asset : tiles) {
            asset.draw(viewport);
        }
    }

    private void interpretModelAsTiles() {
        for(int x = 0; x < model.MAX_BOUNDS.x(); x++) {
            for(int y = 0; y < model.MAX_BOUNDS.y(); y++) {
                if(model.getTileAt(new Point(x, y)).getTerrain() == '.') {
                    tiles.add(new DrawnTile(new Vector3f(x, y, 0.0f), ia.getTerrainTextureID('.')));
                } else {
                    tiles.add(new DrawnCube(new Vector3f(x, y, 0.0f), ia.getTerrainTextureID('#')));
                }
            }
        }
    }

    public void interpretSimpleEventsGraphically(List<SimpleEvent> simpleEvents) {
        System.out.println(simpleEvents);
    }
}
