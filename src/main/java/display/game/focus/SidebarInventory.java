package display.game.focus;

import display.image.ImageAssets;
import entity.Combatant;
import item.Catalog;
import org.lwjgl.nanovg.NVGPaint;

import static org.lwjgl.nanovg.NanoVG.*;

public class SidebarInventory {
    private Catalog focusedCatalog;
    private ImageAssets images;
    
    private float sectionStartX;
    private float sectionStartY;

    SidebarInventory(Combatant c, float x, float y, ImageAssets ia) {
        this.focusedCatalog = c.getInventory();
        this.sectionStartX = x;
        this.sectionStartY = y;
        this.images = ia;
    }

    void draw(long nvgContext) {
        NVGPaint image = NVGPaint.create();
        for (int i = 0; i < focusedCatalog.size(); i++) {
            nvgImagePattern(nvgContext, 0.0f, 0.0f,
                    ImageAssets.SPRITE_DIMENSION_SM_PX, ImageAssets.SPRITE_DIMENSION_SM_PX,
                    0.0f, images.getSmallMiscTextureID('+'), 1.0f,
                    image);

            nvgBeginPath(nvgContext);
            nvgRect(nvgContext, sectionStartX, sectionStartY, ImageAssets.SPRITE_DIMENSION_SM_PX, ImageAssets.SPRITE_DIMENSION_SM_PX);
            nvgFillPaint(nvgContext, image);
            nvgFill(nvgContext);
        }
    }
}
