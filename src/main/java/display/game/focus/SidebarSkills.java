package display.game.focus;

import entity.Combatant;
import skill.SkillSet;

public class SidebarSkills {
    private SkillSet focusedSkillSet;
    
    private float sectionStartX;
    private float sectionStartY;

    SidebarSkills(Combatant c, float x, float y) {
        this.focusedSkillSet = c.getSkillSet();
        this.sectionStartX = x;
        this.sectionStartY = y;
    }

    void draw(long nvgContext) {

    }
}
