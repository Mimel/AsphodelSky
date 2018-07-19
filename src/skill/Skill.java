package skill;

import event.SimpleEvent;

import java.util.ArrayList;
import java.util.List;
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

    private final int id;

    private String name;

    private String desc_flavor;

    private String desc_effect;

    private List<SimpleEvent> skillEffects;

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

    public List<SimpleEvent> useSkill(int targetID) {
        List<SimpleEvent> skillEventsDeepCopy = new ArrayList<>();
        for(SimpleEvent se : skillEffects) {
            SimpleEvent temporarilyRevisedEvent = new SimpleEvent(se);
            temporarilyRevisedEvent.getData().setTargetIDTo(targetID);
            skillEventsDeepCopy.add(temporarilyRevisedEvent);
        }

        return skillEventsDeepCopy;
    }

    @Override
    public String toString() {
        return name;
    }
}
