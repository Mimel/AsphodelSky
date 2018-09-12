package display.game.focus;

import display.image.ImageAssets;
import entity.Combatant;
import item.Catalog;
import org.lwjgl.nanovg.NVGPaint;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.nvglCreateImageFromHandle;

class SidebarInventory {
    private final Catalog focusedCatalog;
    private final ImageAssets images;
    private final FontData fonts;
    private final NVGColorData colors;

    private boolean showFocusedItemMarker;

    SidebarInventory(Combatant c, ImageAssets ia, FontData fd, NVGColorData cd) {
        this.focusedCatalog = c.getInventory();
        this.images = ia;
        this.fonts = fd;
        this.colors = cd;

        this.showFocusedItemMarker = false;
    }

    void showItemMarker() {
        showFocusedItemMarker = true;
    }

    void hideItemMarker() {
        showFocusedItemMarker = false;
    }

    void draw(long nvgContext, float sectionStartX, float sectionStartY) {
        NVGPaint image = NVGPaint.create();
        for (int i = 0; i < focusedCatalog.size(); i++) {
            nvgImagePattern(nvgContext, sectionStartX + (i * ImageAssets.SPRITE_DIMENSION_SM_PX), sectionStartY,
                    ImageAssets.SPRITE_DIMENSION_SM_PX, ImageAssets.SPRITE_DIMENSION_SM_PX,
                    0.0f, nvglCreateImageFromHandle(nvgContext, images.getSmallItemTextureID(focusedCatalog.getItemAt(i).getName()), ImageAssets.SPRITE_DIMENSION_SM_PX, ImageAssets.SPRITE_DIMENSION_SM_PX, 0), 1.0f,
                    image);

            nvgBeginPath(nvgContext);
            nvgRect(nvgContext, sectionStartX + (i * ImageAssets.SPRITE_DIMENSION_SM_PX), sectionStartY, ImageAssets.SPRITE_DIMENSION_SM_PX, ImageAssets.SPRITE_DIMENSION_SM_PX);
            nvgFillPaint(nvgContext, image);
            nvgFill(nvgContext);

            nvgFontSize(nvgContext, 35.0f);
            nvgFontFace(nvgContext, fonts.MULI);
            nvgFillColor(nvgContext, colors.BLACK);
            nvgTextAlign(nvgContext, NVG_ALIGN_BOTTOM);
            nvgText(nvgContext, sectionStartX + (i * ImageAssets.SPRITE_DIMENSION_SM_PX), sectionStartY + (ImageAssets.SPRITE_DIMENSION_SM_PX), focusedCatalog.getAmountAt(i) + "");
        }

        if(showFocusedItemMarker) {
            nvgImagePattern(nvgContext, sectionStartX + (focusedCatalog.getFocusIndex() * ImageAssets.SPRITE_DIMENSION_SM_PX), sectionStartY,
                    ImageAssets.SPRITE_DIMENSION_SM_PX, ImageAssets.SPRITE_DIMENSION_SM_PX,
                    0.0f, nvglCreateImageFromHandle(nvgContext, images.getSmallMiscTextureID('+'), ImageAssets.SPRITE_DIMENSION_SM_PX, ImageAssets.SPRITE_DIMENSION_SM_PX, 0), 1.0f,
                    image);

            nvgBeginPath(nvgContext);
            nvgRect(nvgContext, sectionStartX + (focusedCatalog.getFocusIndex() * ImageAssets.SPRITE_DIMENSION_SM_PX), sectionStartY, ImageAssets.SPRITE_DIMENSION_SM_PX, ImageAssets.SPRITE_DIMENSION_SM_PX);
            nvgFillPaint(nvgContext, image);
            nvgFill(nvgContext);
        }
    }
}
