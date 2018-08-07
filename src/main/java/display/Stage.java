package display;

import display.image.ImageAssets;
import entity.Combatant;
import grid.CompositeGrid;
import grid.Point;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stage {

    private List<Drawable> tiles;

    private Map<Combatant, DrawnCombatant> actors;

    private Combatant focusedActor;

    private Camera viewport;

    Stage(CompositeGrid model, ImageAssets ia, Camera viewport) {
        this.tiles = new ArrayList<>();
        this.actors = new HashMap<>();
        this.viewport = viewport;

        for(int x = 0; x < model.MAX_BOUNDS.x(); x++) {
            for(int y = 0; y < model.MAX_BOUNDS.y(); y++) {
                if(model.getTileAt(new Point(x, y)).getTerrain() == '.') {
                    tiles.add(new DrawnTile(new Vector3f(x, y, 0.0f), ia.getTerrainTextureID('.')));
                } else {
                    tiles.add(new DrawnCube(new Vector3f(x, y, 0.0f), ia.getTerrainTextureID('#')));
                }

                if(model.getCombatantAt(new Point(x, y)) != null) {
                    DrawnCombatant dc = new DrawnCombatant(new Vector3f(x, y, 0.0f), ia.getCombatantTextureID("Khweiri Dervish"));

                    tiles.add(dc);
                    actors.put(model.getCombatantAt(new Point(x, y)), dc);
                }
            }
        }

        this.focusedActor = model.getPlayer();
    }

    public DrawnCombatant getDrawnCombatant(Combatant c) {
        return actors.get(c);
    }

    public void moveCameraToCombatant(Combatant c) {
        viewport.moveCameraTo(actors.get(c).getPosition());
    }

    public void draw() {
        for(Drawable d : tiles) {
            d.draw(viewport);
        }

        for(Map.Entry<Combatant, DrawnCombatant> e : actors.entrySet()) {
            e.getValue().draw(viewport);
        }
    }
}
