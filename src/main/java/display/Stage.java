package display;

import display.image.ImageAssets;
import entity.Combatant;
import grid.CompositeGrid;
import grid.Point;
import item.Catalog;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class Stage {

    private final Map<Point, DrawnGridSpace> tiles;

    private final Map<Combatant, DrawnCombatant> actors;

    private final Map<Catalog, DrawnItem> catalogs;

    private final Combatant player;

    private final Camera viewport;

    Stage(CompositeGrid model, ImageAssets ia, Camera viewport) {
        this.tiles = new HashMap<>();
        this.actors = new HashMap<>();
        this.catalogs = new HashMap<>();
        this.player = model.getPlayer();
        this.viewport = viewport;

        for(int x = 0; x < model.MAX_BOUNDS.x(); x++) {
            for(int y = 0; y < model.MAX_BOUNDS.y(); y++) {
                Point currentLoc = new Point(x, y);
                if(model.getTileAt(currentLoc).getTerrain() == '.') {
                    tiles.put(currentLoc, new DrawnTile(new Vector3f(x, y, 0.0f), ia.getTerrainTextureID('.'), ia.getSmallMiscTextureID('+')));
                } else {
                    tiles.put(currentLoc, new DrawnCube(new Vector3f(x, y, 0.0f), ia.getTerrainTextureID('#')));
                }

                Combatant c;
                if((c = model.getCombatantAt(currentLoc)) != null) {
                    DrawnCombatant dc = new DrawnCombatant(new Vector3f(x, y, 0.2f), ia.getCombatantTextureID(c.getName()));

                    actors.put(c, dc);
                }

                Catalog cat;
                if((cat = model.getCatalogOnTile(currentLoc)) != null) {
                    DrawnItem di = new DrawnItem(new Vector3f(x, y, 0.1f), ia.getLargeItemTextureID(cat.getFocusedItem().getName()));

                    catalogs.put(cat, di);
                }
            }
        }
    }

    public void showOverlaysOnTile(int x, int y) {
        tiles.get(new Point(x, y)).showOverlay();
    }

    public void hideOverlaysOnTile(int x, int y) {
        tiles.get(new Point(x, y)).hideOverlay();
    }

    public DrawnCombatant getDrawnCombatant(Combatant c) {
        return actors.get(c);
    }

    public void moveCameraToPlayer() {
        viewport.moveCameraTo(actors.get(player).getPosition());
    }

    public void moveCameraToPosition(Point p) {
        viewport.moveCameraTo(new Vector3f(p.x(), p.y(), 0.0f));
    }

    public void removeAllEmptyCatalogs() {
        for(Catalog c : catalogs.keySet()) {
            if(c.isEmpty()) {
                catalogs.remove(c);
            }
        }
    }

    public void draw() {
        for(Drawable d : tiles.values()) {
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
