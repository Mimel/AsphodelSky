package display;

import display.image.ImageAssets;
import entity.Combatant;
import grid.CompositeGrid;
import grid.Point;
import item.Catalog;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stage {

    private List<Drawable> tiles;

    private Map<Combatant, DrawnCombatant> actors;

    private Map<Catalog, DrawnItem> catalogs;

    private Combatant focusedActor;

    private Camera viewport;

    Stage(CompositeGrid model, ImageAssets ia, Camera viewport) {
        this.tiles = new ArrayList<>();
        this.actors = new HashMap<>();
        this.catalogs = new HashMap<>();
        this.viewport = viewport;

        for(int x = 0; x < model.MAX_BOUNDS.x(); x++) {
            for(int y = 0; y < model.MAX_BOUNDS.y(); y++) {
                Point currentLoc = new Point(x, y);
                if(model.getTileAt(currentLoc).getTerrain() == '.') {
                    tiles.add(new DrawnTile(new Vector3f(x, y, 0.0f), ia.getTerrainTextureID('.')));
                } else {
                    tiles.add(new DrawnCube(new Vector3f(x, y, 0.0f), ia.getTerrainTextureID('#')));
                }

                Combatant c;
                if((c = model.getCombatantAt(currentLoc)) != null) {
                    DrawnCombatant dc = new DrawnCombatant(new Vector3f(x, y, 0.1f), ia.getCombatantTextureID(c.getName()));

                    tiles.add(dc);
                    actors.put(c, dc);
                }

                Catalog cat;
                if((cat = model.getCatalogOnTile(currentLoc)) != null) {
                    DrawnItem di = new DrawnItem(new Vector3f(x, y, 0.05f), ia.getLargeItemTextureID(cat.getFocusedItem().getName()));

                    tiles.add(di);
                    catalogs.put(cat, di);
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

        for(DrawnCombatant combatant : actors.values()) {
            combatant.draw(viewport);
        }

        for(DrawnItem item : catalogs.values()) {
            item.draw(viewport);
        }
    }
}
