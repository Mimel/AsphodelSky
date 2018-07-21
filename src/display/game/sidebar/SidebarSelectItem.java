package display.game.sidebar;

import display.game.DrawingArea;
import display.image.ImageAssets;
import item.Catalog;

import javax.imageio.IIOImage;
import java.awt.*;

public class SidebarSelectItem implements SidebarSelectLayer {
    private final Catalog targetCatalog;
    private final ImageAssets iAssets;

    public SidebarSelectItem(Catalog catalog, ImageAssets iAssets) {
        this.targetCatalog = catalog;
        this.iAssets = iAssets;
    }
    @Override
    public void paintSelection(Graphics g, DrawingArea bounds) {
        int slot = targetCatalog.getFocusIndex();
        g.drawImage(iAssets.getMiscImage('+'),
                bounds.getXOffset() + GUISidebar.INVENTORY_OFFSET.x + (slot / GUISidebar.INVENTORY_ROWS * ImageAssets.SPRITE_DIMENSION_PX),
                bounds.getYOffset() + GUISidebar.INVENTORY_OFFSET.y + (slot % GUISidebar.INVENTORY_ROWS * ImageAssets.SPRITE_DIMENSION_PX),
                null);
    }
}
