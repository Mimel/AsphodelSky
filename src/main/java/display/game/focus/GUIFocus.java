package display.game.focus;

import display.Camera;
import display.DrawnTile;
import grid.CompositeGrid;
import org.joml.Vector3f;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class GUIFocus {
    private CompositeGrid model;

    private List<DrawnTile> tiles;

    private Camera viewport;

    public GUIFocus(CompositeGrid model, Camera camera) {
        this.model = model;

        this.tiles = new LinkedList<>();
        this.viewport = camera;

        interpretModelAsTiles();
    }

    public void draw() {
        for(DrawnTile t : tiles) {
            t.draw(viewport);
        }
    }

    public void paint(Graphics g) {

    }

    private void interpretModelAsTiles() {
        for(float x = -25.0f; x < 25.0f; x++) {
            for(float y = -25.0f; y < 25.0f; y++) {
                tiles.add(new DrawnTile(new Vector3f(x, y, 0.0f)));
            }
        }
    }
}
