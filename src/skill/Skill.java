package skill;

import entity.Combatant;
import event.SimpleEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A skill is a an ability that a combatant uses that alters the game state by adding
 * events to the event queue.
 */
public class Skill {

    /**
     * Keeps track of the current id number when a new item is created.
     * Thread-safe, on the off-chance that the individual item catalogs are created concurrently.
     */
    private static final AtomicInteger AUTO_INCR_ID = new AtomicInteger(0);

    /**
     * The id of the skill. The id of all different skills are unique,
     * and the id of all identical skills are identical.
     */
    private final int id;

    /**
     * The name of the skill.
     */
    private final String name;

    /**
     * A short string of text describing the skill in a colorful manner.
     */
    private final String desc_flavor;

    /**
     * A short string of text describing the effect the skill has, and how it alters the game state.
     */
    private final String desc_effect;

    /**
     * A list of simple events to add to an event queue on activation.
     */
    private final List<SimpleEvent> skillEffects;

    Skill(String name, String flavor, String effect, String events) {
        id = AUTO_INCR_ID.getAndIncrement();
        this.name = name;
        this.desc_flavor = flavor;
        this.desc_effect = effect;

        skillEffects = new ArrayList<>();
        for(String phrase : events.split(";")) {
            skillEffects.add(SimpleEvent.interpretEvent(phrase));
        }
    }

    Skill(Skill s) {
        this.id = s.id;
        this.name = s.name;
        this.desc_flavor = s.desc_flavor;
        this.desc_effect = s.desc_effect;
        this.skillEffects = s.skillEffects;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc_flavor() {
        return desc_flavor;
    }

    public String getDesc_effect() {
        return desc_effect;
    }

    /**
     * Uses the skill.
     * @param target The target of the skill.
     * @return The simple events in the skill, but deep-copied, and with the target id applied to all of them.
     */
    public List<SimpleEvent> useSkill(Combatant target) {
        List<SimpleEvent> skillEventsDeepCopy = new ArrayList<>();
        for(SimpleEvent se : skillEffects) {
            SimpleEvent temporarilyRevisedEvent = new SimpleEvent(se);
            temporarilyRevisedEvent.getData().setTargetTo(target);
            skillEventsDeepCopy.add(temporarilyRevisedEvent);
        }

        return skillEventsDeepCopy;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Equates this skill with another. If the specified object is non-null, a Skill, and
     * has an id equal to this skill, then both skills are equal.
     * @param obj The specified object to equate this skill to.
     * @return True if the criteria in the description are fulfilled, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }

        if(this.getClass() != obj.getClass()) {
            return false;
        }

        return (this.getId() == ((Skill) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
