package skill;

import java.util.ArrayList;
import java.util.List;

public class SkillSet {
    private List<Skill> skillList;

    private int focusedSkillIndex;

    public SkillSet() {
        skillList = new ArrayList<>();
        focusedSkillIndex = 0;
    }

    public int getFocusedSkillIndex() {
        return focusedSkillIndex;
    }

    public void resetFocusedSkillIndex() {
        focusedSkillIndex = 0;
    }

    public Skill getFocusedSkill() {
        if(focusedSkillIndex < skillList.size()) {
            return skillList.get(focusedSkillIndex);
        }

        return null;
    }

    public boolean setFocusedSkillIndex(int offset) {
        if(focusedSkillIndex + offset >= 0 && focusedSkillIndex + offset < skillList.size()) {
            focusedSkillIndex += offset;
            return true;
        }

        return false;
    }

    public void addSkill(Skill newSkill) {
        skillList.add(newSkill);
    }

    public List<Skill> getSkillsDeepCopy() {
        List<Skill> newSkills = new ArrayList<>();
        for(Skill s : skillList) {
            newSkills.add(s);
        }
        return newSkills;
    }

    public boolean isSkillSetEmpty() {
        return skillList.size() == 0;
    }
}
