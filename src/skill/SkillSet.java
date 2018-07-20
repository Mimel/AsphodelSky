package skill;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of skills. Note that this implementation acts like a set in that
 * it cannot contain duplicate skills.
 *
 * A skill set, as a string, is represented by the following format, where "[SKILL NAME]"
 * can be replaced by the exact name of a skill;
 * "[SKILL NAME],[SKILL NAME],[SKILL NAME],...,END"
 */
public class SkillSet {

    /**
     * The full list of skills in this set.
     */
    private final List<Skill> skillList;

    /**
     * An integer indicating an index in the skillList
     * to center operations on. Must be a number between 0
     * and one less than the number of elements in the skillList.
     */
    private int focusedSkillIndex;

    public SkillSet() {
        skillList = new ArrayList<>();
        focusedSkillIndex = 0;
    }

    /**
     * Loads a skillSet from a formatted string.
     * Such a string must have exact skill names delimited by commas, and end with the
     * word 'END' after the last comma.
     * @param skillSetRepresentation A formatted string.
     */
    public SkillSet(String skillSetRepresentation, SkillLibrary skillMappings) {
        this.skillList = new ArrayList<>();
        this.focusedSkillIndex = 0;

        for(String skillName : skillSetRepresentation.split(",")) {
            if(skillName.equals("END")) {
                continue;
            }

            addSkill(skillMappings.getSkillByName(skillName));
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

    /**
     * Resets the focused skill index to zero.
     */
    public void resetFocusedSkillIndex() {
        focusedSkillIndex = 0;
    }

    /**
     * Gets the skill at the index pointed to by the focused skill index.
     * @return The skill at the focused skill index.
     */
    public Skill getFocusedSkill() {
        if(focusedSkillIndex < skillList.size()) {
            return skillList.get(focusedSkillIndex);
        }

        return null;
    }

    /**
     * Sets the focused skill index by adding an offset to the index. If the total sum produces a number between
     * 0 and one less than the number of elements in the skill list, then the change is made.
     * If not, then the focused skill index remains the same.
     * @param offset An integer to add to the current focused skill index.
     * @return True if the new sum is valid, and the change was made. False otherwise.
     */
    public boolean setFocusedSkillIndex(int offset) {
        if(focusedSkillIndex + offset >= 0 && focusedSkillIndex + offset < skillList.size()) {
            focusedSkillIndex += offset;
            return true;
        }

        return false;
    }

    /**
     * Adds a skill to the end of the skill list.
     * If the skill is null or already exists in the list, then the skill is not added.
     * @param newSkill The new skill to add.
     */
    private void addSkill(Skill newSkill) {
        if(newSkill != null && !skillList.contains(newSkill)) {
            skillList.add(newSkill);
        }
    }

    public void transferFrom(SkillSet skillSetToTransferFrom) {
        for(Skill s : skillSetToTransferFrom.skillList) {
            this.addSkill(s);
        }

        skillSetToTransferFrom.skillList.clear();
    }

    /**
     * Gets a deep copy of the skill list.
     * @return A deep copy of the skill list.
     */
    public List<Skill> getSkillsDeepCopy() {
        return new ArrayList<>(skillList);
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
