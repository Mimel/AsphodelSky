package display.game.focus;

import display.image.ImageAssets;
import entity.Combatant;
import org.lwjgl.nanovg.NVGPaint;
import skill.SkillSet;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.nvglCreateImageFromHandle;

public class SidebarSkills {
    private SkillSet focusedSkillSet;
    private ImageAssets images;
    private FontData fonts;
    private NVGColorData colors;

    private boolean showFocusedSkillMarker;

    SidebarSkills(Combatant c, ImageAssets ia, FontData fd, NVGColorData cd) {
        this.focusedSkillSet = c.getSkillSet();
        this.images = ia;
        this.fonts = fd;
        this.colors = cd;

        this.showFocusedSkillMarker = false;
    }

    void showSkillMarker() {
        showFocusedSkillMarker = true;
    }

    void hideSkillMarker() {
        showFocusedSkillMarker = false;
    }

    void draw(long nvgContext, float sectionStartX, float sectionStartY) {
        NVGPaint image = NVGPaint.create();
        for (int i = 0; i < focusedSkillSet.size(); i++) {
            nvgImagePattern(nvgContext, sectionStartX + (i * ImageAssets.SPRITE_DIMENSION_SM_PX), sectionStartY,
                    ImageAssets.SPRITE_DIMENSION_SM_PX, ImageAssets.SPRITE_DIMENSION_SM_PX,
                    0.0f, nvglCreateImageFromHandle(nvgContext, images.getSkillTextureID(focusedSkillSet.getSkillAtIndex(i).getName()), ImageAssets.SPRITE_DIMENSION_SM_PX, ImageAssets.SPRITE_DIMENSION_SM_PX, 0), 1.0f,
                    image);

            nvgBeginPath(nvgContext);
            nvgRect(nvgContext, sectionStartX + (i * ImageAssets.SPRITE_DIMENSION_SM_PX), sectionStartY, ImageAssets.SPRITE_DIMENSION_SM_PX, ImageAssets.SPRITE_DIMENSION_SM_PX);
            nvgFillPaint(nvgContext, image);
            nvgFill(nvgContext);
        }

        if(showFocusedSkillMarker) {
            nvgImagePattern(nvgContext, sectionStartX + (focusedSkillSet.getFocusedSkillIndex() * ImageAssets.SPRITE_DIMENSION_SM_PX), sectionStartY,
                    ImageAssets.SPRITE_DIMENSION_SM_PX, ImageAssets.SPRITE_DIMENSION_SM_PX,
                    0.0f, nvglCreateImageFromHandle(nvgContext, images.getSmallMiscTextureID('+'), ImageAssets.SPRITE_DIMENSION_SM_PX, ImageAssets.SPRITE_DIMENSION_SM_PX, 0), 1.0f,
                    image);

            nvgBeginPath(nvgContext);
            nvgRect(nvgContext, sectionStartX + (focusedSkillSet.getFocusedSkillIndex() * ImageAssets.SPRITE_DIMENSION_SM_PX), sectionStartY, ImageAssets.SPRITE_DIMENSION_SM_PX, ImageAssets.SPRITE_DIMENSION_SM_PX);
            nvgFillPaint(nvgContext, image);
            nvgFill(nvgContext);
        }
    }
}
