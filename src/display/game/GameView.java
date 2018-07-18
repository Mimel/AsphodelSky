package display.game;

import display.image.ImageAssets;
import entity.Combatant;
import entity.Player;
import grid.CompositeGrid;

import javax.swing.*;
import java.awt.*;

public class GameView extends JPanel {
    private final CompositeGrid model;

    private Point trueOrigin;

    private GUISidebar sidebar;

    private GUIFooter footer;

    public GameView(CompositeGrid model, GUISidebar drawnSidebar, GUIFooter drawnFooter) {
        this.model = model;
        this.sidebar = drawnSidebar;
        this.footer = drawnFooter;
        this.trueOrigin = new Point(0, 0);
        recalculateTrueOrigin();
    }

    @Override
    protected void paintComponent(Graphics g) {
        long timeStart = System.currentTimeMillis();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        recalculateTrueOrigin();
        int testx = trueOrigin.x;
        int testy = trueOrigin.y;
        Point focus = new Point(model.getFocus().x(), model.getFocus().y());
        Point start = new Point(focus.x - widthInTiles() / 2, focus.y - heightInTiles() / 2);

        if(start.x < 0) { start.move(0, start.y); testx = 0; }
        else if(start.x > model.getNumberOfColumns() - visibleColumns()) {
            if(model.getNumberOfColumns() <= widthInTiles()) {
                start.move(0, start.y);
                testx = getWidth() - (widthInTiles() * ImageAssets.SPRITE_DIMENSION_PX);

                if(getWidth() > model.getNumberOfColumns() * ImageAssets.SPRITE_DIMENSION_PX) {
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
                testy = getHeight() - (heightInTiles() * ImageAssets.SPRITE_DIMENSION_PX);

                if(getHeight() > model.getNumberOfRows() * ImageAssets.SPRITE_DIMENSION_PX) {
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
            xMargin = (getWidth() - (model.getNumberOfColumns() * ImageAssets.SPRITE_DIMENSION_PX)) / 2;
        }

        if(heightInTiles() > model.getNumberOfRows()) {
            yMargin = (getHeight() - (model.getNumberOfRows() * ImageAssets.SPRITE_DIMENSION_PX)) / 2;
        }

        int startingX = start.x;

        Combatant currentCombatant;
        for(int y = testy + yMargin; y < getHeight() && start.y < model.getNumberOfRows(); y += ImageAssets.SPRITE_DIMENSION_PX) {
            for(int x = testx + xMargin; x < getWidth() && start.x < model.getNumberOfColumns(); x += ImageAssets.SPRITE_DIMENSION_PX) {
                g.drawImage(ImageAssets.getTerrainImage(model.getTileAt(start.x, start.y).getTerrain()), x, y, null);

                if((currentCombatant = model.getCombatantAt(start.x, start.y)) != null) {
                    if(currentCombatant.getId() == Player.PLAYER_ID) {
                        g.setColor(new Color(200, 100, 30));
                        g.fillRect(x, y, ImageAssets.SPRITE_DIMENSION_PX, ImageAssets.SPRITE_DIMENSION_PX);
                    } else {
                        g.drawImage(ImageAssets.getCharImage(currentCombatant.getName()), x, y, null);
                    }
                }

                if(model.getItemsOnTile(start.x, start.y) != null && !model.getItemsOnTile(start.x, start.y).isEmpty()) {
                    g.drawImage(ImageAssets.getItemImage(model.getItemsOnTile(start.x, start.y).getFocusedItem().getName()), x, y, null);
                }

                start.translate(1, 0);
            }
            start.move(startingX, start.y + 1);

        }

        sidebar.paint(g);
        footer.paint(g);

        long timeEnd = System.currentTimeMillis();
        System.out.println("Paint time: " + (timeEnd - timeStart) + "ms.");
    }

    public GUISidebar getSidebar() {
        return sidebar;
    }

    public GUIFooter getFooter() {
        return footer;
    }

    private int widthInTiles() {
        return (int)Math.ceil((double)getWidth() / ImageAssets.SPRITE_DIMENSION_PX);
    }

    private int heightInTiles() {
        return (int)Math.ceil((double)getHeight() / ImageAssets.SPRITE_DIMENSION_PX);
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
        int trueXStart = -(((widthInTiles() * ImageAssets.SPRITE_DIMENSION_PX) - getWidth()) / 2);
        int trueYStart = -(((heightInTiles() * ImageAssets.SPRITE_DIMENSION_PX) - getHeight()) / 2);

        if(widthInTiles() % 2 == 0) {
            trueXStart -= (ImageAssets.SPRITE_DIMENSION_PX / 2);
        }

        if(heightInTiles() % 2 == 0) {
            trueYStart -= (ImageAssets.SPRITE_DIMENSION_PX / 2);
        }
        trueOrigin.setLocation(trueXStart, trueYStart);
    }
}
