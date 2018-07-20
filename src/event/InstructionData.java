package event;

import entity.Combatant;
import grid.Point;
import item.Item;
import skill.Skill;

/**
 * A data object that encapsulates all the data necessary for an event to be used.
 */
public class InstructionData {
    private Combatant caster;
    private Combatant target;
    private Item item;
    private Skill skill;
    private Point tile;
    private int secondary;

    InstructionData() {

    }

    Combatant getCaster() {
        return caster;
    }

    Combatant getTarget() {
        return target;
    }

    Item getItem() {
        return item;
    }

    Skill getSkill() {
        return skill;
    }

    Point getTile() {
        return tile;
    }

    int getSecondary() {
        return secondary;
    }

    public InstructionData setCasterTo(Combatant caster) {
        this.caster = caster;
        return this;
    }

    public InstructionData setTargetTo(Combatant target) {
        this.target = target;
        return this;
    }

    public InstructionData setItemTo(Item item) {
        this.item = item;
        return this;
    }

    public InstructionData setSkillTo(Skill skill) {
        this.skill = skill;
        return this;
    }

    public InstructionData setCoordTo(Point loc) {
        this.tile = loc;
        return this;
    }

    public InstructionData setSecondaryTo(int sec) {
        this.secondary = sec;
        return this;
    }
}
