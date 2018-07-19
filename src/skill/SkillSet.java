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

    public SkillSet(String skillSetRepresentation) {
        this.skillList = new ArrayList<>();
        this.focusedSkillIndex = 0;

        for(String skillName : skillSetRepresentation.split(",")) {
            if(skillName.equals("END")) {
                continue;
            }

            addSkill(SkillLoader.getSkillByName(skillName));
        }
    }

    public SkillSet(SkillSet old) {
        this.skillList = new ArrayList<>();
        this.focusedSkillIndex = old.focusedSkillIndex;

        for(Skill s : old.skillList) {
            this.addSkill(s);
        }
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
        List<Skill> newSkills = new ArrayList<>(skillList);
        return newSkills;
    }

    public boolean isSkillSetEmpty() {
        return skillList.size() == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Skill s : skillList) {
            sb.append(s.toString()).append(',');
        }
        sb.append("END");

        return sb.toString();
    }
}
