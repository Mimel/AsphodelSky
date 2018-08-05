package event;

import entity.Combatant;
import grid.Point;
import item.Item;
import skill.Skill;

/**
 * A data object that encapsulates all the data necessary for an event to be used.
 * InstructionSet data consists of six parts:
 *
 * A caster, which is the combatant that is performing the action. This must always be initialized on
 * InstructionData creation, can never change, and can never, ever be null (Though it can be a null object).
 *
 * A target, which is the combatant that is primarily affected by the action. This can be null before
 * entry into the EventQueue, but on entry into the EventQueue, this can never, ever be null.
 * The target is allowed to be the same as the caster.
 *
 * An item, which will be affected by an action. This item can be null, if an action in question
 * does not require it.
 *
 * A skill, which will be affected by an action. This skill can be null, if an action in question
 * does not require it.
 *
 * A tile, which is a Cartesian coordinate that will be affected by an action. This point can be null,
 * if an action in question does not require it.
 *
 * A secondary integer, whose usage depends entirely on the context of the action. Some actions
 * may not use secondary variables.
 *
 */
public class InstructionData {
    private final Combatant caster;
    private Combatant target;
    private Item item;
    private Skill skill;
    private Point tile;
    private int secondary;

    InstructionData(Combatant caster) {
        this.caster = caster;
    }

    public Combatant getCaster() {
        return caster;
    }

    public Combatant getTarget() {
        return target;
    }

    Item getItem() {
        return item;
    }

    Skill getSkill() {
        return skill;
    }

    public Point getTile() {
        return tile;
    }

    int getSecondary() {
        return secondary;
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
