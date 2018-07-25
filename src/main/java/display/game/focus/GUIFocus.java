package display.game.focus;

import display.game.DrawingArea;
import display.image.ImageAssets;
import entity.Combatant;
import grid.CompositeGrid;
import grid.Point;

import java.awt.*;

public class GUIFocus {
    private CompositeGrid model;

    private Point center;

    private DrawingArea bounds;

    private ImageAssets imageAssets;

    public GUIFocus(int x, int y, int w, int h, CompositeGrid model, ImageAssets assets) {
        this.model = model;
        this.bounds = new DrawingArea(x, y, w, h);
        this.center = new Point(bounds.getWidth() / 2 - (ImageAssets.SPRITE_DIMENSION_LG_PX / 2), bounds.getHeight() / 2 - (ImageAssets.SPRITE_DIMENSION_LG_PX / 2));
        this.imageAssets = assets;
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(bounds.getXOffset(), bounds.getYOffset(), bounds.getWidth(), bounds.getHeight());

        Point playerLoc = model.getLocationOfCombatant(Combatant.PLAYER_ID);
        Point topLeftOfGrid = new Point(center.x() - (playerLoc.x() * ImageAssets.SPRITE_DIMENSION_LG_PX), center.y() - (playerLoc.y() * ImageAssets.SPRITE_DIMENSION_LG_PX));
        Point bottomRightOfGrid = new Point(topLeftOfGrid.x() + (model.MAX_BOUNDS.x() * ImageAssets.SPRITE_DIMENSION_LG_PX), topLeftOfGrid.y() + (model.MAX_BOUNDS.y() * ImageAssets.SPRITE_DIMENSION_LG_PX));
        for(int x = topLeftOfGrid.x(); x < bottomRightOfGrid.x(); x += ImageAssets.SPRITE_DIMENSION_LG_PX) {
            for(int y = topLeftOfGrid.y(); y < bottomRightOfGrid.y(); y += ImageAssets.SPRITE_DIMENSION_LG_PX) {
                if(x > -ImageAssets.SPRITE_DIMENSION_LG_PX && y > -ImageAssets.SPRITE_DIMENSION_LG_PX && x < bounds.getWidth() && y < bounds.getHeight()) {
                    Point correctedPoint = new Point((x - topLeftOfGrid.x()) / ImageAssets.SPRITE_DIMENSION_LG_PX, (y - topLeftOfGrid.y()) / ImageAssets.SPRITE_DIMENSION_LG_PX);
                    g.drawImage(imageAssets.getTerrainImage(model.getTileAt(correctedPoint).getTerrain()), x, y, null);

                    Combatant currentCombatant = model.getCombatantAt(correctedPoint);
                    if(currentCombatant != null) {
                        if(currentCombatant.getId() == Combatant.PLAYER_ID) {
                            g.setColor(new Color(200, 100, 30));
                            g.fillRect(x, y, ImageAssets.SPRITE_DIMENSION_LG_PX, ImageAssets.SPRITE_DIMENSION_LG_PX);
                        } else {
                            g.drawImage(imageAssets.getCharImage(currentCombatant.getName()), x, y, null);
                        }
                    }

                    if(model.getCatalogOnTile(correctedPoint) != null && !model.getCatalogOnTile(correctedPoint).isEmpty()) {
                        g.drawImage(imageAssets.getItemImage(model.getCatalogOnTile(correctedPoint).getFocusedItem().getName()), x, y, null);
                    }
                }
            }
        }
    }
}
