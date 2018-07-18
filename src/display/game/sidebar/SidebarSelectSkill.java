package display.game.sidebar;

import display.game.DrawingArea;
import display.image.ImageAssets;
import skill.SkillSet;

import java.awt.*;

public class SidebarSelectSkill implements SidebarSelectLayer {

    private final SkillSet targetSkills;

    public SidebarSelectSkill(SkillSet skills) {
        this.targetSkills = skills;
    }

    @Override
    public void paintSelection(Graphics g, DrawingArea bounds) {
        int slot = targetSkills.getFocusedSkillIndex();
        g.drawImage(ImageAssets.getMiscImage('+'),
                bounds.getXOffset() + GUISidebar.SKILLSET_OFFSET.x + (slot * ImageAssets.SPRITE_DIMENSION_PX),
                bounds.getYOffset() + GUISidebar.SKILLSET_OFFSET.y,
                null);
    }
}
