package display.game.sidebar;

import display.game.DrawingArea;
import display.image.ImageAssets;
import item.Catalog;

import java.awt.*;

public class SidebarSelectItem implements SidebarSelectLayer {
    private final Catalog targetCatalog;

    public SidebarSelectItem(Catalog catalog) {
        this.targetCatalog = catalog;
    }
    @Override
    public void paintSelection(Graphics g, DrawingArea bounds) {
        int slot = targetCatalog.getFocusIndex();
        g.drawImage(ImageAssets.getMiscImage('+'),
                bounds.getXOffset() + GUISidebar.INVENTORY_OFFSET.x + (slot / GUISidebar.INVENTORY_ROWS * ImageAssets.SPRITE_DIMENSION_PX),
                bounds.getYOffset() + GUISidebar.INVENTORY_OFFSET.y + (slot % GUISidebar.INVENTORY_ROWS * ImageAssets.SPRITE_DIMENSION_PX),
                null);
    }
}
