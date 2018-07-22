package display.game.focus;

import display.game.DrawingArea;
import display.image.ImageAssets;
import entity.Combatant;
import entity.Player;
import grid.CompositeGrid;

import java.awt.*;

public class GUIFocus {
    private CompositeGrid model;

    private Point trueOrigin;

    private DrawingArea bounds;

    private ImageAssets imageAssets;

    public GUIFocus(int x, int y, int w, int h, CompositeGrid model, ImageAssets assets) {
        this.model = model;
        this.bounds = new DrawingArea(x, y, w, h);
        this.trueOrigin = new Point(0, 0);
        recalculateTrueOrigin();

        this.imageAssets = assets;
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, bounds.getWidth(), bounds.getHeight());

        recalculateTrueOrigin();
        int testx = trueOrigin.x;
        int testy = trueOrigin.y;
        Point focus = new Point(model.getFocus().x(), model.getFocus().y());
        Point start = new Point(focus.x - widthInTiles() / 2, focus.y - heightInTiles() / 2);

        if(start.x < 0) { start.move(0, start.y); testx = 0; }
        else if(start.x > model.getNumberOfColumns() - visibleColumns()) {
            if(model.getNumberOfColumns() <= widthInTiles()) {
                start.move(0, start.y);
                testx = bounds.getWidth() - (widthInTiles() * ImageAssets.SPRITE_DIMENSION_PX);

                if(bounds.getWidth() > model.getNumberOfColumns() * ImageAssets.SPRITE_DIMENSION_PX) {
                    testx = 0;
                }
            } else {
                start.move(model.getNumberOfColumns() - visibleColumns(), start.y);
                testx *= 2;
            }
        }

        if(start.y < 0) { start.move(start.x, 0); testy = 0; }
        else if(start.y > model.getNumberOfRows() - visibleRows()) {
            if(model.getNumberOfRows() <= heightInTiles()) {
                start.move(start.x, 0);
                testy = bounds.getHeight() - (heightInTiles() * ImageAssets.SPRITE_DIMENSION_PX);

                if(bounds.getHeight() > model.getNumberOfRows() * ImageAssets.SPRITE_DIMENSION_PX) {
                    testy = 0;
                }
            } else {
                start.move(start.x, model.getNumberOfRows() - visibleRows());
                testy *= 2;
            }
        }

        int xMargin = 0;
        int yMargin = 0;

        if(widthInTiles() > model.getNumberOfColumns()) {
            xMargin = (bounds.getWidth() - (model.getNumberOfColumns() * ImageAssets.SPRITE_DIMENSION_PX)) / 2;
        }

        if(heightInTiles() > model.getNumberOfRows()) {
            yMargin = (bounds.getHeight() - (model.getNumberOfRows() * ImageAssets.SPRITE_DIMENSION_PX)) / 2;
        }

        int startingX = start.x;

        Combatant currentCombatant;
        for(int y = testy + yMargin; y < bounds.getHeight() && start.y < model.getNumberOfRows(); y += ImageAssets.SPRITE_DIMENSION_PX) {
            for(int x = testx + xMargin; x < bounds.getWidth() && start.x < model.getNumberOfColumns(); x += ImageAssets.SPRITE_DIMENSION_PX) {
                g.drawImage(imageAssets.getTerrainImage(model.getTileAt(new grid.Point(start.x, start.y)).getTerrain()), x, y, null);

                if((currentCombatant = model.getCombatantAt(new grid.Point(start.x, start.y))) != null) {
                    if(currentCombatant.getId() == Player.PLAYER_ID) {
                        g.setColor(new Color(200, 100, 30));
                        g.fillRect(x, y, ImageAssets.SPRITE_DIMENSION_PX, ImageAssets.SPRITE_DIMENSION_PX);
                    } else {
                        g.drawImage(imageAssets.getCharImage(currentCombatant.getName()), x, y, null);
                    }
                }

                if(model.getCatalogOnTile(new grid.Point(start.x, start.y)) != null && !model.getCatalogOnTile(new grid.Point(start.x, start.y)).isEmpty()) {
                    g.drawImage(imageAssets.getItemImage(model.getCatalogOnTile(new grid.Point(start.x, start.y)).getFocusedItem().getName()), x, y, null);
                }

                start.translate(1, 0);
            }
            start.move(startingX, start.y + 1);

        }
    }

    private int widthInTiles() {
        return (int)Math.ceil((double)bounds.getWidth() / ImageAssets.SPRITE_DIMENSION_PX);
    }

    private int heightInTiles() {
        return (int)Math.ceil((double)bounds.getHeight() / ImageAssets.SPRITE_DIMENSION_PX);
    }

    private int visibleColumns() {
        if(widthInTiles() % 2 == 0) {
            return widthInTiles() + 1;
        } else {
            return widthInTiles();
        }
    }

    private int visibleRows() {
        if(heightInTiles() % 2 == 0) {
            return heightInTiles() + 1;
        } else {
            return heightInTiles();
        }
    }

    private void recalculateTrueOrigin() {
        int trueXStart = -(((widthInTiles() * ImageAssets.SPRITE_DIMENSION_PX) - bounds.getWidth()) / 2);
        int trueYStart = -(((heightInTiles() * ImageAssets.SPRITE_DIMENSION_PX) - bounds.getHeight()) / 2);

        if(widthInTiles() % 2 == 0) {
            trueXStart -= (ImageAssets.SPRITE_DIMENSION_PX / 2);
        }

        if(heightInTiles() % 2 == 0) {
            trueYStart -= (ImageAssets.SPRITE_DIMENSION_PX / 2);
        }
        trueOrigin.setLocation(trueXStart, trueYStart);
    }
}
