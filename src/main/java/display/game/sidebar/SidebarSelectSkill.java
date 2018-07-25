package display.game.sidebar;

import display.game.DrawingArea;
import display.image.ImageAssets;
import skill.SkillSet;

import java.awt.*;

public class SidebarSelectSkill implements SidebarSelectLayer {

    private final SkillSet targetSkills;
    private final ImageAssets iAssets;

    public SidebarSelectSkill(SkillSet skills, ImageAssets iAssets) {
        this.targetSkills = skills;
        this.iAssets = iAssets;
    }

    @Override
    public void paintSelection(Graphics g, DrawingArea bounds) {
        int slot = targetSkills.getFocusedSkillIndex();
        g.drawImage(iAssets.getMiscImage('+'),
                bounds.getXOffset() + GUISidebar.SKILLSET_OFFSET.x + (slot * ImageAssets.SPRITE_DIMENSION_SM_PX),
                bounds.getYOffset() + GUISidebar.SKILLSET_OFFSET.y,
                null);
    }
}
